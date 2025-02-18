package com.machinecoding.cache;

import com.machinecoding.backend.StorageBackend;
import com.machinecoding.helpers.Cleaner;
import com.machinecoding.model.CacheEntry;
import com.machinecoding.policies.EvictionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;



public class Cache<K, V> implements ICache<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(Cache.class);
    private final Map<K, CacheEntry<V>> internalMap;
    private final int capacity;
    private final EvictionPolicy<K> evictionPolicy;
    private final StorageBackend<K, CacheEntry<V>> storageBackend;
    private final Cleaner<K, V> cleaner;

    public Cache(int capacity, EvictionPolicy<K> evictionPolicy, StorageBackend<K, CacheEntry<V>> storageBackend) {
        this.internalMap = new ConcurrentHashMap<>();
        this.capacity = capacity;
        this.evictionPolicy = evictionPolicy;
        this.storageBackend = storageBackend;
        this.cleaner = new Cleaner<>(Executors.newSingleThreadScheduledExecutor());

        if(storageBackend != null){
            Map<K, CacheEntry<V>> data = storageBackend.load();
            for(Map.Entry<K, CacheEntry<V>> entry : data.entrySet()){
                internalMap.put(entry.getKey(), entry.getValue());
                evictionPolicy.keyAccessed(entry.getKey());
            }
        }
        cleaner.startCleanUpTask(internalMap, 5 * 1000);
    }

    protected void removeEntry(K key){
        internalMap.remove(key);
        if(storageBackend != null){
            storageBackend.remove(key);
        }
    }


    @Override
    public void put(K key, CacheEntry<V> value) {
        if(internalMap.containsKey(key)){
            internalMap.put(key, value);
            evictionPolicy.keyAccessed(key);
            if(storageBackend != null){
                storageBackend.put(key, value);
            }
            return;
        }
        if(internalMap.size() >= capacity){
            K evictedKey = evictionPolicy.evictKey();
            internalMap.remove(evictedKey);
            if(storageBackend != null){
                storageBackend.remove(evictedKey);
            }
        }

        internalMap.put(key, value);
        evictionPolicy.keyAccessed(key);
        if(storageBackend != null){
            storageBackend.put(key, value);
        }
    }

    @Override
    public V get(K key) {
        if(!internalMap.containsKey(key)){
            return null;
        }
        evictionPolicy.keyAccessed(key);
        logger.info("Get Value {} for key", internalMap.get(key).getValue());
        return internalMap.get(key).getValue();
    }

    public String toString(){
        for(K key : internalMap.keySet()){
            System.out.println(key.toString() + " : " + internalMap.get(key).getValue());
        }
        return "";
    }


}

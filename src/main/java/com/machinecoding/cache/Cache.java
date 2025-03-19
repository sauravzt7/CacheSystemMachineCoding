package com.machinecoding.cache;

import com.machinecoding.helpers.Cleaner;
import com.machinecoding.model.CacheEntry;
import com.machinecoding.policies.eviciton.EvictionPolicy;
import com.machinecoding.policies.write.WritePolicy;
import com.machinecoding.policies.write.WriteThroughPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import static com.machinecoding.backend.SQLiteStorageBackend.load;
import static com.machinecoding.constants.Constants.DB_URL;
import static com.machinecoding.constants.Constants.TABLE_NAME;


public class Cache<K, V> implements ICache<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(Cache.class);
    private final Map<K, CacheEntry<V>> internalMap;
    private final int capacity;
    private final EvictionPolicy<K> evictionPolicy;
    private final WritePolicy<K, CacheEntry<V>> writePolicy;

    public Cache(int capacity, EvictionPolicy<K> evictionPolicy) {
        this.internalMap = new ConcurrentHashMap<>();
        this.capacity = capacity;
        this.evictionPolicy = evictionPolicy;
        Cleaner<K, V> cleaner = new Cleaner<>(Executors.newSingleThreadScheduledExecutor());
        this.writePolicy = new WriteThroughPolicy<>();
        internalMap.putAll(load(DB_URL, TABLE_NAME)); // option to load the data to cache upon restart
        cleaner.startCleanUpTask(internalMap, 5 * 1000);
    }

    protected void removeEntry(K key){
        internalMap.remove(key);
    }

    @Override
    public void put(K key, CacheEntry<V> value) {
        if(internalMap.size() >= capacity){
            K evictedKey = evictionPolicy.evictKey();
            internalMap.remove(evictedKey);
        }

        if(!internalMap.containsKey(key)){
            logger.warn("updating key {}", key);
            internalMap.put(key, value);
            writePolicy.write(key, value);
            evictionPolicy.keyAccessed(key);
            return;
        }


        internalMap.put(key, value);
        writePolicy.write(key, value);
        evictionPolicy.keyAccessed(key);
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
        System.out.println("Current cache size: " + internalMap.size());
        for(K key : internalMap.keySet()){
            System.out.println(key.toString() + " : " + internalMap.get(key).getValue());
        }
        return "";
    }


}

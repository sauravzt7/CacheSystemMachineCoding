package com.machinecoding.cache;

import com.machinecoding.helpers.Cleaner;
import com.machinecoding.model.CacheEntry;
import com.machinecoding.policies.EvictionPolicy;
import com.machinecoding.policies.WritePolicy;
import com.machinecoding.policies.WriteThroughPolicy;
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
    private final Cleaner<K, V> cleaner;

    public Cache(int capacity, EvictionPolicy<K> evictionPolicy) {
        this.internalMap = new ConcurrentHashMap<>();
        this.capacity = capacity;
        this.evictionPolicy = evictionPolicy;
        this.cleaner = new Cleaner<>(Executors.newSingleThreadScheduledExecutor());
        this.writePolicy = new WriteThroughPolicy<>();
        load(DB_URL, TABLE_NAME); // option to load the data to cache upon restart
        cleaner.startCleanUpTask(internalMap, 5 * 1000);
    }

    protected void removeEntry(K key){
        internalMap.remove(key);
    }


    @Override
    public void put(K key, CacheEntry<V> value) {
        if(internalMap.containsKey(key)){
            internalMap.put(key, value);
            writePolicy.write(key, value);
            evictionPolicy.keyAccessed(key);
            return;
        }
        if(internalMap.size() >= capacity){
            K evictedKey = evictionPolicy.evictKey();
            internalMap.remove(evictedKey);
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
        for(K key : internalMap.keySet()){
            System.out.println(key.toString() + " : " + internalMap.get(key).getValue());
        }
        return "";
    }


}

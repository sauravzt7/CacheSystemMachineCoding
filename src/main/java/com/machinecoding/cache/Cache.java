package com.machinecoding.cache;

import com.machinecoding.policies.EvictionPolicy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache<K, V> implements ICache<K, V> {

    private final Map<K, V> internalMap;
    private final int capacity;
    private final EvictionPolicy<K> evictionPolicy;


    public Cache(int capacity, EvictionPolicy<K> evictionPolicy) {
        this.internalMap = new ConcurrentHashMap<>();
        this.capacity = capacity;
        this.evictionPolicy = evictionPolicy;
    }


    @Override
    public void put(K key, V value) {
        if(internalMap.containsKey(key)){
            internalMap.put(key, value);
            evictionPolicy.keyAccessed(key);
            return;
        }
        if(internalMap.size() >= capacity){
            K evictedKey = evictionPolicy.evictKey();
            internalMap.remove(evictedKey);
        }

        internalMap.put(key, value);
        evictionPolicy.keyAccessed(key);
    }

    @Override
    public V get(K key) {
        if(!internalMap.containsKey(key)){
            return null;
        }
        evictionPolicy.keyAccessed(key);
        return internalMap.get(key);
    }

    public Map<K, V> getInternalMap() {
        return this.internalMap;
    }

    public String toString(){
        for(K key : internalMap.keySet()){
            System.out.println(key.toString() + " : " + internalMap.get(key));
        }
        return "";
    }
}

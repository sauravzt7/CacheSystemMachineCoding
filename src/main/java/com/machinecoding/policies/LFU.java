package com.machinecoding.policies;

import java.util.HashMap;
import java.util.Map;

public class LFU<K> implements EvictionPolicy<K> {

    private final Map<K, Integer> accessCount;

    public LFU() {
        this.accessCount = new HashMap<>();
    }
    @Override
    public void keyAccessed(K key) {
        accessCount.put(key, accessCount.getOrDefault(key, 0) + 1);

    }

    @Override
    public K evictKey() {
        if(accessCount.isEmpty()){
            throw new IllegalStateException("Eviction attempted without any keys in cache.");
        }
        int minCount = Integer.MAX_VALUE;
        K minKey = null;
        for(Map.Entry<K, Integer> entry : accessCount.entrySet()){
            if(entry.getValue() < minCount){
                minCount = entry.getValue();
                minKey = entry.getKey();
            }
        }
        System.out.println("evicted key is : " + minKey);
        return minKey;
    }
}

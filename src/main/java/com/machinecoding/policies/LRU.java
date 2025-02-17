package com.machinecoding.policies;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRU<K> implements EvictionPolicy<K> {

    private final Map<K, Boolean> accessOrder;


    public LRU(){
        this.accessOrder = new LinkedHashMap<>(16, 0.75f, true);
    }

    @Override
    public void keyAccessed(K key) {
        accessOrder.put(key, Boolean.TRUE);
    }

    @Override
    public K evictKey() {
        if(accessOrder.isEmpty()){
            throw new IllegalStateException("Eviction attempted without any keys in cache.");
        }
        return accessOrder.keySet().iterator().next();
    }

}

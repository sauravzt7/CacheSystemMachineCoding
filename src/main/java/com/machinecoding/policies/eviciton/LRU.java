package com.machinecoding.policies.eviciton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRU<K> implements EvictionPolicy<K> {

    private final Map<K, Boolean> accessOrder;
    private static final Logger logger = LoggerFactory.getLogger(LRU.class);


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
        logger.info("evicted key is : " + accessOrder.keySet().iterator().next());
        return accessOrder.keySet().iterator().next();
    }

}

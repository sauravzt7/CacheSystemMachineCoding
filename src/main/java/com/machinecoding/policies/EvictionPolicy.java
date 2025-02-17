package com.machinecoding.policies;

public interface EvictionPolicy<K> {

    void keyAccessed(K key);
    K evictKey();
}

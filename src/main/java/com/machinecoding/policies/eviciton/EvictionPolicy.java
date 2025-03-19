package com.machinecoding.policies.eviciton;

public interface EvictionPolicy<K> {

    void keyAccessed(K key);
    K evictKey();
}

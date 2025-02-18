package com.machinecoding.cache;

import com.machinecoding.model.CacheEntry;

public interface ICache<K, V> {
    void put(K key, CacheEntry<V> value);
    V get(K key);
}

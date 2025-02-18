package com.machinecoding.backend;

import java.util.Map;

public interface StorageBackend<K, V> {
    void put(K key, V value);
    V get(K key);
    void remove(K key);
    void clear();
    Map<K, V> load(); // Load all key-value pairs from persistent storage
}


package com.machinecoding.policies;

import com.machinecoding.cache.Cache;
import com.machinecoding.model.CacheEntry;
import com.machinecoding.storage.PersistentStorage;

public interface WritePolicy<K, V> {

    void write(K key, V value);

}

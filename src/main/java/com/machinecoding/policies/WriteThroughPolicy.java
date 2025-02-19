package com.machinecoding.policies;

import com.machinecoding.backend.StorageBackend;
import com.machinecoding.cache.Cache;
import com.machinecoding.factory.StorageBackendFactory;
import com.machinecoding.model.CacheEntry;
import com.machinecoding.storage.PersistentStorage;

public class WriteThroughPolicy<K, V> implements WritePolicy<K, V>{
    private StorageBackend<K, V> storageBackend;

    public WriteThroughPolicy() {
        this.storageBackend = StorageBackendFactory.getSQLiteStorageBackend();
    }

    @Override
    public void write(K key, V value) {
        storageBackend.put(key, value); // write immediately to the Persistent Storage
    }
}

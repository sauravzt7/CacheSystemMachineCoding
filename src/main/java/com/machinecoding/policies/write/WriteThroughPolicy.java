package com.machinecoding.policies.write;

import com.machinecoding.backend.StorageBackend;
import com.machinecoding.factory.StorageBackendFactory;

public class WriteThroughPolicy<K, V> implements WritePolicy<K, V> {
    private StorageBackend<K, V> storageBackend;

    public WriteThroughPolicy() {
        this.storageBackend = StorageBackendFactory.getSQLiteStorageBackend();
    }

    @Override
    public void write(K key, V value) {
        storageBackend.put(key, value); // write immediately to the Persistent Storage
    }
}

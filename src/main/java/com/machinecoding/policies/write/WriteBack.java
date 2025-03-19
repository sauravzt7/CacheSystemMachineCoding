package com.machinecoding.policies.write;

import com.machinecoding.backend.StorageBackend;
import com.machinecoding.factory.StorageBackendFactory;
import com.machinecoding.model.CacheEntry;

import java.util.HashMap;
import java.util.Map;

import static com.machinecoding.helpers.WriteBackWorker.flushToDB;

public class WriteBack<K, V> implements WritePolicy<K, V> {

    private StorageBackend<K, CacheEntry<V>> storageBackend;

    public WriteBack() {
        this.storageBackend = StorageBackendFactory.getSQLiteStorageBackend();
    }

    private final Map<K, CacheEntry<V>> dirtyKeys = new HashMap<>();

    @Override
    public void write(K key, V value) {
        dirtyKeys.putIfAbsent(key, (CacheEntry<V>) value);
        if(dirtyKeys.size() == 1000){
            flushToDB(dirtyKeys, storageBackend);
        }
    }



}

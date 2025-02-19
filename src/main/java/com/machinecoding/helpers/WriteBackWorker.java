package com.machinecoding.helpers;

import com.machinecoding.backend.StorageBackend;
import com.machinecoding.model.CacheEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WriteBackWorker {
    private static ScheduledExecutorService executor;
    private static final Logger logger = LoggerFactory.getLogger(Cleaner.class);
    

    // Method to start cleanup task asynchronously
    public static<K, V> void flushToDB(Map<K, CacheEntry<V>> dirtyEntries, StorageBackend<K, CacheEntry<V>> storageBackend) {
        executor = Executors.newSingleThreadScheduledExecutor(); // Ensure executor is initialized
        executor.schedule(() -> {
            for (Map.Entry<K, CacheEntry<V>> entry : dirtyEntries.entrySet()) {
                try {
                    storageBackend.put(entry.getKey(), entry.getValue());
                    logger.info("Flushed to DB key: {}, value: {}", entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    logger.error("Failed to flush key: {} to DB", entry.getKey(), e);
                }
            }
        }, 0, TimeUnit.MILLISECONDS);
    }

}

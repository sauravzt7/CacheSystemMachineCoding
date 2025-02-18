package com.machinecoding.helpers;

import com.machinecoding.model.CacheEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Cleaner<K, V> {


    private static ScheduledExecutorService executor;
    private static final Logger logger = LoggerFactory.getLogger(Cleaner.class);

    public Cleaner(ScheduledExecutorService executor){
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    // Method to start cleanup task asynchronously
    public void startCleanUpTask(Map<K, CacheEntry<V>> internalMap, long rateInMillis) {
        executor.scheduleAtFixedRate(() -> {
            // Iterate through the map to remove expired entries

            Iterator<Map.Entry<K, CacheEntry<V>>> iterator = internalMap.entrySet().iterator();
            while (iterator.hasNext()) {
                logger.info("Removing expired entries key: {}, value: {}", iterator.next().getKey(), iterator.next().getValue());
                Map.Entry<K, CacheEntry<V>> entry = iterator.next();
                if (entry.getValue().isExpired()) {
                    iterator.remove(); // Remove expired entry from the map
                }
            }
        }, rateInMillis, rateInMillis, TimeUnit.MILLISECONDS);
    }


    public void stopCleanUpTask(){
        executor.shutdown();
    }
}

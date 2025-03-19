package com.machinecoding.model;

import java.time.Instant;

public class CacheEntry<V> {
    public V value;
    public final long expirationTime;

    public CacheEntry(V value, long ttlInMillis) {
        this.value = value;
        this.expirationTime = Instant.now().toEpochMilli() + ttlInMillis;
    }

    public V getValue() {
        return value;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public boolean isExpired() {
        return Instant.now().toEpochMilli() > expirationTime;
    }
}

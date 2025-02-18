package com.machinecoding.policies;

import org.w3c.dom.ls.LSOutput;

import java.util.LinkedList;
import java.util.Queue;

public class FIFO<K> implements EvictionPolicy<K> {

    private final Queue<K> queue;

    public FIFO() {
        this.queue = new LinkedList<K>();
    }

    @Override
    public void keyAccessed(K key) {
        queue.add(key);
    }


    @Override
    public K evictKey() {
        if(queue.isEmpty()) {
            throw new IllegalStateException("Eviction attempted without any keys in cache.");
        }
        return queue.poll();
    }
}

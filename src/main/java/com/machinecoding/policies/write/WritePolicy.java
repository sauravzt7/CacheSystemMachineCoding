package com.machinecoding.policies.write;

public interface WritePolicy<K, V> {

    void write(K key, V value);

}

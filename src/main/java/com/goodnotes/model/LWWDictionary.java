package com.goodnotes.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class LWWDictionary<K, V> {

    private Map<K, TimestampedValue> addMap;
    private Map<K, LocalDateTime> removeMap;

    public LWWDictionary() {
        addMap = new HashMap<>();
        removeMap = new HashMap<>();
    }

    public boolean containsKey(K key) {
        return addMap.containsKey(key) && (!removeMap.containsKey(key) || removeMap.get(key).isBefore(addMap.get(key).timestamp));
    }

    public void add(K key, V value) {
        add(key, value, LocalDateTime.now());
    }

    public void add(K key, V value, LocalDateTime timestamp) {
        addMap.put(key, new TimestampedValue(value, timestamp));
    }

    public void remove(K key) {
        remove(key, LocalDateTime.now());
    }

    public void remove(K key, LocalDateTime timestamp) {
        removeMap.put(key, timestamp);
    }

    public int size() {
        return (int) addMap.entrySet().stream().filter(entry -> !removeMap.containsKey(entry.getKey()) || entry.getValue().timestamp.isAfter(removeMap.get(entry.getKey()))).count();
    }

    public V lookup(K key){
        return containsKey(key) ? (V) addMap.get(key).value : null;
    }

    class TimestampedValue<V> {
        LocalDateTime timestamp;
        V value;

        public TimestampedValue(V value) {
            this.value = value;
            this.timestamp = LocalDateTime.now();
        }

        public TimestampedValue(V value, LocalDateTime timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }
    }
}

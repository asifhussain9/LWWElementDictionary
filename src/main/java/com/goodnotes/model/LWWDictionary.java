package com.goodnotes.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    public void update(K oldKey, K newKey) {
        TimestampedValue remove = containsKey(oldKey) ? addMap.remove(oldKey) : null;
        add(newKey, remove == null ? null : (V)remove.value, LocalDateTime.now());
    }

    public void update(K oldKey, K newKey, LocalDateTime timestamp) {
        TimestampedValue remove = addMap.remove(oldKey);
        add(newKey, remove == null ? null : (V)remove.value, timestamp);
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

    public Map<K, TimestampedValue> merge() {
        return addMap.entrySet().stream().filter(entry -> !removeMap.containsKey(entry.getKey()) || removeMap.get(entry.getKey()).isBefore(entry.getValue().timestamp)).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
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

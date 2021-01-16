package com.goodnotes.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * HashMap based implementation of the <tt>LWWDictionary</tt>.
 *
 * @param <K> the type of keys maintained by this dictionary
 * @param <V> the type of mapped values
 *
 * @author  Asif Hussain
 */
public class LWWDictionary<K, V> {

    /**
     * Map to add values.
     */
    private Map<K, TimestampedValue> addMap;
    /**
     * Map to remove values.
     */
    private Map<K, LocalDateTime> removeMap;

    public LWWDictionary() {
        addMap = new HashMap<>();
        removeMap = new HashMap<>();
    }

    /**
     * Checks if the key is present in dictionary
     */
    public boolean containsKey(K key) {
        return addMap.containsKey(key) && (!removeMap.containsKey(key) || removeMap.get(key).isBefore(addMap.get(key).timestamp));
    }

    /**
     * Adds a key, value entry to dictionary. Assumes current timestamp.
     */
    public void add(K key, V value) {
        add(key, value, LocalDateTime.now());
    }

    /**
     * Adds a key, value entry to dictionary with provided timestamp.
     */
    public void add(K key, V value, LocalDateTime timestamp) {
        addMap.put(key, new TimestampedValue(value, timestamp));
    }

    /**
     * Updates key value. If old key is not present in the dictionary, adds null value. Assumes current timestamp
     */
    public void update(K oldKey, K newKey) {
        TimestampedValue remove = containsKey(oldKey) ? addMap.remove(oldKey) : null;
        add(newKey, remove == null ? null : (V)remove.value, LocalDateTime.now());
    }

    /**
     * Updates key value. If old key is not present in the dictionary, adds null value.
     */
    public void update(K oldKey, K newKey, LocalDateTime timestamp) {
        TimestampedValue remove = addMap.remove(oldKey);
        add(newKey, remove == null ? null : (V)remove.value, timestamp);
    }

    /**
     * Removes an entry from dictionary. Assumes current timestamp.
     */
    public void remove(K key) {
        remove(key, LocalDateTime.now());
    }

    /**
     * Removes an entry from dictionary.
     */
    public void remove(K key, LocalDateTime timestamp) {
        removeMap.put(key, timestamp);
    }

    /**
     * Returns the size of dictionary using AddMap and RemoveMap
     */
    public int size() {
        return (int) addMap.entrySet().stream().filter(entry -> !removeMap.containsKey(entry.getKey()) || entry.getValue().timestamp.isAfter(removeMap.get(entry.getKey()))).count();
    }

    /**
     * method to lookup value for a key in the dictionary
     */
    public V lookup(K key){
        return containsKey(key) ? (V) addMap.get(key).value : null;
    }

    /**
     * returns a new map by merging addMap and removeMap
     */
    public Map<K, TimestampedValue> merge() {
        return addMap.entrySet().stream().filter(entry -> !removeMap.containsKey(entry.getKey()) || removeMap.get(entry.getKey()).isBefore(entry.getValue().timestamp)).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
    }

    /**
     * Class to store value along with timestamp in the addMap
     */
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

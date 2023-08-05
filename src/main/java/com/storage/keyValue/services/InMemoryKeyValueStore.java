package com.storage.keyValue.services;

import java.util.HashMap;
import java.util.Map;

public class InMemoryKeyValueStore<K, V> implements IKeyValueStore<K, V> {
    private final Map<K, Map<String, V>> store;
    private final Map<Object, K> primaryIndex;
    private final Map<String, Map<Object, Map<K, Map<String, V>>>> secondaryIndexes;

    public InMemoryKeyValueStore() {
        this.store = new HashMap<>();
        this.primaryIndex = new HashMap<>();
        this.secondaryIndexes = new HashMap<>();
    }

    @Override
    public void put(K key, Map<String, V> attributes) {
        store.put(key, attributes);

        // Update primary index
        Object primaryKey = attributes.get("primaryKey");
        if (primaryKey != null) {
            primaryIndex.put(primaryKey, key);
        }

        // Update secondary indexes if needed
        for (String attributeName : attributes.keySet()) {
            if (!attributeName.equals("primaryKey")) {
                if (secondaryIndexes.containsKey(attributeName)) {
                    Object value = attributes.get(attributeName);

                    // Initialize the secondary index map if not present
                    if (!secondaryIndexes.get(attributeName).containsKey(value)) {
                        secondaryIndexes.get(attributeName).put(value, new HashMap<>());
                    }

                    // Put the key and its attributes into the secondary index
                    secondaryIndexes.get(attributeName).get(value).put(key, attributes);
                }
            }
        }
    }


    @Override
    public Map<String, V> get(K key) {
        return store.get(key);
    }

    @Override
    public void delete(K key) {
        Map<String, V> attributes = store.remove(key);

        // Remove from primary index if needed
        Object primaryKey = attributes.get("primaryKey");
        if (primaryKey != null) {
            primaryIndex.remove(primaryKey);
        }

        // Remove from secondary indexes if needed
        for (String attributeName : attributes.keySet()) {
            if (!attributeName.equals("primaryKey")) {
                if (secondaryIndexes.containsKey(attributeName)) {
                    Object value = attributes.get(attributeName);
                    secondaryIndexes.get(attributeName).remove(value);
                }
            }
        }
    }

    @Override
    public void createPrimaryIndex() {
        for (K key : store.keySet()) {
            Map<String, V> attributes = store.get(key);
            Object primaryKey = attributes.get("primaryKey");
            if (primaryKey != null) {
                primaryIndex.put(primaryKey, key);
            }
        }
    }


    @Override
    public void createSecondaryIndex(String attributeName) {
        secondaryIndexes.put(attributeName, new HashMap<>());
    }

    @Override
    public K queryByPrimaryIndex(Object value) {
        return primaryIndex.get(value);
    }

    @Override
    public Map<K, Map<String, V>> queryBySecondaryIndex(String indexName, Object value) {
        if (!secondaryIndexes.containsKey(indexName)) {
            throw new IllegalArgumentException("Index not found: " + indexName);
        }

        Map<K, Map<String, V>> secondaryIndex = secondaryIndexes.get(indexName).get(value);
        if (secondaryIndex != null) {
            return secondaryIndex;
        } else {
            return new HashMap<>();
        }
    }
}
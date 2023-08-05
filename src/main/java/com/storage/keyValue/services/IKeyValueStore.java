package com.storage.keyValue.services;

import java.util.Map;

public interface IKeyValueStore<K, V> {
    void put(K key, Map<String, V> attributes); // key, {attributeName, V: dataType of attribute value}
    Map<String, V> get(K key);
    void delete(K key);
    void createPrimaryIndex();
    void createSecondaryIndex(String attributeName);
    K queryByPrimaryIndex(Object value);
    Map<K, Map<String, V>> queryBySecondaryIndex(String indexName, Object value);
}
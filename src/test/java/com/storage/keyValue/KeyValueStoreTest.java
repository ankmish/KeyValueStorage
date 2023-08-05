package com.storage.keyValue;


import com.storage.keyValue.services.IKeyValueStore;
import com.storage.keyValue.services.InMemoryKeyValueStore;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class KeyValueStoreTest {
    private IKeyValueStore<String, Object> keyValueStore;

    @BeforeEach
    public void setup() {
        keyValueStore = new InMemoryKeyValueStore<>();
    }

    @Test
    public void testInsertAndGet() {
        Map<String, Object> attributes1 = new HashMap<>();
        attributes1.put("primaryKey", "1");
        attributes1.put("name", "Alice");
        attributes1.put("age", 30);
        attributes1.put("city", "New York");
        keyValueStore.put("1", attributes1);

        Map<String, Object> attributes2 = new HashMap<>();
        attributes2.put("primaryKey", "2");
        attributes2.put("name", "Bob");
        attributes2.put("age", 25);
        attributes2.put("city", "San Francisco");
        keyValueStore.put("2", attributes2);

        assertEquals(attributes1, keyValueStore.get("1"));
        assertEquals(attributes2, keyValueStore.get("2"));
    }

    @Test
    public void testQueryByPrimaryIndex() {
        Map<String, Object> attributes1 = new HashMap<>();
        attributes1.put("primaryKey", "1");
        attributes1.put("name", "Alice");
        attributes1.put("age", 30);
        attributes1.put("city", "New York");
        keyValueStore.put("1", attributes1);

        keyValueStore.createPrimaryIndex();

        assertEquals("1", keyValueStore.queryByPrimaryIndex("1"));
    }

    @Test
    public void testDelete() {
        Map<String, Object> attributes1 = new HashMap<>();
        attributes1.put("primaryKey", "1");
        attributes1.put("name", "Alice");
        attributes1.put("age", 30);
        attributes1.put("city", "New York");
        keyValueStore.put("1", attributes1);

        keyValueStore.delete("1");
        assertNull(keyValueStore.get("1"));
    }
}
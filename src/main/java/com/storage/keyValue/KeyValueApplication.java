package com.storage.keyValue;

import com.storage.keyValue.services.IKeyValueStore;
import com.storage.keyValue.services.InMemoryKeyValueStore;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KeyValueApplication {

	public static void main(String[] args) {
		IKeyValueStore<String, Object> keyValueStore = new InMemoryKeyValueStore<>();

		keyValueStore.createSecondaryIndex("age");
		keyValueStore.createSecondaryIndex("city");

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

		// Create the primary index
		keyValueStore.createPrimaryIndex();

		// Query by primary index example
		String key = keyValueStore.queryByPrimaryIndex("1");
		System.out.println("Query Result (Primary Index): " + keyValueStore.get(key));
		// Output: Query Result (Primary Index): {primaryKey=1, name=Alice, age=30, city=New York}
		// Query by secondary index example
		Map<String, Map<String, Object>> queryResult = keyValueStore.queryBySecondaryIndex("age", 30);
		System.out.println("Query Result (Secondary Index - Age): " + queryResult);
		// Output: Query Result (Secondary Index - Age): {1={primaryKey=1, name=Alice, age=30, city=New York}}
	}

}

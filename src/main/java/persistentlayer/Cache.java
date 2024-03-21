package persistentlayer;

import customexceptions.CustomException;

public interface Cache<K, V> {
	void set(K key, V value) throws CustomException;

	V get(K key) throws CustomException;

	default void put(K key, V value) throws CustomException {
		set(key, value);
	}

	void remove(K key) throws CustomException;
}

package logicallayer;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import persistentlayer.Cache;

public class LRUCache<K, V> implements Cache<K, V> {

	private int capacity;

	private Map<K, V> map;
	private Deque<K> deque;

	public LRUCache(int capacity) {
		this.capacity = capacity;
		map = new HashMap<>();
		deque = new LinkedList<>();
	}

	public void set(K key, V value) {
		if (!map.containsKey(key)) {
			if (deque.size() == capacity) {
				K lru = deque.removeLast();
				map.remove(lru);
			}
		} else {
			deque.remove(key);
		}
		deque.push(key);
		map.put(key, value);
	}

	public V get(K key) {
		if (deque.remove(key)) {
			deque.push(key);
		}
		return map.get(key);
	}

	public void remove(K key) {
		deque.remove(key);
		map.remove(key);
	}
}

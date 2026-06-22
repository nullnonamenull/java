
import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> {
	
	private final int capacity;
	private final Map<K, V> cache;

	public LruCache(int capacity) {
		this.capacity = capacity;

		this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
			@Override
			protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
				return size() > LruCache.this.capacity;
			}
		};
	}

	public V get(K key) {
		return cache.get(key);
	}

	public void put(K key, V value) {
		cache.put(key, value);
	}

	@Override
	public String toString() {
		return cache.toString();
	}

}
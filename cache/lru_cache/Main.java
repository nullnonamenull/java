

public class Main {
	
	public static void main(String[] args) {
		LruCache<String, String> cache = new LruCache<>(2);

		cache.put("user:1", "Mirek");
		System.out.println(cache);
	}

}
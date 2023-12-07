import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>(2);

        cache.put(1, "A");
        cache.put(2, "B");
        cache.put(3, "C"); // Evicts key 1

        System.out.println(cache.get(1)); // Returns null
        System.out.println(cache.get(2)); // Returns B
        System.out.println(cache.get(3)); // Returns C
    }
}

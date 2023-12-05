import java.util.*;

public class LFUCache<K, V> {
    private final Map<K, Node> cache;
    private final PriorityQueue<Node> frequencyQueue;
    private final int capacity;

    private class Node implements Comparable<Node> {
        K key;
        V value;
        int frequency;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.frequency = 1;
        }

        @Override
        public int compareTo(Node other) {
            return this.frequency - other.frequency; // Higher frequency first
        }
    }

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.frequencyQueue = new PriorityQueue<>();
    }

    public V get(K key) {
        Node node = cache.get(key);
        if (node == null) {
            return null;
        }
        node.frequency++;
        frequencyQueue.remove(node); // Remove and re-insert to update the PriorityQueue
        frequencyQueue.add(node);
        return node.value;
    }

    public void put(K key, V value) {
        if (cache.containsKey(key)) {
            Node node = cache.get(key);
            node.value = value;
            node.frequency++;
            frequencyQueue.remove(node); // Remove and re-insert to update the PriorityQueue
            frequencyQueue.add(node);
        } else {
            Node newNode = new Node(key, value);


            if (cache.size() == capacity) {
                Node toEvict = frequencyQueue.poll();
                if (toEvict != null) {
                    cache.remove(toEvict.key);
                }
            }
            cache.put(key, newNode);
            frequencyQueue.add(newNode);
        }
    }
    public static void main(String[] args) {
        LFUCache<Integer, String> cache = new LFUCache<>(3);

        cache.put(1, "A");
        cache.put(2, "B");
        cache.put(3, "C"); // Evicts key 1

        System.out.println(cache.get(1)); // Returns null
        System.out.println(cache.get(2)); // Returns B
        System.out.println(cache.get(1)); // Returns null
        System.out.println(cache.get(2)); // Returns B
        System.out.println(cache.get(1)); // Returns null
        System.out.println(cache.get(2)); // Returns B
        System.out.println(cache.get(3)); // Returns C
        cache.put(4, "D"); // Evicts key 1
        System.out.println(cache.get(1));
        System.out.println(cache.get(2));
        System.out.println(cache.get(3)); // Returns C
        System.out.println(cache.get(4));
    }
}
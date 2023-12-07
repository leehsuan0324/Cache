import java.util.*;
// Thread-safe
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class LRUCache<K, V> {
    private final Map<K, Node> cache;
    private final int capacity;
    private Node head, tail;
    // randomAccess is used to get a random key in O(1) time
    private final ArrayList<Node> randomAccess;
    /* 
    private final Lock lock = new ReentrantLock();
    try {
        lock.lock();
        // do something
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        lock.unlock();
    }
    */

    private class Node {
        K key;
        V value;
        Node prev, next;
        // randomAccess is used to get a random key in O(1) time
        int index;
        
    }
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        // randomAccess is used to get a random key in O(1) time
        this.randomAccess = new ArrayList<>();
    }

    public V get(K key) {
        Node node = cache.get(key);
        if (node == null) {
            return null;
        }
        moveToHead(node);
        return node.value;
    }

    public void put(K key, V value) {
        Node node = cache.get(key);
        if (node == null) {
            if (cache.size() == capacity) {
                Node tail = popTail();
                cache.remove(tail.key);
                // randomAccess is used to get a random key in O(1) time
                randomAccess.get(randomAccess.size() - 1).index = tail.index;
                randomAccess.set(tail.index, randomAccess.get(randomAccess.size() - 1));
                randomAccess.remove(randomAccess.size() - 1);
            }
            Node newNode = new Node();
            newNode.key = key;
            newNode.value = value;
            // randomAccess is used to get a random key in O(1) time
            newNode.index = randomAccess.size();
            addNode(newNode);
            cache.put(key, newNode);
            // randomAccess is used to get a random key in O(1) time
            randomAccess.add(newNode);
        } else {
            node.value = value;
            moveToHead(node);
        }
    }   
    // manual remove
    public void remove(K key) {
        Node node = cache.get(key);
        if (node == null) {
            return;
        }
        removeNode(node);
        cache.remove(key);
        randomAccess.get(randomAccess.size() - 1).index = node.index;
        randomAccess.set(node.index, randomAccess.get(randomAccess.size() - 1));
        randomAccess.remove(randomAccess.size() - 1);
    }
    // randomAccess is used to get a random key in O(1) time
    public K getRandomKey() {
        if (randomAccess.isEmpty()) {
            return null;
        }
        int randomIndex = (int) (Math.random() * randomAccess.size());
        return randomAccess.get(randomIndex).key;
    }
    private void addNode(Node node) {
        node.prev = null;
        node.next = head;

        if (head != null) {
            head.prev = node;
        }
        head = node;

        if (tail == null) {
            tail = head;
        }
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    private void moveToHead(Node node) {
        removeNode(node);
        addNode(node);
    }

    private Node popTail() {
        Node res = tail;
        removeNode(tail);
        return res;
    }

    /*
        int n = 2; // LRU Cache size
        String query = "get 1 set 3 C get 2";

        LRUCache<Integer, String> cache = new LRUCache<>(n);
        Scanner scanner = new Scanner(query);

        while (scanner.hasNext()) {
            String action = scanner.next();

            switch (action) {
                case "get":
                    if (scanner.hasNextInt()) {
                        int key = scanner.nextInt();
                        System.out.println("get " + key + " -> " + cache.get(key));
                    }
                    break;
                case "set":
                    if (scanner.hasNextInt()) {
                        int key = scanner.nextInt();
                        if (scanner.hasNext()) {
                            String value = scanner.next();
                            cache.put(key, value);
                            System.out.println("set " + key + " -> " + value);
                        }
                    }
                    break;
            }
        }

        scanner.close();
    */

    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>(5);

        cache.put(1, "A");
        cache.put(2, "B");
        cache.put(3, "C"); 
        cache.put(4, "D");
        cache.put(5, "E");
        cache.put(6, "F"); // Evicts key 1
        cache.put(7, "G"); // Evicts key 2

        System.out.println(cache.get(1)); // Returns null
        System.out.println(cache.get(2)); // Returns null
        System.out.println(cache.get(3)); // Returns C
        System.out.println(cache.get(4)); // Returns D
        
        cache.put(8, "H"); // Evicts key 5
        cache.put(9, "I"); // Evicts key 6
        cache.remove(3);
        cache.put(10, "J");
        for(int i = 0; i < 50; i++) {
            System.out.println(cache.getRandomKey());
        }
        /*
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 50; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(cache.getRandomKey());
                }
            });
        }
        executor.shutdown(); // Initiate shutdown
        try {
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) { // Wait for tasks to finish
                executor.shutdownNow(); // Force shutdown if tasks are not finished
            }
        } catch (InterruptedException e) {
            executor.shutdownNow(); // Force shutdown on interruption
            Thread.currentThread().interrupt(); // Preserve interruption status
        }
         */
    }
}

import java.util.HashMap;
import java.util.Map;

public class MRUCache<K, V> {
    private final Map<K, Node> cache;
    private final int capacity;
    private Node head, tail;

    private class Node {
        K key;
        V value;
        Node prev, next;
    }

    public MRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
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
            Node newNode = new Node();
            newNode.key = key;
            newNode.value = value;

            cache.put(key, newNode);
            addNode(newNode);

            if (cache.size() > capacity) {
                Node head = popHead();
                cache.remove(head.key);
            }
        } else {
            node.value = value;
            moveToHead(node);
        }
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

    private Node popHead() {
        Node res = head;
        removeNode(head);
        return res;
    }
}

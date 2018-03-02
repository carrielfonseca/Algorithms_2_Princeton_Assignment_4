import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class TSTFabioNoGeneric{
    private int n;              // size
    private Node root;   // root of TST
//    private Node<Value> currentNode; //lastNode in the TST
//    private String currentWord;

    private static class Node {
        private char c;                        // character
        private Node left, mid, right;  // left, middle, and right subtries
        private boolean val;                     // value associated with string
        
    }

    /**
     * Initializes an empty string symbol table.
     */
    public TSTFabioNoGeneric() {   
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != false;
    }

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.val;
    }

    // return subtrie corresponding to given key
    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, boolean val) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (!contains(key)) n++;
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, boolean val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if      (c < x.c)               x.left  = put(x.left,  key, val, d);
        else if (c > x.c)               x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
        else                            x.val   = val;
        return x;
    }

     public boolean hasKeysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        Queue<String> queue = new Queue<String>();
        
        Node x = get(root, prefix, 0);             
        if (x == null) return false;
        if (x.val != false) {
        	queue.enqueue(prefix);
        }
        collectKey(x.mid, new StringBuilder(prefix), queue);
        return !queue.isEmpty();
    }
     
    private void collectKey(Node x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collectKey(x.left,  prefix, queue);
        if (x.val != false) {
        	queue.enqueue(prefix.toString() + x.c);
        	return;
        }
        collectKey(x.mid,   prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collectKey(x.right, prefix, queue);
    }    
}


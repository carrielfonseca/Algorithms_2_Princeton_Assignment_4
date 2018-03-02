import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieST;

public class MultiwayTrieNoGenericFabio {
    private static final int R = 26;        // extended ASCII
    private Node root;      // root of trie
    private int n;          // number of keys in trie
    private boolean hasPrefix;
    private Node currentNode;

    // R-way trie node
    private static class Node {
        private boolean val;
        private Node[] next = new Node[R];        
    }

   /**
     * Initializes an empty string symbol table.
     */
    public MultiwayTrieNoGenericFabio() {
    	hasPrefix = false;
    	currentNode = root;
    }


    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean get(StringBuilder key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return  x.val;
    }
    
    public boolean get(String key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return  x.val;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(StringBuilder key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != false;
    }
    
    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != false;
    }

    private Node get(Node x, StringBuilder key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = (char) (key.charAt(d) -  'A') ;        
        return get(x.next[c], key, d+1);
    }
    
    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = (char) (key.charAt(d) -  'A') ;        
        return get(x.next[c], key, d+1);
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
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (val == false) delete(key);
        else root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, boolean val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (x.val == false) n++;
            x.val = true;
            return x;
        }
        char c = (char) (key.charAt(d) - 'A');
        x.next[c] = put(x.next[c], key, val, d+1);
        return x;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }
    
	//non-recursive implementation
	public boolean hasKeysWithPrefix(String prefix) {
		Node x = currentNode;
        for (int i = 0; i < prefix.length() && x != null; i++)
            x = x.next[(char) (prefix.charAt(i)- 'A')];
		
		
		if (x == null) {
			x = root;
	        for (int i = 0; i < prefix.length() && x != null; i++)
	            x = x.next[(char) (prefix.charAt(i)- 'A')];
		}
        
        
        currentNode = x;
        
        return x != null;
    }
    
    // recursive implementation
//	public boolean hasKeysWithPrefix(String prefix) {
//		boolean hasPrefix;
//		Node x = get(root, prefix, 0);
//		collectKey(x, new StringBuilder(prefix));
//		hasPrefix = this.hasPrefix;
//		this.hasPrefix = false;
//		return hasPrefix;
//	}
//    
//	private void collectKey(Node x, StringBuilder prefix) {
//		if (x == null)	return;
//		if (x.val != false)	{
//			hasPrefix = true;
//			return;  //dont continue if finds one key with prefix
//		}
//		for (char c = 0; c < R; c++) {
//			prefix.append(((char)(c+ 'A')));
//			collectKey(x.next[c], prefix);
//			prefix.deleteCharAt(prefix.length() - 1);
//		}
//	}

	/**
     * Removes the key from the set if the key is present.
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(String key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            if (x.val != false) n--;
            x.val = false;
        }
        else {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d+1);
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.val != false) return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }

    /**
     * Unit tests the {@code TrieST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // build symbol table from standard input
        TrieST<Integer> st = new TrieST<Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }

        // print results
        if (st.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : st.keys()) {
                StdOut.println(key + " " + st.get(key));
            }
            StdOut.println();
        }

        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(st.longestPrefixOf("shellsort"));
        StdOut.println();

        StdOut.println("longestPrefixOf(\"quicksort\"):");
        StdOut.println(st.longestPrefixOf("quicksort"));
        StdOut.println();

        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : st.keysWithPrefix("shor"))
            StdOut.println(s);
        StdOut.println();

        StdOut.println("keysThatMatch(\".he.l.\"):");
        for (String s : st.keysThatMatch(".he.l."))
            StdOut.println(s);
    }
}

    


   
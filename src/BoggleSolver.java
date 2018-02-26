import edu.princeton.cs.algs4.TST;

public class BoggleSolver {

	TST<Boolean> dictionaryInTrie = new TST<>();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
    	for (String word : dictionary) {
    		dictionaryInTrie.put(word, true);
    	}
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
//    public Iterable<String> getAllValidWords(BoggleBoard board)

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
//    public int scoreOf(String word)
	
	public static void main(String[] args) {
	    
	}
}
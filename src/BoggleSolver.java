import edu.princeton.cs.algs4.TST;

public class BoggleSolver {

	TST<Boolean> dictionaryInTrie = new TST<>(); // ternary search tries
	boolean[] marked;  //if true, means the cell is already visited in a certain path in the Boggle Boad

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
    
    // returns the index of the cel in a matrix with a numberOfRows and a numberOfCols
    // read from left to right, top to bottom
    private int vertexIndex(int i, int j, int numberOfRows, int numberOfCols) {
    	int vertexIndex =  i*numberOfCols + j;
    	return vertexIndex;
    }
    
    private static int rowOfVertex(int vertexIndex, int numberOfRows, int numberOfCols) {
    	int rowOfVertex =  (vertexIndex / numberOfCols);
    	return rowOfVertex;
    }
    
    private static int colOfVertex(int vertexIndex, int numberOfRows, int numberOfCols) {
    	int colOfVertex =  (vertexIndex % numberOfCols);
    	return colOfVertex;
    }
	
	public static void main(String[] args) {
		int test = colOfVertex(9,10,5);
		System.out.println(test);
		
		
	    
	}
}
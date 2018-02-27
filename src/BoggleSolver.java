import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.TST;


// words with at least 3 letters
// no duplication in words
// Qu case
public class BoggleSolver {

	TST<Boolean> dictionaryInTrie = new TST<>(); // ternary search tries
	Graph boggleGraph;
	boolean[] marked;  //if true, means the cell is already visited in a certain path in the Boggle Boad

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
    	for (String word : dictionary) {    		
    		dictionaryInTrie.put(word, true);    		
    	}
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
    	buildsBoggleGraph(board, boggleGraph);
    	ArrayList<String> validWords = new ArrayList<>();
    	for (int i = 0; i < board.rows()*board.cols(); i++) {    	
    		visitSquare(board, boggleGraph, i ,"", validWords);    	
    	}
    	return validWords;    	
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
    	int points;
    	if (word.length() >= 3 && word.length() <= 4) {
    		points = 1;
    	} 
    	else if (word.length() == 5)  {
    		points = 2;
    	}
    	else if (word.length() == 6)  {
    		points = 3;
    	}
    	else if (word.length() == 7)  {
    		points = 5;
    	}
    	else if (word.length() >= 8)  {
    		points = 11;
    	}
    	else {
    		points = 0;
    	}
    	return points;
    }
    
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
    
    private void addEdgesToNeighboors(Graph graph, int i, int j, int numberOfRows, int numberOfCols) {
    	for (int l = (i-1); l <= (i+1); i++) {
    		for (int m = (j-1); m <= (j+1); j++) {
    			if (l >= 0 && l <= (numberOfRows-1) && m >= 0 && m <= (numberOfCols-1)) {
    				graph.addEdge(vertexIndex(i, j, numberOfRows, numberOfCols), vertexIndex(l, m, numberOfRows, numberOfCols));
    			}
    		}
    	}
    }
    
    private void buildsBoggleGraph(BoggleBoard board,Graph boggleGraph) {
		for (int i = 0; i < board.rows(); i++) {
			for (int j = 0; j < board.cols(); j++) {
				addEdgesToNeighboors(boggleGraph, i, j, board.rows(), board.rows());
			}
		}
    }
    
    private void visitSquare(BoggleBoard board, Graph boggleGraph, int vertex, String word, List<String> words) {
    	marked[vertex] = true;
    	word = word + board.getLetter(rowOfVertex(vertex,  board.rows(), board.cols()), colOfVertex(vertex,  board.rows(), board.cols()));
    	// word must be in the dictionary AND have at least 3 letters
    	if (dictionaryInTrie.contains(word) && word.length() >= 3) { 
    		words.add(word);
    	}
    	for (int v : boggleGraph.adj(vertex)) {
    		Queue<String> keysWithPrefix = (Queue<String>) dictionaryInTrie.keysWithPrefix(word);
    		//does not need to visit a square if you know there arent any words that start with those caracters
    		if (!marked[v] && !keysWithPrefix.isEmpty()) {
    			visitSquare(board, boggleGraph, v, word, words);
    		}
    	}
    	word = word.substring(0, word.length()-2); //takes out the last character of the word
    	marked[vertex] = false;
    }
    
    private int countStringOccurrences(String s, String stringToCount) {    	
    	int counter = 0;
    	for(int i=0; i<s.length(); i++) {
    	    if( (s.substring(i, stringToCount.length()-1)).equals(stringToCount)) {
    	        counter++;
    	    } 
    	}
    	return counter;
    }
	
	public static void main(String[] args) {
		String test = "";
		test = test + "a";
		System.out.println(test.equals("a"));
		
		
	    
	}
}
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolverStringBuilder {

	private TSTFabioNoGeneric dictionaryInTrie = new TSTFabioNoGeneric(); // ternary search tries
	private ArrayList<Bag<Integer>> adj; // adjacent squares that can be visited from each vertex
	private boolean[] marked;  //if true, means the cell is already visited in a certain path in the Boggle Boad
	private char[] charVertex; //the chararcter of the ith vertex
	private StringBuilder stringBuilder;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolverStringBuilder(String[] dictionary) {
    	for (String word : dictionary) {    		
    		dictionaryInTrie.put(word, true);    		
    	}
    	stringBuilder = new StringBuilder();
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
    	int n = board.rows()*board.cols();
    	adj =  new ArrayList<>(n);
    	charVertex = new char[n];
    	for (int v = 0; v < n; v++) {
            adj.add(new Bag<Integer>());
            charVertex[v] = board.getLetter(rowOfVertex(v,board.rows(), board.cols()), colOfVertex(v,board.rows(), board.cols()));
        }
    	marked = new boolean[n];
    	buildsNeighboors(board);
    	Set<String> validWords = new HashSet<>();
    	for (int i = 0; i < n; i++) {    	
    		visitSquare(board, i , stringBuilder, validWords);
    		stringBuilder.setLength(0); // set length of buffer to 0
    		stringBuilder.trimToSize(); // trim the underlying buffer
    	}
    	return validWords;    	
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
    	int points;
    	int wordLength = word.length();;
    	if (!dictionaryInTrie.contains(word)) {
    		points = 0;
    	}    	
    	else if (wordLength >= 3 && wordLength <= 4) {
    		points = 1;
    	} 
    	else if (wordLength == 5)  {
    		points = 2;
    	}
    	else if (wordLength == 6)  {
    		points = 3;
    	}
    	else if (wordLength == 7)  {
    		points = 5;
    	}
    	else if (wordLength >= 8)  {
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
    
    private int rowOfVertex(int vertexIndex, int numberOfRows, int numberOfCols) {
    	int rowOfVertex =  (vertexIndex / numberOfCols);
    	return rowOfVertex;
    }
    
    private int colOfVertex(int vertexIndex, int numberOfRows, int numberOfCols) {
    	int colOfVertex =  (vertexIndex % numberOfCols);
    	return colOfVertex;
    }
    
    private void addNeighboors(int i, int j, int numberOfRows, int numberOfCols) {
    	int vertexIndex = vertexIndex(i, j, numberOfRows, numberOfCols);
    	for (int l = (i-1); l <= (i+1); l++) {
    		for (int m = (j-1); m <= (j+1); m++) {
    			//if within boudaries AND not the same square (cannot have self reference)     			
    			if (l >= 0 && l <= (numberOfRows-1) && m >= 0 && m <= (numberOfCols-1)
    			    && !(l == i && m == j)) {
    				adj.get(vertexIndex).add(vertexIndex(l, m, numberOfRows, numberOfCols));
    			}
    		}
    	}
    }
    
    private void buildsNeighboors(BoggleBoard board) {
		for (int i = 0; i < board.rows(); i++) {
			for (int j = 0; j < board.cols(); j++) {
				addNeighboors(i, j, board.rows(), board.cols());
			}
		}
    }
    
    private void visitSquare(BoggleBoard board,  int vertex, StringBuilder word, Set<String> words) {
    	marked[vertex] = true;
    	word.append(charVertex[vertex]);
    	// makes a correction for the special case of letter "Q"
    	if (charVertex[vertex] == 'Q') {
    		word.append("U");
    	}   	    	
    	// word must be in the dictionary AND have at least 3 letters
    	if (word.length() >= 3 && dictionaryInTrie.contains(word)) { 
    		words.add(word.toString());
    	}
    	for (int v : adj.get(vertex)) {
    		//does not need to visit a square if you know there arent any words that start with those caracters
    		if (!marked[v] && dictionaryInTrie.hasKeysWithPrefix(word)) {
    			visitSquare(board, v, word, words);
    		}
    	}
    	if (charVertex[vertex] == 'Q') {
    		word.deleteCharAt(word.length()-1); //takes out the last 2 characters of the word if finds a Q
    		word.deleteCharAt(word.length()-1);
    	}
    	else {
    		word.deleteCharAt(word.length()-1); //takes out the last character of the word if not a Q
    	}
    	
    	marked[vertex] = false;
    }
    
	public static void main(String[] args) {
//		String word = "H";
//		word = word.substring(0, word.length()-1);
//		System.out.println(word);
		In in = new In("dictionary-yawl.txt");
	    String[] dictionary = in.readAllStrings();
	    BoggleSolverStringBuilder solver = new BoggleSolverStringBuilder(dictionary);
	    BoggleBoard board = new BoggleBoard("board-points26539.txt");
	    System.out.println(board);
	    int score = 0;
	    for (String word : solver.getAllValidWords(board)) {
	        StdOut.println(word);
	        score += solver.scoreOf(word);
	    }
	    StdOut.println("Score = " + score);

	}
}
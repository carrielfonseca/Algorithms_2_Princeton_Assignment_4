import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

public class BoggleSolver {

	private TST<Boolean> dictionaryInTrie = new TST<>(); // ternary search tries
	private Graph boggleGraph;
	private boolean[] marked;  //if true, means the cell is already visited in a certain path in the Boggle Boad

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
    	for (String word : dictionary) {    		
    		dictionaryInTrie.put(word, true);    		
    	}
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
    	boggleGraph = new Graph(board.rows()*board.cols());
    	marked = new boolean[board.rows()*board.cols()];
    	buildsBoggleGraph(board, boggleGraph);
    	Set<String> validWords = new TreeSet<>();
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
    
    private int rowOfVertex(int vertexIndex, int numberOfRows, int numberOfCols) {
    	int rowOfVertex =  (vertexIndex / numberOfCols);
    	return rowOfVertex;
    }
    
    private int colOfVertex(int vertexIndex, int numberOfRows, int numberOfCols) {
    	int colOfVertex =  (vertexIndex % numberOfCols);
    	return colOfVertex;
    }
    
    private void addEdgesToNeighboors(Graph graph, int i, int j, int numberOfRows, int numberOfCols) {
    	for (int l = (i-1); l <= (i+1); l++) {
    		for (int m = (j-1); m <= (j+1); m++) {
    			//if within boudaries AND not the same square (cannot have self reference) 
    			
    			if (l >= 0 && l <= (numberOfRows-1) && m >= 0 && m <= (numberOfCols-1)
    			    && !(l == i && m == j)) {
    				graph.addEdge(vertexIndex(i, j, numberOfRows, numberOfCols), vertexIndex(l, m, numberOfRows, numberOfCols));
    			}
    		}
    	}
    }
    
    private void buildsBoggleGraph(BoggleBoard board,Graph boggleGraph) {
		for (int i = 0; i < board.rows(); i++) {
			for (int j = 0; j < board.cols(); j++) {
				addEdgesToNeighboors(boggleGraph, i, j, board.rows(), board.cols());
			}
		}
    }
    
    private void visitSquare(BoggleBoard board, Graph boggleGraph, int vertex, String word, Set<String> words) {
    	marked[vertex] = true;
    	String boardLetter = "" +  board.getLetter(rowOfVertex(vertex,  board.rows(), board.cols()), colOfVertex(vertex,  board.rows(), board.cols()));
    	// makes a correction for the special case of letter "Q"
    	if (boardLetter.equalsIgnoreCase("Q")) {
    		boardLetter = "QU"; 
    	}
    	word = word + boardLetter;
    	int countQu = countStringOccurrences(word, "QU");
    	int lengthAdjusted = word.length() - countQu;
    	// word must be in the dictionary AND have at least 3 letters
    	if (dictionaryInTrie.contains(word) && lengthAdjusted >= 3) { 
    		words.add(word);
    	}
    	for (int v : boggleGraph.adj(vertex)) {
    		Queue<String> keysWithPrefix = (Queue<String>) dictionaryInTrie.keysWithPrefix(word);
    		//does not need to visit a square if you know there arent any words that start with those caracters
    		if (!marked[v] && !keysWithPrefix.isEmpty()) {
    			visitSquare(board, boggleGraph, v, word, words);
    		}
    	}
    	if (boardLetter.equalsIgnoreCase("Qu")) {
    		word = word.substring(0, word.length()-2); //takes out the last 2 characters of the word if finds a Q
    	}
    	else {
    		word = word.substring(0, word.length()-1); //takes out the last character of the word if not a Q
    	}
    	
    	marked[vertex] = false;
    }
    
    private int countStringOccurrences(String s, String stringToCount) {    	
    	int counter = 0;
    	for(int i=0; i<(s.length()-stringToCount.length()+1); i++) {
    	    if( (s.substring(i, i+stringToCount.length())).equalsIgnoreCase(stringToCount)) {
    	        counter++;
    	    } 
    	}
    	return counter;
    }
	
	public static void main(String[] args) {
//		String word = "Hw";
//		word = word.substring(0, word.length()-1);
//		System.out.println(word);
		In in = new In("dictionary-algs4.txt");
	    String[] dictionary = in.readAllStrings();
	    BoggleSolver solver = new BoggleSolver(dictionary);
	    BoggleBoard board = new BoggleBoard("board-q.txt");
	    System.out.println(board);
	    System.out.println(board.getLetter(2, 1));
	    int score = 0;
	    for (String word : solver.getAllValidWords(board)) {
	        StdOut.println(word);
	        score += solver.scoreOf(word);
	    }
	    StdOut.println("Score = " + score);	    
	}
}
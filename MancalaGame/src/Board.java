
public class Board {
	
	private int[] board;
	
	public Board(){}
	
	/* 
	 * Initialize the board
	 * Size : Number of pits for each player
	 */
	public Board(int size, int numOfStones){
		//Number of pits for each player + 2 mancalas
		int lenOfArray = 2*size + 2;
		
		board = new int[lenOfArray];

		for(int i=0; i<lenOfArray-1; i++){
			board[i] = numOfStones;
		}
		
		//Set the Mancala pits to zero
		board[size] = 0; //Mancala for player 1
		board[lenOfArray] = 0; //Mancala for player 2
	}
	
	
	/*
	 * Print the board
	 */
	
	public String toString(){
		String ret = new String();
		for (int i : board){
			ret = "i" + "," ;
		}
		
		//remove the last comma
		return ret.substring(0,ret.length()-1);
		
	}
	
}



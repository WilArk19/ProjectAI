
public class Board {
	
	private int[] board;
	

	private int boardSize;
	
	
	
	
	public Board(){}
	
	/* 
	 * Initialize the board
	 * Size : Number of pits for each player
	 */
	public Board(int size, int numOfStones){
		//Number of pits for each player + 2 mancalas
		this.boardSize = size;
		int lenOfArray = 2*size + 2;
		
		board = new int[lenOfArray];

		for(int i=0; i<lenOfArray-1; i++){
			board[i] = numOfStones;
		}
		
		
		
		//Set the Mancala pits to zero
		board[size] = 0; //Mancala for player 1
		board[lenOfArray-1] = 0; //Mancala for player 2
		
		/*for(int i=0; i<lenOfArray-1; i++){
			System.out.print(board[i]);
		}*/
	}
	
	
	/*
	 * Print the board
	 */
	
	@Override
	public String toString(){
		
		String ret = new String();
		
		/*for (int i : board){
			ret = ret + "" + i + "," ;
		}
		//remove the last comma
		return ret.substring(0,ret.length()-1);
		*/
		
		System.out.print("P1 |  " +  board[getPitSize()] + " | ");
		for(int i =getPitSize()-1; i >=0;i--){
			System.out.print( board[i] + " ");
			
		}
		System.out.print(" | ");
		
		System.out.println();
		System.out.print("P2 |  " + " " + " | ");
		
		for(int i =1; i<=getPitSize();i++){
			System.out.print(board[getPitSize() + i] + " ");
		}
		
		System.out.print(" | ");
		System.out.println(board[getBoardSize() -1]);
		return "";
	}
	
	public int getPitSize() {
		return boardSize;
	}

	public void setPitSize(int size) {
		this.boardSize = size;
	}
	
	public int getBoardSize() {
		return 2*boardSize + 2;
	}
	
	/*
	public void setBoardSize(int size) {
		this.boardSize = size;
	}*/
	

	public int[] getBoard() {
		return board;
	}

	public void setBoard(int[] board) {
		this.board = board;
	}

	
	
	//Display the board
	public void displayBoard(){
		System.out.println("Display the board");
		System.out.println(toString());
		
	}
	
	
	public int getMancala(int playerNum){
		if (playerNum == 1){
			return boardSize;
		} else {
			return (2*boardSize + 1);
			
		}
	}
	
	//get opponents mancala
	public int getOpponentsMancala(int playerNum){
		if (playerNum == 2){
			return boardSize;
		} else {
			return (2*boardSize + 1);
			
		}
	}
	
	
}



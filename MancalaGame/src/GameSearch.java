
public class GameSearch {

	Board mancalaBoard;
	AlgoType searchType;
	boolean isHuman;
	
	/*
	 * By default - Algo : Greedy, Num of pits = 4 with 4 stones each, Computer
	 */
	public GameSearch(){
		mancalaBoard = new Board(4,4);
		searchType = AlgoType.GREEDY;
		isHuman = false;
		
	}
		
	//contain the start of game
	public void playGame(){

	}

	//The logic for greedy algorithm 
	public void greedy(){

	}

	//The logic for minimax algorithm 
	public void minimax(){

	}

	//The logic for alphaBeta
	public void alphaBeta(){

	}
}


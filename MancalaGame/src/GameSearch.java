


public abstract class GameSearch {

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
	
	public GameSearch(Board mancalaBoard, AlgoType algoType , boolean isHuman){
		this.mancalaBoard = mancalaBoard;
		this.searchType = algoType;
		this.isHuman = isHuman;
	}
		
	
	
	public abstract boolean drawnPosition(Position p);
    public abstract boolean wonPosition(Position p, boolean playerIsHuman);
    //public abstract int positionEvaluation(Position p, boolean playerIsHuman);
    public abstract Position [] possibleMoves(Position p, boolean playerIsHuman);
    public abstract Position makeMove(Position p, boolean player, Move move);
    public abstract boolean reachedMaxDepth(Position p, int depth);
    public abstract Move move();
    public abstract boolean isGameOver();
    //public abstract int getScore(boolean playerIsHuman);
}


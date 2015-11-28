import java.util.Scanner;

public class Mancala{

	Board mancalaBoard;
	AlgoType searchType;
	Player player1;
	Player player2;


	Mancala(){
		gameStart();
	}


	//just to commit
	//Preparation of game
	public void gameStart(){

		int algoType = -1;
		Scanner sc = new Scanner(System.in);

		while(algoType != 1 && algoType !=2 && algoType !=3 && algoType != 4) {
			System.out.println("Enter the number for the type of game algorithm");
			System.out.println("1 - Greedy");
			System.out.println("2 - Minimax");
			System.out.println("3 - Alpha Beta");
			System.out.println("4 - Exit");

			algoType = sc.nextInt();
		}

		if (algoType == 4) System.exit(0);

		System.out.println(algoType);

		int firstTurn = -1;
		while (firstTurn !=1 && firstTurn !=2){
			System.out.println("Select the player for the first chance");
			System.out.println("1 - Human");
			System.out.println("2 - Computer");
			System.out.println("3 - Exit");

			firstTurn = sc.nextInt();
		}

		if (firstTurn == 3) {
			System.exit(0);
		} else if (firstTurn == 1){
			player1 = new Player(true,1);
		} else{
			player1 = new Player(false,1);
		}


		int secondTurn = -1;
		while (secondTurn !=1 && secondTurn !=2){
			System.out.println("Select the opponent");
			System.out.println("1 - Human");
			System.out.println("2 - Computer");
			System.out.println("3 - Exit");
			secondTurn = sc.nextInt();
		}

		if (secondTurn == 3) {
			System.exit(0);
		} else if (secondTurn == 1){
			player2 = new Player(true,2);
		} else{
			player2 = new Player(false,2);
		}

		//TODO : Check for size of board
		System.out.println("initialize the board");
		mancalaBoard = new Board(4,3);


		if (algoType == 1)
			searchType = AlgoType.GREEDY;
		else if (algoType == 2)
			searchType = AlgoType.MINIMAX;
		else 
			searchType = AlgoType.ALPHA_BETA;

	}


	//Playing the game
	public void playGame(){

		//display the board
		//mancalaBoard.displayBoard();
		Player currPlayer = player1;

		while (true){

			System.out.println("Playr Num is : " + currPlayer.playerNum);

			Move nextMove;
			if (!currPlayer.isHuman){
				nextMove = getCompMove(mancalaBoard.getPitSize(),searchType);
			} else {
				//System.out.println("get human move");
				nextMove = getHumanMove(mancalaBoard.getPitSize(),currPlayer);

			}

			//Play the move
			boolean gotFreeTurn = makeMove(nextMove,currPlayer);

			if (isGameOver()){
				getAllStones();
				if (isGameDrawn()){
					System.out.println("No winner : Game draw");
				} else{
					int winner = findWinner();
					System.out.println("Game over. Player " + winner + " won.");
					System.out.println("Scores are");
					System.out.println("Player 1: " + getScore(player1));
					System.out.println("Player 2: " + getScore(player2));

				}

				break;
			}

			if (wonPosition(nextMove, currPlayer.isHuman)){
				if (currPlayer.isHuman){
					System.out.println("Human won the Game");
				}else{
					System.out.println("Program won the Game");
				}
				//exit
				System.exit(0);

			}

			//Toggle the turn if no free turn
			if (!gotFreeTurn){
				if (currPlayer.equals (player1)){
					currPlayer = player2;
				}else {
					currPlayer = player1; 
				}

			}

		}

	}






	//Human move generator
	public Move getHumanMove(int size, Player currPlayer){
		mancalaBoard.displayBoard();
		//System.out.println(size);
		int pitNum = -1;
		Move myMove = new Move();
		do {
			System.out.println("Select the pitnumber");
			Scanner sc = new Scanner(System.in);
			pitNum = sc.nextInt();

			myMove = convertIndexToMove(pitNum, currPlayer);

		} while (pitNum<0 || pitNum > size || isIllegalMove(myMove));



		//System.out.println("Return to main");
		return myMove;
	}

	//computer move generator
	public Move getCompMove(int size, AlgoType searchType){
		Move nextMove;
		if (searchType == AlgoType.GREEDY){
			nextMove = getGreedyMove();
		} else if (searchType == AlgoType.MINIMAX){
			nextMove = getMiniMaxMove();
		} else{
			nextMove = getAlphaBetaMove();
		}
		return nextMove;
	}




	//returns true if after making a turn we get a free turn
	public boolean makeMove(Move move, Player currPlayer){

		boolean freeTurn = false;
		int moveIndex = move.getMoveIndex();
		int[] tempBoard = mancalaBoard.getBoard();
		int boardSize = mancalaBoard.getBoardSize();
		//System.out.println("moveIndex is: " +moveIndex);
		int numOfStones = tempBoard[moveIndex];

		tempBoard[moveIndex] = 0;
		moveIndex++;

		while(numOfStones>0){
			//System.out.println("moveIndex is : " +  moveIndex);
			//System.out.println("opo mancala is: " + mancalaBoard.getOpponentsMancala(currPlayer.playerNum));
			if ((moveIndex % boardSize ) == 
					mancalaBoard.getOpponentsMancala(currPlayer.playerNum)){
				moveIndex++;
				continue;
			}
			tempBoard[(moveIndex) % boardSize]++;
			moveIndex++;
			numOfStones--;
		}

		//if just to check if we get a free turn
		int indexToCompare = (moveIndex-1) % boardSize;
		int myMancala = mancalaBoard.getMancala(currPlayer.playerNum);


		if (indexToCompare == myMancala)
		{
			freeTurn = true;
		}else{

			if (tempBoard[indexToCompare] == 1){
				if ((currPlayer.playerNum == 1 && indexToCompare < mancalaBoard.getPitSize())
						|| (currPlayer.playerNum == 2 && indexToCompare > mancalaBoard.getPitSize())){
					//check opponents opp pit - 2*p - index
					int oppPit = (mancalaBoard.getPitSize() * 2) - indexToCompare;
					if (tempBoard[oppPit] > 0){
						tempBoard[myMancala] += tempBoard[oppPit] + tempBoard[indexToCompare];
						tempBoard[oppPit] = 0;
						tempBoard[indexToCompare] = 0;
					}
				}
			}
		}

		mancalaBoard.setBoard(tempBoard);
		//System.out.println("freeturn is :"+ freeTurn);
		return freeTurn;
	}



	//convert the index entered to a move
	public Move convertIndexToMove(int pitNum,Player currPlayer){
		Move move;
		int sizeOfBoard = mancalaBoard.getPitSize();
		if (currPlayer.playerNum == 1){
			//according to player1
			move = new Move(pitNum-1);
		} else{
			//according to player2
			move  = new Move(sizeOfBoard + pitNum);
		}

		return move;
	}



	public  boolean isGameOver(){

		//check for player1
		int[] tempBoard = mancalaBoard.getBoard();
		int pitSize = mancalaBoard.getPitSize();
		int totSize = mancalaBoard.getBoardSize();
		boolean flag = true;

		for(int i=0; i <pitSize;i++)
		{
			if (tempBoard[i] >0){
				flag = false;
				break;
			}
		}


		//ch1eck for player2
		if (!flag){
			flag = true;
			for(int j= pitSize+1; j< totSize-1; j++){
				if (tempBoard[j] >0){
					flag = false;
					break;
				}
			}
		}

		if (!flag)
			return false;
		else 
			return true;

	}

	public boolean isGameDrawn(){
		if (getScore(player1) == getScore(player2))
			return true;
		else 
			return false;
	}


	public void stealStones(){

	}


	private int findWinner() {
		if (getScore(player1) > getScore(player2))
			return 1;
		else 
			return 2;
	}

	public void getAllStones(){
		int[] tempBoard = mancalaBoard.getBoard();
		int pitSize = mancalaBoard.getPitSize();
		int totSize = mancalaBoard.getBoardSize();

		for(int i=0; i <pitSize;i++)
		{
			tempBoard[pitSize] += tempBoard[i];
			tempBoard[i] = 0;
		}


		for(int j= pitSize+1; j< totSize-1; j++){
			tempBoard[totSize-1] += tempBoard[j];
			tempBoard[j] = 0;
		}

		mancalaBoard.setBoard(tempBoard);
	}



	//Generate nextMove according to greedy algorithm
	public Move getGreedyMove(){

		return null;
	}

	//Generate nextMove according to Minimax algorithm
	public Move getMiniMaxMove(){
		return null;
	}

	//Generate nextMove according to aplha beta algorithm
	public Move getAlphaBetaMove(){
		return null;
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


	public boolean wonPosition(Move Move, boolean playerIsHuman){
		//check if play the move is the game won
		return false;
	}

	public int positionEvaluation(Position p, boolean playerIsHuman){

		return -1;
	}

	public Position[] possibleMoves(Position p, boolean playerIsHuman){

		return null;
	}



	public boolean reachedMaxDepth(Position p, int depth){

		return false;
	}




	public int getScore(Player currPlayer){
		return (mancalaBoard.getBoard())[(mancalaBoard.getMancala
				(currPlayer.playerNum))];
	}

	public void printPosition(){

	}


	//check if the move is illegal : if pit contains nothing 
	public boolean isIllegalMove(Move myMove){

		if (mancalaBoard.getBoard()[myMove.getMoveIndex()] == 0){
			return true;
		}
		return false;
	}

	public static void main(String[] args){
		Mancala playObj = new Mancala();
		playObj.playGame();

	}



}



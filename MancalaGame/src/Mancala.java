import java.util.Scanner;

public class Mancala{

	Board mancalaBoard;
	AlgoType searchType;
	Player player1;
	Player player2;


	Mancala(){
		gameStart();
	}


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
		mancalaBoard = new Board(4,4);
		
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
		mancalaBoard.toString();
		Player currPlayer = player1;
		while (true){
			Move nextMove;
			if (!currPlayer.isHuman){
				nextMove = getCompMove(mancalaBoard.getBoardSize(),searchType);
			} else {
				nextMove = getHumanMove(mancalaBoard.getBoardSize(),currPlayer);
				
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
			
			if (drawnPosition(nextMove)){
				System.out.println("Game draw");
				//exit
				System.exit(0);
			}
			
			//if not won or draw make the new move 
			//Toggle the turn if no free turn
			if (!makeMove(nextMove)){
				currPlayer = player2;
			}
			
		}
		
    }



	//Human move generator
	public Move getHumanMove(int size, Player currPlayer){
		mancalaBoard.displayBoard();
		System.out.println(size);
		int pitNum = -1;
		while (pitNum < 0 || pitNum > size){
			System.out.println("Select the pitnumber");
			Scanner sc = new Scanner(System.in);
			pitNum = sc.nextInt();
			}
		
		Move myMove = covertIndexToMove(pitNum, currPlayer);
		
		System.out.println("Return to main");
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
	
	
	//returns true if after making a turn we get a free turn
	public boolean makeMove(Move move){
	
		return false;
	}
	
	
	//convert the index entered to a move
	public Move covertIndexToMove(int pitNum,Player currPlayer){
		Move move;
		int sizeOfBoard = mancalaBoard.getBoardSize();
		if (currPlayer.playerNum == 1){
			//according to player1
			move = new Move(pitNum-1);
		} else{
			//according to player2
			move  = new Move(sizeOfBoard + pitNum);
		}
		
		return move;
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


	public boolean drawnPosition(Move move){
		//check if i play the move is the game drawn
		return false;
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

	public Position makeMove(Position p, boolean playerIsHuman, Move move){

		return null;
	}

	public boolean reachedMaxDepth(Position p, int depth){

		return false;
	}

	public void distributeStones(){


	}

	public  boolean isGameOver(){

		return false;
	}

	public int getScore(int player){
		return 0;
	}

	public void printPosition(){

	}



	public static void main(String[] args){
		Mancala playObj = new Mancala();
		playObj.playGame();

	}



}



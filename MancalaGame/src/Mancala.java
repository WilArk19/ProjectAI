import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * The main class for the game
 * @author Madhuri
 *
 */

public class Mancala{

	Board mancalaBoard;
	AlgoType searchType;
	Player player1;
	Player player2;

	final public int BOARD_SIZE = 3;
	final public int STONES_IN_PIT = 3;
	final public int MAX_DEPTH = 2;

	Mancala(){
		gameStart();
	}


	/**
	 * Preparation of the game
	 */
	public void gameStart(){

		int algoType = -1;
		Scanner sc = new Scanner(System.in);

		while(algoType != 1 && algoType !=2 && algoType !=3 && algoType != 4) {
			System.out.println("Enter the number for the type of game algorithm");
			System.out.println("1 - Greedy");
			System.out.println("2 - Minimax");
			System.out.println("3 - Exit");

			algoType = sc.nextInt();
		}

		//If the user selected 3 - Exit
		if (algoType == 3) System.exit(0);

		// Select the first move
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

		// Select the first move
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

		mancalaBoard = new Board(BOARD_SIZE,STONES_IN_PIT);


		if (algoType == 1)
			searchType = AlgoType.GREEDY;
		else if (algoType == 2)
			searchType = AlgoType.MINIMAX;
		else 
			searchType = AlgoType.ALPHA_BETA;

	}



	/**
	 * Playing the game
	 */
	public void playGame(){

		Player currPlayer = player1;
		int numOfTotalMoveplayer1 = 0;
		int numOfTotalMoveplayer2 = 0;

		while (true){

			//System.out.println();
			/*System.out.println();
			System.out.println("##################################");

			System.out.println("Current Player is player " + currPlayer.playerNum);
			System.out.println();
			System.out.println();
			System.out.println("################################");
			System.out.println("          TURN CHANGED");
			System.out.println("################################"); */

			//Find the nextMove
			Move nextMove;
			if (!currPlayer.isHuman){
				nextMove = getCompMove(mancalaBoard.getPitSize(),searchType,currPlayer);
			} else {
				nextMove = getHumanMove(mancalaBoard.getPitSize(),currPlayer);

			}

			//Play the move
			boolean gotFreeTurn = makeMove(nextMove,currPlayer);

			if (currPlayer.equals(player1)){
				numOfTotalMoveplayer1++;
			}else{
				numOfTotalMoveplayer2++;
			}

			// Check for game over
			if (isGameOver(mancalaBoard.getBoard())){
				mancalaBoard.setBoard(getAllStones(mancalaBoard.getBoard()));
				if (isGameDrawn()){
					System.out.println("No winner : Game draw");
				} else{
					int winner = findWinner();
					System.out.println("Game over. Player " + winner + " won.");
					System.out.println("Scores are");
					System.out.println("Player 1: " + getScore(player1));
					System.out.println("Player 2: " + getScore(player2));

				}

				System.out.println("Player1 took : " + numOfTotalMoveplayer1 + "moves");
				System.out.println("Player2 took : " + numOfTotalMoveplayer2 + "moves");

				break;
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







	/**
	 * Human Move generator
	 * @param size : Size of the pits 
	 * @param currPlayer : current player
	 * @return The move selected by the user
	 */
	public Move getHumanMove(int size, Player currPlayer){
		mancalaBoard.displayBoard();
		int pitNum = -1;
		Move myMove = new Move();
		do {
			System.out.println("Select the pitnumber");
			Scanner sc = new Scanner(System.in);
			pitNum = sc.nextInt();

			myMove = convertIndexToMove(pitNum, currPlayer);

		} while (pitNum<0 || pitNum > size || isIllegalMove(myMove));

		return myMove;
	}

	
	/**
	 * Computer move generator
	 * @param size : Size of the pits
	 * @param searchType
	 * @param currPlayer
	 * @return the move 
	 */
	public Move getCompMove(int size, AlgoType searchType,Player currPlayer){
		Move nextMove;
		if (searchType == AlgoType.GREEDY){
			nextMove = getGreedyMove(currPlayer);
			//System.out.println("done with the move");
		} else {
			nextMove = getMiniMaxMove(currPlayer,MAX_DEPTH);
			//System.out.println("done with the move");
		}
		return nextMove;
	}





	/**
	 * Make the selected move 
	 * @param move : the move the player wants to take
	 * @param currPlayer 
	 * @return true if after making a turn we get a free turn
	 */
	public boolean makeMove(Move move, Player currPlayer){

		boolean freeTurn = false;
		int moveIndex = move.getMoveIndex();
		int[] tempBoard = mancalaBoard.getBoard();
		int boardSize = mancalaBoard.getBoardSize();
		int numOfStones = tempBoard[moveIndex];

		tempBoard[moveIndex] = 0;
		moveIndex++;

		while(numOfStones>0){
			if ((moveIndex % boardSize ) == 
					mancalaBoard.getOpponentsMancala(currPlayer.playerNum)){
				moveIndex++;
				continue;
			}
			tempBoard[(moveIndex) % boardSize]++;
			moveIndex++;
			numOfStones--;
		}

		int indexToCompare = (moveIndex-1) % boardSize;
		int myMancala = mancalaBoard.getMancala(currPlayer.playerNum);

		//Check if you get a free turn
		if (indexToCompare == myMancala)
		{
			freeTurn = true;
			System.out.println("You got a free turn with this move");
		}else{
			//Check if you can steal the stones
			if (tempBoard[indexToCompare] == 1){
				if ((currPlayer.playerNum == 1 && indexToCompare < mancalaBoard.getPitSize())
						|| (currPlayer.playerNum == 2 && indexToCompare > mancalaBoard.getPitSize())){
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
		return freeTurn;
	}




	/**
	 * Convert the index entered to a move
	 * @param pitNum - The pit number
	 * @param currPlayer
	 * @return the move 
	 */
	public Move convertIndexToMove(int pitNum,Player currPlayer){
		Move move;
		int sizeOfBoard = mancalaBoard.getPitSize();
		if (currPlayer.playerNum == 1){
			//Move according to player1
			move = new Move(pitNum-1);
		} else{
			//Move according to player2
			move  = new Move(sizeOfBoard + pitNum);
		}

		return move;
	}


	/**
	 * Check if the game is over or not
	 * @param tempBoard
	 * @return true if game over
	 */

	public  boolean isGameOver(int[] tempBoard){

		//Check for player1
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


		//Check for player2
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

	/**
	 * Check if the game is drawn
	 * @return true if it is 
	 */
	public boolean isGameDrawn(){
		if (getScore(player1) == getScore(player2))
			return true;
		else 
			return false;
	}


	/**
	 * Find the winner
	 * @return player number of the winner
	 */
	private int findWinner() {
		if (getScore(player1) > getScore(player2))
			return 1;
		else 
			return 2;
	}

	/**
	 * Steal all the stones
	 * @param tempBoard
	 * @return the board after stealing
	 */
	public int[] getAllStones(int[] tempBoard){
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

		return tempBoard;
	}



	/**
	 * Generate nextMove according to greedy algorithm
	 * @param currPlayer
	 * @return the move according to the greedy algorithm
	 */
	public Move getGreedyMove(Player currPlayer){

		TreeMap<Integer,ArrayList<Integer>> scoreMoveMap = new TreeMap<>();

		int mancalaIndex = mancalaBoard.getMancala(currPlayer.playerNum);
		int oppMancalaIndex = mancalaBoard.getOpponentsMancala(currPlayer.playerNum);

		int startIndex = 0;
		int endIndex = mancalaBoard.getPitSize();
		if (currPlayer.playerNum == 2){
			startIndex = mancalaBoard.getPitSize()+1;
			endIndex = mancalaBoard.getBoardSize()-1;
		} 

		for (int i = startIndex; i< endIndex;i++){

			Move currMove = new Move(i);
			int[] tempBoard = new int[mancalaBoard.getBoardSize()];
			tempBoard = makeTempMove(currMove, currPlayer);

			int numToInc = tempBoard[mancalaIndex];
			ArrayList<Integer> possibleMoves = new ArrayList<Integer>();

			if (scoreMoveMap.containsKey(numToInc)){
				possibleMoves = (ArrayList<Integer>) scoreMoveMap.get(numToInc);
			}

			possibleMoves.add(i);
			scoreMoveMap.put(numToInc,possibleMoves);

		}

		int highestEvalVal =  scoreMoveMap.lastKey();
		List<Integer> highestMoves = scoreMoveMap.get(highestEvalVal);
		
		Move finalMove = new Move(highestMoves.remove(0));
		while(isIllegalMove(finalMove)){
			finalMove = new Move(highestMoves.remove(0));
		}

		return finalMove;
	}


	/**
	 * Gives the number of mancala stones for a move
	 * @param currMove
	 * @param mancalaIndex
	 * @return the number of stones 
	 */
	public int checkMove(Move currMove, int mancalaIndex){
		int moveIndex = currMove.getMoveIndex();
		int distFromMancala = mancalaIndex - moveIndex;
		System.out.println("mancala is at: " + mancalaIndex);
		System.out.println("dis from mancala: " + distFromMancala);

		int[] tempBoard = mancalaBoard.getBoard();
		int numOfStones = tempBoard[moveIndex];

		System.out.println("num of stones: " + numOfStones + "  for move: " + moveIndex);
		if (numOfStones < distFromMancala){

			return 0;
		} else {
			return (1 + ((numOfStones - distFromMancala )/(2* mancalaBoard.getPitSize()+1)));
		}

	}

	/**
	 * Makes the move temporarily for seeing the moves ahead
	 * @param move
	 * @param currPlayer
	 * @return the board
	 */
	public int[] makeTempMove(Move move, Player currPlayer){

		int moveIndex = move.getMoveIndex();
		mancalaBoard.displayBoard();

		int boardSize = mancalaBoard.getBoardSize();
		int[] tempBoard = new int[boardSize];
		int[] srcBoard = mancalaBoard.getBoard();

		System.arraycopy( srcBoard, 0, tempBoard, 0, boardSize );

		int numOfStones = tempBoard[moveIndex];

		tempBoard[moveIndex] = 0;
		moveIndex++;

		while(numOfStones>0){
			if ((moveIndex % boardSize ) == 
					mancalaBoard.getOpponentsMancala(currPlayer.playerNum)){
				moveIndex++;
				continue;
			}
			tempBoard[(moveIndex) % boardSize]++;
			moveIndex++;
			numOfStones--;
		}

		//Just to check if we get a free turn
		int indexToCompare = (moveIndex-1) % boardSize;
		int myMancala = mancalaBoard.getMancala(currPlayer.playerNum);

		if(!(indexToCompare == myMancala)){
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
		return tempBoard;
	}


	/**
	 * Generate nextMove according to Minimax algorithm
	 * @param currPlayer
	 * @param globaldepth
	 * @return
	 */
	
	public Move getMiniMaxMove(Player currPlayer,int globaldepth){
		//System.out.println("In getMiniMaxMove ");
		MiniMaxNode rootNode = new MiniMaxNode(mancalaBoard.getBoard(),currPlayer.playerNum,0,
				Integer.MIN_VALUE,null, new Move(-1),false);

		MiniMaxTree gameTree = new MiniMaxTree();
		gameTree.insert(rootNode, null);
		gameTree = miniMaxHelperWithGO2(gameTree,rootNode, currPlayer, mancalaBoard.getBoard(), 0,globaldepth, true);
		//System.out.println("Out");
		MiniMaxNode root = gameTree.getRoot();
		MiniMaxNode retNode = evaluateGameTree(root, 0, 2);

		Move retMove = new Move();
		//Selecting from roots child nodes - Maximizing
		for(MiniMaxNode childNode: retNode.getChildrenList()){
			//System.out.println(childNode.moveIndex.getMoveIndex() + " : " + childNode.getEvalVal());
			if (retNode.getEvalVal() < childNode.getEvalVal()){
				retNode.setEvalVal(childNode.getEvalVal());
				retMove = childNode.getMoveIndex();
			}	
		}

		//sort and get the minimum value of moveindex
		//System.out.println("the next move is with moveindex : "+  retMove.getMoveIndex());
		return (retMove);

	}

		
	
	/**
	 *  Helper function to generate the minimax game tree
	 * @param gameTree : The game tree
	 * @param rootNode : The root of the game tree
	 * @param currPlayer : The Current player
	 * @param tempBoard : The temporary board
	 * @param tempDepth : The temporary depth in the level of recursion
	 * @param globalDepth : The minimax search tree depth
	 * @param checkParentsFt : Check if parent got a free turn
	 * @return The game tree
	 */
	

	public MiniMaxTree miniMaxHelper(MiniMaxTree gameTree,MiniMaxNode rootNode, 
			Player currPlayer, int[] tempBoard, int tempDepth, int globalDepth, boolean checkParentsFt){

		System.out.println();

		//CHECK THE DEPTH
		if (tempDepth >= globalDepth && !checkParentsFt ){
			return gameTree;
		}

		MiniMaxNode currParent = rootNode;

		//Check the player
		if (!checkParentsFt){
			if (currPlayer.equals (player1)){
				currPlayer  = player2;
			}else {
				currPlayer  = player1;
			}

		}

		int startIndex = 0;
		int endIndex = mancalaBoard.getPitSize();
		if (currPlayer.playerNum == 2){
			startIndex = mancalaBoard.getPitSize()+1;
			endIndex = mancalaBoard.getBoardSize()-1;
		} 

		int i = startIndex;
		for (i = startIndex ;i <endIndex; i++){

			Move currMove = new Move(i);
			if (isIllegalMove(currMove,tempBoard)){
				continue;
			}else{
				BoardFTCheck bft = makeTempMiniMaxMove(currMove, currPlayer, tempBoard);

				int mydepth = currParent.depth;
				if (mydepth == 0){
					mydepth++;
				}

				if (!checkParentsFt){
					mydepth++;
				}

				int evalFunc;
				//Odds are minimizers
				if (mydepth % 2 == 1){
					evalFunc = Integer.MAX_VALUE;
				}else{
					evalFunc = Integer.MIN_VALUE;
				}

				//if we reached the leaf find the evaluation right away
				if (mydepth == globalDepth || bft.leadsToGameOver){
					displayHelper(bft.tempBoard);

					//If I dont get a free turn at last level then only evaluate
					if (!bft.freeTurn){
						evalFunc = getEvalFunc(currPlayer, bft.tempBoard);
					} else {
						//last level is already toggled
						if (evalFunc == Integer.MAX_VALUE){
							evalFunc = Integer.MIN_VALUE;
						} else {
							evalFunc = Integer.MAX_VALUE;
						}
					}
				}

				MiniMaxNode newNode = new MiniMaxNode(bft.tempBoard,currPlayer.playerNum,
						mydepth,evalFunc,currParent,currMove,
						bft.freeTurn);


				
				if(bft.freeTurn){
					miniMaxHelper(gameTree,newNode,currPlayer, bft.tempBoard, mydepth, globalDepth,true);
				} else{
					miniMaxHelper(gameTree,newNode,currPlayer, bft.tempBoard, mydepth, globalDepth,false);

				}


			}


		}
		return gameTree;

	}



	
	
	public MiniMaxTree miniMaxHelperWithGO2(MiniMaxTree gameTree,MiniMaxNode rootNode, 
			Player currPlayer, int[] tempBoard, int tempDepth, int globalDepth, boolean checkParentsFt){

		System.out.println();
		//System.out.println("In miniMaxHelperWithGO2 ");
		//TODO : CHECK THE DEPTH
		if (tempDepth >= globalDepth && !checkParentsFt ){
			return gameTree;
		}


		MiniMaxNode currParent = rootNode;


		if (!checkParentsFt){
			if (currPlayer.equals (player1)){
				currPlayer  = player2;
			}else {
				currPlayer  = player1;
			}

		}


		int startIndex = 0;
		int endIndex = mancalaBoard.getPitSize();
		if (currPlayer.playerNum == 2){
			startIndex = mancalaBoard.getPitSize()+1;
			endIndex = mancalaBoard.getBoardSize()-1;
		} 

		//boolean checkParentsFt = false;
		int i = startIndex;
		for (i = startIndex ;i <endIndex; i++){

			Move currMove = new Move(i);
			if (isIllegalMove(currMove,tempBoard)){
				continue;
			}else{
				BoardFTCheck bft = makeTempMiniMaxMoveWithGO2(currMove, currPlayer, tempBoard);
				
				int mydepth = currParent.depth;
				if (mydepth == 0){
					mydepth++;
				}

				if (!checkParentsFt){
					mydepth++;
				}

				int evalFunc;
				//Odds are minimizers
				if (mydepth % 2 == 1){
					evalFunc = Integer.MAX_VALUE;
				}else{
					evalFunc = Integer.MIN_VALUE;
				}

				//if we reached the leaf find the evaluation right away
				if (mydepth == globalDepth || bft.leadsToGameOver){
					displayHelper(bft.tempBoard);

					//If I dont get a free turn at last level then only evaluate
					if (!bft.freeTurn){
						evalFunc = getEvalFunc(currPlayer, bft.tempBoard);
					} else {
						//last level is already toggled
						if (evalFunc == Integer.MAX_VALUE){
							evalFunc = Integer.MIN_VALUE;
						} else {
							evalFunc = Integer.MAX_VALUE;
						}
					}
				}

				MiniMaxNode newNode = new MiniMaxNode(bft.tempBoard,currPlayer.playerNum,
						mydepth,evalFunc,currParent,currMove,
						bft.freeTurn);


				//displayHelper(bft.tempBoard);
				/*System.out.println("Node to insert is ");
				displayHelper(newNode.nodeBoard);
				System.out.println("£££££££££££££££££££££££££££");
				System.out.println("In insert tree");
				System.out.println("£££££££££££££££££££££££££££");*/
				
				gameTree.insert(newNode, currParent);
				
				//gameTree.displayTree();

				/*System.out.println("return from insert tree");
				System.out.println();*/

				if(bft.freeTurn){
					miniMaxHelperWithGO2(gameTree,newNode,currPlayer, bft.tempBoard, mydepth, globalDepth,true);
				} else{
					miniMaxHelperWithGO2(gameTree,newNode,currPlayer, bft.tempBoard, mydepth, globalDepth,false);

				}


			}


		}
		return gameTree;

	}



	public BoardFTCheck makeTempMiniMaxMoveWithGO2(Move move, Player currPlayer, int[] srcBoard){

		//System.out.println("in makeTempMiniMaxMoveWithGO2");
		int moveIndex = move.getMoveIndex();

		int boardSize = mancalaBoard.getBoardSize();
		int[] tempBoard = new int[boardSize];

		System.arraycopy( srcBoard, 0, tempBoard, 0, boardSize );
		int numOfStones = tempBoard[moveIndex];

		tempBoard[moveIndex] = 0;
		moveIndex++;

		while(numOfStones>0){
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
		//System.out.println("indexTocomp : " +  indexToCompare);
		int myMancala = mancalaBoard.getMancala(currPlayer.playerNum);

		boolean freeTurn = false;

		//if just to check if we get a free turn
		if (indexToCompare == myMancala)
		{
			freeTurn = true;
			System.out.println("Yayyyyii , I got a free turn");
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
		//System.out.println("tempBoard in make temp move: " + tempBoard.toString());



		//if the game is over we need to evaluate the function
		boolean gameOver = false;
		if (isGameOver(tempBoard)){

			tempBoard = getAllStones(tempBoard);
			gameOver = true;
		}


		//System.out.println("Over");
		BoardFTCheck bft = new BoardFTCheck(tempBoard,freeTurn,gameOver);
		return bft;
	}










	/**
	 * Helper to display the board
	 * @param board
	 */
	public void displayHelper(int[] board){
		int pitSize = (board.length - 2)/2;
		System.out.print("P1 |  " +  board[pitSize] + " | ");
		for(int i =pitSize-1; i >=0;i--){
			System.out.print( board[i] + " ");

		}
		System.out.print(" | ");

		System.out.println();
		System.out.print("P2 |  " + " " + " | ");

		for(int i =1; i<= pitSize;i++){
			System.out.print(board[pitSize + i] + " ");
		}

		System.out.print(" | ");
		System.out.println(board[board.length -1]);

	}




	public BoardFTCheck makeTempMiniMaxMove(Move move, Player currPlayer, int[] srcBoard){


		int moveIndex = move.getMoveIndex();

		int boardSize = mancalaBoard.getBoardSize();
		int[] tempBoard = new int[boardSize];

		System.arraycopy( srcBoard, 0, tempBoard, 0, boardSize );
		int numOfStones = tempBoard[moveIndex];

		tempBoard[moveIndex] = 0;
		moveIndex++;

		while(numOfStones>0){
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
		//System.out.println("indexTocomp : " +  indexToCompare);
		int myMancala = mancalaBoard.getMancala(currPlayer.playerNum);

		boolean freeTurn = false;

		//if just to check if we get a free turn
		if (indexToCompare == myMancala)
		{
			freeTurn = true;
			System.out.println("Yayyyyii , I got a free turn");
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
		//System.out.println("tempBoard in make temp move: " + tempBoard.toString());



		//if the game is over we need to evaluate the function
		boolean gameOver = false;
		if (isGameOver(tempBoard)){

			tempBoard = getAllStones(tempBoard);
			gameOver = true;
		}


		System.out.println();
		BoardFTCheck bft = new BoardFTCheck(tempBoard,freeTurn,gameOver);
		return bft;
	}




	/**
	 * evaluate all tree and select the correct move
	 * @param rootNode
	 * @param height
	 * @param globalDepth
	 * @return The root node
	 */
	public MiniMaxNode evaluateGameTree(MiniMaxNode rootNode, int height, int globalDepth)
	{

		//if (height == globalDepth)
		//{
		if ((height == globalDepth) && globalDepth % 2 == 0 && (rootNode.childrenList.isEmpty() 
				|| rootNode.childrenList == null)){
			//toggle the sign if the level is even
			//System.out.println();
			//displayHelper(rootNode.nodeBoard);
			
			//System.out.println(" This is a leaf node");
			
			rootNode.setEvalVal(rootNode.getEvalVal() * -1);
			//displayHelper(rootNode.nodeBoard);
			return rootNode; 
		}
		//System.out.println("returning from leaf with this root board");

		//System.out.println("eval func for leaf node is : " + rootNode.getEvalVal());
		//System.out.println("returned now");

		//}
		else {
			//displayHelper(rootNode.nodeBoard);
			//System.out.println("list size for child" + rootNode.getChildrenList().size());

			ArrayList<MiniMaxNode> childNodes = rootNode.getChildrenList();

			//we have not reached the depth but its still a leaf
			if (childNodes == null || childNodes.isEmpty()){
				if (rootNode.getDepth() % 2 == 0){
					//toggle the sign if the level is even
					rootNode.setEvalVal(rootNode.getEvalVal() * -1);
				} 

				return rootNode; 
			}

			for (MiniMaxNode node : childNodes){
				//int count =1;
				//System.out.println("going in for loop :");
				MiniMaxNode retMove = evaluateGameTree(node, node.getDepth(), globalDepth);

				if ((node.getDepth() % 2 == 0) 
						|| ( (node.getDepth() % 2 == 1) && (!node.getChildrenList().isEmpty()) ) ){
					//even level has to be minimizer
					//System.out.println(" In the Minimizer Code now");
					//System.out.println("returned board is ");
					//displayHelper(retMove.nodeBoard);
					//System.out.println("RetMove eval: " + retMove.getEvalVal());
					//System.out.println();

					//System.out.println("The root node here is ");
					//displayHelper(rootNode.getNodeBoard());

					//System.out.println("rootNode eval befor: " + rootNode.getEvalVal() );
					//System.out.println();
					if (retMove.getEvalVal() < rootNode.getEvalVal()){
						rootNode.setEvalVal(retMove.getEvalVal());
						//System.out.println("i am in the if condition of minimizer");
						//System.out.println("rootNode eval after if " + rootNode.getEvalVal() );

					}
				} else if((node.getDepth() % 2 == 0) 
						|| ( (node.getDepth() % 2 == 1) && (!node.getChildrenList().isEmpty()) ) ){
					//System.out.println(" In the Maximizer Code now");
					//System.out.println("returned board is ");
					//displayHelper(retMove.nodeBoard);
					//System.out.println("RetMove eval: " + retMove.getEvalVal());
					//System.out.println();

					//System.out.println("The root node here is ");
					//displayHelper(rootNode.getNodeBoard());

					//System.out.println("rootNode eval: " + rootNode.getEvalVal() );
					//System.out.println();
					//odd level has to be a maximizer
					if(retMove.getEvalVal() > rootNode.getEvalVal()){
						//System.out.println("i am in condition of minimizer");
						rootNode.setEvalVal(retMove.getEvalVal());
						//System.out.println("rootNode eval after if " + rootNode.getEvalVal() );
					}
				}
			}
		}

		//System.out.println("###############RETURNING #########################");
		return rootNode;

	}














	/**
	 * Find the score for a player
	 * @param currPlayer
	 * @return the score
	 */

	public int getScore(Player currPlayer){
		return (mancalaBoard.getBoard())[(mancalaBoard.getMancala
				(currPlayer.playerNum))];
	}

	/**
	 * Evaluates the scores for the board position
	 * @param currPlayer
	 * @param tempBoard
	 * @return scores
	 */
	public int getEvalFunc(Player currPlayer, int[] tempBoard){
		int mancalaIndex = mancalaBoard.getMancala(currPlayer.playerNum);
		//System.out.println("i am player " + currPlayer.playerNum + "  & my mancala is "  + mancalaIndex );

		int oppMancalaIndex = mancalaBoard.getOpponentsMancala(currPlayer.playerNum);
		//System.out.println("i am opponent & my mancala is "  + oppMancalaIndex );

		//System.out.println("value in board for the indices are : " + tempBoard[mancalaIndex] + " & " + tempBoard[oppMancalaIndex] );
		return (tempBoard[mancalaIndex] - tempBoard[oppMancalaIndex]);
	}

	

	/**
	 * Check if the move is illegal : if pit contains nothing
	 * @param myMove
	 * @return true for an illegal move
	 */
	public boolean isIllegalMove(Move myMove){

		if (mancalaBoard.getBoard()[myMove.getMoveIndex()] == 0){
			System.out.println("The move you selected is not allowed. Select again.");
			return true;
		}
		return false;
	}

	/**
	 * Check if the move is illegal : if pit contains nothing
	 * @param myMove
	 * @param tempBoard
	 * @return true for an illegal move 
	 */
	public boolean isIllegalMove(Move myMove, int[] tempBoard){

		if (tempBoard[myMove.getMoveIndex()] == 0){
			return true;
		}
		return false;
	}
	
	/**
	 * Game starts here
	 * @param args
	 */

	public static void main(String[] args){
		Mancala playObj = new Mancala();
		playObj.playGame();

	}



}



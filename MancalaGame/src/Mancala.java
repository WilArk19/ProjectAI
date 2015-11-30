import java.awt.DisplayMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.text.GlyphView;

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
		mancalaBoard = new Board(3,3);


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

			//System.out.println();
			System.out.println();
			System.out.println("##################################");

			System.out.println("Playr Num is : " + currPlayer.playerNum);
			System.out.println();
			System.out.println();
			System.out.println("################################");
			System.out.println("          TURN CHANGED");
			System.out.println("################################");

			Move nextMove;
			if (!currPlayer.isHuman){
				//System.out.println("Progs turn");
				nextMove = getCompMove(mancalaBoard.getPitSize(),searchType,currPlayer);
				//System.out.println("Move selected is " + nextMove.getMoveIndex());
			} else {
				//System.out.println("get human move");
				nextMove = getHumanMove(mancalaBoard.getPitSize(),currPlayer);

			}

			//Play the move
			boolean gotFreeTurn = makeMove(nextMove,currPlayer);

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

			//###########TODO : Remove the break statement

			//Play the move only if you are human

			/*if(currPlayer.isHuman){
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

				//Toggle the turn if no free turn
				if (!gotFreeTurn){
					if (currPlayer.equals (player1)){
						currPlayer = player2;
					}else {
						currPlayer = player1; 
					}

				}
			} else{
				// if comp move just break it


				break;
			}*/
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
	public Move getCompMove(int size, AlgoType searchType,Player currPlayer){
		Move nextMove;
		if (searchType == AlgoType.GREEDY){
			//System.out.println("its a greedy search");
			nextMove = getGreedyMove(currPlayer);
			System.out.println("the next move selected by greedy is " + nextMove.getMoveIndex());

		} else if (searchType == AlgoType.MINIMAX){
			//System.out.println("MiniMax");
			nextMove = getMiniMaxMove(currPlayer,2);

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



	public  boolean isGameOver(int[] tempBoard){

		//check for player1
		//int[] tempBoard = mancalaBoard.getBoard();
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



	private int findWinner() {
		if (getScore(player1) > getScore(player2))
			return 1;
		else 
			return 2;
	}

	public int[] getAllStones(int[] tempBoard){
		//int[] tempBoard = mancalaBoard.getBoard();
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

		//mancalaBoard.setBoard(tempBoard);
		return tempBoard;
	}



	//Generate nextMove according to greedy algorithm
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

			//System.out.println("tempBoard:" + tempBoard.toString());

			//System.out.println("mancala Index is : "+mancalaIndex);
			int numToInc = tempBoard[mancalaIndex];
			System.out.println("#######The move : " + i + " increases by: " + numToInc);
			ArrayList<Integer> possibleMoves = new ArrayList<Integer>();

			if (scoreMoveMap.containsKey(numToInc)){
				possibleMoves = (ArrayList<Integer>) scoreMoveMap.get(numToInc);
			}

			possibleMoves.add(i);
			//System.out.println("HashMap.adds " + i + " " + possibleMoves.toString());
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


	//gives the number of mancala stones for a move
	public int checkMove(Move currMove, int mancalaIndex){

		//int mancalaIndex = mancalaBoard.getMancala(currPlayer.playerNum);
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

	public int[] makeTempMove(Move move, Player currPlayer){

		int moveIndex = move.getMoveIndex();
		//System.out.println("for move" +  moveIndex) ;
		//System.out.println("before anyMove");
		mancalaBoard.displayBoard();

		int boardSize = mancalaBoard.getBoardSize();
		int[] tempBoard = new int[boardSize];
		int[] srcBoard = mancalaBoard.getBoard();

		System.arraycopy( srcBoard, 0, tempBoard, 0, boardSize );


		//int boardSize = mancalaBoard.getBoardSize();
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
		//System.out.println("tempBoard in make temp move: " + tempBoard.toString());

		return tempBoard;
	}



	//Generate nextMove according to Minimax algorithm
	public Move getMiniMaxMove(Player currPlayer,int globaldepth){

		MiniMaxNode rootNode = new MiniMaxNode(mancalaBoard.getBoard(),currPlayer.playerNum,0,
				Integer.MIN_VALUE,null, new Move(-1),false);

		MiniMaxTree gameTree = new MiniMaxTree();
		gameTree.insert(rootNode, null);


		//miniMaxHelper(gameTree,rootNode, currPlayer, mancalaBoard.getBoard(), 0 ,globaldepth);
		//miniMaxHelper(gameTree,rootNode, currPlayer, mancalaBoard.getBoard(), 0,globaldepth, true);
		//miniMaxHelperWithGO(gameTree,rootNode, currPlayer, mancalaBoard.getBoard(), 0,globaldepth, true);
		gameTree = miniMaxHelperWithGO2(gameTree,rootNode, currPlayer, mancalaBoard.getBoard(), 0,globaldepth, true);



		/*just to check if tree is changed 
		ArrayList<MiniMaxNode> np = gameTree.getRoot().childrenList;
		System.out.println("Print the root node");
		for(MiniMaxNode n: np){
			System.out.println(n.moveIndex.getMoveIndex());
		}


		Collections.sort(np, Collections.reverseOrder());
		for(MiniMaxNode n: np){
			System.out.println(n.moveIndex.getMoveIndex());
		}*/


		System.out.println("finally, done with tree building");
		System.out.println("madhuri");

		MiniMaxNode root = gameTree.getRoot();
		System.out.println("root for the board before evaluation is" );
		displayHelper(root.nodeBoard);

		//MiniMaxNode retNode = evaluateGameTree(root, 0, 2);
		//MiniMaxNode retNode = evaluateGameTreeWithGO(root, 0, 2);
		System.out.println("DISPLAYING MY TREE");
		gameTree.displayTree();

		System.out.println("STARTING THE EVALUATION");
		MiniMaxNode retNode = evaluateGameTreeWithGO2(root, 0, 2);


		System.out.println("returned from eval madhuri");
		//System.out.println("return Node eval func is: " + retNode.getEvalVal());

		Move retMove = new Move();
		//Selecting from roots child nodes - Maximizing
		for(MiniMaxNode childNode: retNode.getChildrenList()){
			System.out.println(childNode.moveIndex.getMoveIndex() + " : " + childNode.getEvalVal());
			if (retNode.getEvalVal() < childNode.getEvalVal()){
				retNode.setEvalVal(childNode.getEvalVal());
				retMove = childNode.getMoveIndex();
			}	
		}

		//sort and get the minimum value of moveindex
		//Collections.sort(retMoves);
		System.out.println();
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		gameTree.displayTree();;
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

		System.out.println("the next move is with moveindex : "+  retMove.getMoveIndex());
		return (retMove);


		//#######################TODO#############################
		//return new Move(1);
	}

	public void miniMaxHelper(MiniMaxTree gameTree,MiniMaxNode rootNode, Player currPlayer, int[] tempBoard, int tempDepth, int globalDepth){
		System.out.println();
		System.out.println();
		//TODO : CHECK THE DEPTH
		if (tempDepth >= 2){
			System.out.println("GlobalDepth: " + globalDepth);
			System.out.println("reached end of depth so return \n");
			//if (tempDepth == globalDepth){
			return;
		}


		MiniMaxNode currParent = rootNode;
		int startIndex = 0;
		int endIndex = mancalaBoard.getPitSize();
		if (currPlayer.playerNum == 2){
			startIndex = mancalaBoard.getPitSize()+1;
			endIndex = mancalaBoard.getBoardSize()-1;
		} 


		boolean checkParentsFt = false;
		int i = startIndex;
		for (i = startIndex ;i <endIndex; i++){

			Move currMove = new Move(i);
			if (isIllegalMove(currMove,tempBoard)){
				continue;
			}else{
				BoardFTCheck bft = makeTempMiniMaxMove(currMove, currPlayer, tempBoard);
				System.out.println("isparentGotaFreeTurn: " + currParent.isparentGotaFreeTurn());
				System.out.println("checkParentsFt : " + checkParentsFt);

				if (!currParent.isparentGotaFreeTurn() && !checkParentsFt){
					System.out.println("in changing depth");
					//if (currParent.depth > 0){
					checkParentsFt = true;
					System.out.println("checkparent ft changed to true once");

					//}

					tempDepth++;

				}

				int evalFunc;
				//Odds are minimizers
				if (tempDepth%2 == 1){
					evalFunc = Integer.MAX_VALUE;
				}else{
					evalFunc = Integer.MIN_VALUE;
				}

				//if we reached the leaf find the evaluation right away
				if (tempDepth == globalDepth){
					evalFunc = getEvalFunc(currPlayer, bft.tempBoard);
				}

				MiniMaxNode newNode = new MiniMaxNode(bft.tempBoard,currPlayer.playerNum,
						tempDepth,evalFunc,currParent,currMove,
						bft.freeTurn);

				System.out.println();
				displayHelper(bft.tempBoard);
				System.out.println("isFreeTurn: "+ bft.freeTurn);
				System.out.println("parent :" + currParent.moveIndex.getMoveIndex());
				System.out.println("depth: " + tempDepth);
				System.out.println("new Node Index: " + newNode.moveIndex.getMoveIndex() );

				System.out.println("EvalFUnc is :" + evalFunc);
				System.out.println("CurrPlayer is :" + currPlayer.playerNum );
				System.out.println("Done with this node");
				System.out.println("#################");


				gameTree.insert(newNode, currParent);


				if(bft.freeTurn){
					System.out.println("befor rcall CurrPlayer is :" + currPlayer.playerNum );
					miniMaxHelper(gameTree,newNode,currPlayer, bft.tempBoard, tempDepth, globalDepth);
				} else{

					Player tempPlayer	= new Player();
					//Toggle the currPlayer 
					System.out.println("Parent Depth : " + currParent.depth);
					System.out.println("my depth : " + newNode.depth);
					if (!checkParentsFt){
						System.out.println("in check point");


						if (currPlayer.equals (player1)){
							System.out.println("Toggle the player now from 1 to 2");
							//currPlayer = player2;
							tempPlayer  = player2;
						}else {
							System.out.println("Toggle the player now from 2 to 1");
							//currPlayer = player1; 
							tempPlayer  = player1;
						}

					}

					System.out.println("before re call CurrPlayer is :" + currPlayer.playerNum );
					//miniMaxHelper(gameTree,newNode,currPlayer, bft.tempBoard, tempDepth, globalDepth);
					miniMaxHelper(gameTree,newNode,tempPlayer, bft.tempBoard, tempDepth, globalDepth);

				}


			}


		}



		if(i == endIndex){
			System.out.println("the last index");
			if (currPlayer.equals (player1)){
				System.out.println("Toggle the player now from 1 to 2");
				currPlayer = player2;
			}else {
				System.out.println("Toggle the player now from 2 to 1");
				currPlayer = player1; 
			}
		}

		System.out.println("player num before backing up: " + currPlayer.playerNum);
		System.out.println("Returning from recursion from depth : " + tempDepth);
		System.out.println();



	}




	public void miniMaxHelper(MiniMaxTree gameTree,MiniMaxNode rootNode, Player currPlayer, int[] tempBoard, int tempDepth, int globalDepth, boolean checkParentsFt){
		System.out.println();
		System.out.println();

		//TODO : CHECK THE DEPTH
		if (tempDepth >= globalDepth){
			System.out.println("GlobalDepth: " + globalDepth);
			System.out.println("reached end of depth so return \n");
			//if (tempDepth == globalDepth){
			return;
		}


		MiniMaxNode currParent = rootNode;

		//Player tempPlayer	= new Player();
		System.out.println();
		if (!checkParentsFt){
			System.out.println("Toggle the player because parent got a free turn");
			if (currPlayer.equals (player1)){
				System.out.println("Toggle the player now from 1 to 2");
				//currPlayer = player2;
				currPlayer  = player2;
			}else {
				System.out.println("Toggle the player now from 2 to 1");
				//currPlayer = player1; 
				currPlayer  = player1;
			}

		} else{
			System.out.println("do not change the player");
		}


		System.out.println();
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
				BoardFTCheck bft = makeTempMiniMaxMove(currMove, currPlayer, tempBoard);
				//System.out.println("isparentGotaFreeTurn: " + currParent.isparentGotaFreeTurn());
				System.out.println("Did parent get a free turn : " + checkParentsFt);

				/*if (!currParent.isparentGotaFreeTurn()
						&& !checkParentsFt){
					System.out.println("in changing depth");
					//if (currParent.depth > 0){
						checkParentsFt = true;
						System.out.println("checkparent ft changed to true once");

					//}*/
				System.out.println();
				System.out.println("parents depth " + currParent.depth);
				int mydepth = currParent.depth;
				if (mydepth == 0){
					System.out.println("because root increase it");
					mydepth++;
				}

				if (!checkParentsFt){
					System.out.println("parent dint have a free turn then only increase my depth");
					mydepth++;
				}
				System.out.println();
				//}

				int evalFunc;
				//Odds are minimizers
				if (mydepth % 2 == 1){
					evalFunc = Integer.MAX_VALUE;
				}else{
					evalFunc = Integer.MIN_VALUE;
				}

				//if we reached the leaf find the evaluation right away
				if (mydepth == globalDepth){
					//System.out.println("my depth is two and board now is");
					displayHelper(bft.tempBoard);
					evalFunc = getEvalFunc(currPlayer, bft.tempBoard);
					//System.out.println("and the eval for board is: " +evalFunc);
				}

				MiniMaxNode newNode = new MiniMaxNode(bft.tempBoard,currPlayer.playerNum,
						mydepth,evalFunc,currParent,currMove,
						bft.freeTurn);

				System.out.println();
				displayHelper(bft.tempBoard);
				System.out.println("isFreeTurn: "+ bft.freeTurn);
				System.out.println("parent is at index:" + currParent.moveIndex.getMoveIndex());
				System.out.println("depth: " + mydepth);
				System.out.println("new Node Index: " + newNode.moveIndex.getMoveIndex() );

				System.out.println("EvalFUnc is :" + evalFunc);
				System.out.println("CurrPlayer is :" + currPlayer.playerNum );
				System.out.println("Done with this node");
				System.out.println("#################");


				gameTree.insert(newNode, currParent);


				if(bft.freeTurn){
					//System.out.println("befor rcall CurrPlayer is :" + currPlayer.playerNum );
					System.out.println("got a free turn now");
					//checkParentsFt = true;
					miniMaxHelper(gameTree,newNode,currPlayer, bft.tempBoard, mydepth, globalDepth,true);
				} else{


					//Toggle the currPlayer 
					//System.out.println("Parent Depth : " + currParent.depth);
					//checkParentsFt = false;
					System.out.println("no free turn");


					//System.out.println("before re call CurrPlayer is :" + currPlayer.playerNum );
					miniMaxHelper(gameTree,newNode,currPlayer, bft.tempBoard, mydepth, globalDepth,false);
					//miniMaxHelper(gameTree,newNode,tempPlayer, bft.tempBoard, tempDepth, globalDepth,checkParentsFt);

				}


			}


		}

		//System.out.println("player num before backing up: " + currPlayer.playerNum);
		System.out.println("Returning from recursion from depth : " + tempDepth);
		System.out.println();



	}


	public void miniMaxHelperWithGO(MiniMaxTree gameTree,MiniMaxNode rootNode, Player currPlayer, int[] tempBoard, int tempDepth, int globalDepth, boolean checkParentsFt){
		//System.out.println();
		System.out.println();

		//TODO : CHECK THE DEPTH
		if (tempDepth >= globalDepth && !checkParentsFt ){
			//System.out.println("GlobalDepth: " + globalDepth);
			//System.out.println("reached end of depth so return \n");
			//if (tempDepth == globalDepth){
			return;
		}


		MiniMaxNode currParent = rootNode;

		//Player tempPlayer	= new Player();
		//System.out.println();
		if (!checkParentsFt){
			//System.out.println("Toggle the player because parent got a free turn");
			if (currPlayer.equals (player1)){
				//System.out.println("Toggle the player now from 1 to 2");
				//currPlayer = player2;
				currPlayer  = player2;
			}else {
				//System.out.println("Toggle the player now from 2 to 1");
				//currPlayer = player1; 
				currPlayer  = player1;
			}

		} else{
			//System.out.println("do not change the player");
		}


		//System.out.println();
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
				BoardFTCheck bft = makeTempMiniMaxMoveWithGO(currMove, currPlayer, tempBoard);
				//System.out.println("isparentGotaFreeTurn: " + currParent.isparentGotaFreeTurn());
				//System.out.println("Did parent get a free turn : " + checkParentsFt);

				/*if (!currParent.isparentGotaFreeTurn()
						&& !checkParentsFt){
					System.out.println("in changing depth");
					//if (currParent.depth > 0){
						checkParentsFt = true;
						System.out.println("checkparent ft changed to true once");

					//}*/
				//System.out.println();
				//System.out.println("parents depth " + currParent.depth);
				int mydepth = currParent.depth;
				if (mydepth == 0){
					//System.out.println("because root increase it");
					mydepth++;
				}

				if (!checkParentsFt){
					//System.out.println("parent dint have a free turn then only increase my depth");
					mydepth++;
				}
				//System.out.println();
				//}

				int evalFunc;
				//Odds are minimizers
				if (mydepth % 2 == 1){
					evalFunc = Integer.MAX_VALUE;
				}else{
					evalFunc = Integer.MIN_VALUE;
				}

				//if we reached the leaf find the evaluation right away
				if (mydepth == globalDepth || bft.leadsToGameOver){
					//System.out.println("my depth is two and board now is");
					displayHelper(bft.tempBoard);
					evalFunc = getEvalFunc(currPlayer, bft.tempBoard);
					//System.out.println("and the eval for board is: " +evalFunc);
				}

				MiniMaxNode newNode = new MiniMaxNode(bft.tempBoard,currPlayer.playerNum,
						mydepth,evalFunc,currParent,currMove,
						bft.freeTurn);

				//System.out.println();
				displayHelper(bft.tempBoard);
				//System.out.println("isFreeTurn: "+ bft.freeTurn);
				//System.out.println("parent is at index:" + currParent.moveIndex.getMoveIndex());
				//System.out.println("depth: " + mydepth);
				//System.out.println("new Node Index: " + newNode.moveIndex.getMoveIndex() );

				//System.out.println("EvalFUnc is :" + evalFunc);
				//System.out.println("CurrPlayer is :" + currPlayer.playerNum );
				//System.out.println("Done with this node");
				System.out.println("#################");


				gameTree.insert(newNode, currParent);


				if(bft.freeTurn){
					//System.out.println("befor rcall CurrPlayer is :" + currPlayer.playerNum );
					//System.out.println("got a free turn now");
					//checkParentsFt = true;
					miniMaxHelperWithGO(gameTree,newNode,currPlayer, bft.tempBoard, mydepth, globalDepth,true);
				} else{


					//Toggle the currPlayer 
					//System.out.println("Parent Depth : " + currParent.depth);
					//checkParentsFt = false;
					//System.out.println("no free turn");


					//System.out.println("before re call CurrPlayer is :" + currPlayer.playerNum );
					miniMaxHelperWithGO(gameTree,newNode,currPlayer, bft.tempBoard, mydepth, globalDepth,false);
					//miniMaxHelper(gameTree,newNode,tempPlayer, bft.tempBoard, tempDepth, globalDepth,checkParentsFt);

				}


			}


		}

		//System.out.println("player num before backing up: " + currPlayer.playerNum);
		//System.out.println("Returning from recursion from depth : " + tempDepth);
		//System.out.println();



	}




	public MiniMaxTree miniMaxHelperWithGO2(MiniMaxTree gameTree,MiniMaxNode rootNode, 
			Player currPlayer, int[] tempBoard, int tempDepth, int globalDepth, boolean checkParentsFt){

		System.out.println();

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
				System.out.println("Node to insert is ");
				displayHelper(newNode.nodeBoard);
				System.out.println("���������������������������");
				System.out.println("In insert tree");
				System.out.println("���������������������������");
				gameTree.insert(newNode, currParent);
				//gameTree.displayTree();

				System.out.println("return from insert tree");
				System.out.println();

				if(bft.freeTurn){
					miniMaxHelperWithGO2(gameTree,newNode,currPlayer, bft.tempBoard, mydepth, globalDepth,true);
				} else{
					miniMaxHelperWithGO2(gameTree,newNode,currPlayer, bft.tempBoard, mydepth, globalDepth,false);

				}


			}


		}
		return gameTree;

	}








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
		System.out.println();
		System.out.println("###################################");
		System.out.println("for move" +  moveIndex) ;
		//System.out.println("before anyMove");
		//mancalaBoard.displayBoard();

		int boardSize = mancalaBoard.getBoardSize();
		int[] tempBoard = new int[boardSize];
		//int[] srcBoard = mancalaBoard.getBoard();

		System.arraycopy( srcBoard, 0, tempBoard, 0, boardSize );


		//int boardSize = mancalaBoard.getBoardSize();
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

		//System.out.println("after while");
		//displayHelper(tempBoard);
		//System.out.println();

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

		System.out.println("Source Board");
		displayHelper(srcBoard);
		System.out.println();
		System.out.println(" CHanged Board");
		displayHelper(tempBoard);
		System.out.println();
		BoardFTCheck bft = new BoardFTCheck(tempBoard,freeTurn);
		return bft;
	}



	public BoardFTCheck makeTempMiniMaxMoveWithGO(Move move, Player currPlayer, int[] srcBoard){


		int moveIndex = move.getMoveIndex();
		System.out.println();
		System.out.println("###################################");
		System.out.println("for move" +  moveIndex) ;
		//System.out.println("before anyMove");
		//mancalaBoard.displayBoard();

		int boardSize = mancalaBoard.getBoardSize();
		int[] tempBoard = new int[boardSize];
		//int[] srcBoard = mancalaBoard.getBoard();

		System.arraycopy( srcBoard, 0, tempBoard, 0, boardSize );


		//int boardSize = mancalaBoard.getBoardSize();
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

		//System.out.println("after while");
		//displayHelper(tempBoard);
		//System.out.println();

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

		System.out.println("Source Board");
		displayHelper(srcBoard);
		System.out.println();
		System.out.println(" CHanged Board");
		displayHelper(tempBoard);

		//if the game is over we need to evaluate the function
		boolean gameOver = false;
		if (isGameOver(tempBoard)){
			System.out.println();
			System.out.println("???????????????????????????????????");
			System.out.println("             GAME OVER       ");
			System.out.println("???????????????????????????????????");
			System.out.println();
			tempBoard = getAllStones(tempBoard);
			gameOver = true;
		}


		System.out.println();
		BoardFTCheck bft = new BoardFTCheck(tempBoard,freeTurn,gameOver);
		return bft;
	}



	public BoardFTCheck makeTempMiniMaxMoveWithGO2(Move move, Player currPlayer, int[] srcBoard){


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




	//evaluate all tree and select the correct move
	public MiniMaxNode evaluateGameTree(MiniMaxNode rootNode, int height, int globalDepth)
	{
		//System.out.println("height : " + height);
		if (height == globalDepth)
		{
			//System.out.println("reached the maxDepth");
			//System.out.println("height : " + height);
			if (globalDepth % 2 == 0){
				//toggle the sign if the level is even

				rootNode.setEvalVal(rootNode.getEvalVal() * -1);
			}
			//System.out.println("returning from: " + rootNode.getMoveIndex().getMoveIndex() );
			return rootNode; 
		}
		else {
			//System.out.println(rootNode.getMoveIndex().getMoveIndex());
			ArrayList<MiniMaxNode> childNodes = rootNode.getChildrenList();
			//sort the list to get the first min according to the rule
			Collections.sort(childNodes);



			for (MiniMaxNode node : childNodes){
				MiniMaxNode retMove = evaluateGameTree(node, node.getDepth(), globalDepth);

				if (rootNode.getDepth() % 2 == 0){
					//even level has to be minimizer
					//System.out.println("The depth for node : " + rootNode.getMoveIndex().getMoveIndex() + 
					//" +is : " + rootNode.getDepth());
					//System.out.println("returned evalfunc : " + retMove.getEvalVal());
					//System.out.println(" nodes evalfunc before changing :  "+ rootNode.getEvalVal());

					if (retMove.getEvalVal() > rootNode.getEvalVal()){


						rootNode.setEvalVal(retMove.getEvalVal());

						//System.out.println(" nodes evalfunc after changing :  "+ rootNode.getEvalVal());
					}

				} else {
					//odd level has to be a maximizer

					//System.out.println("The depth for node : " + rootNode.getMoveIndex().getMoveIndex() + 
					//	" +is : " + rootNode.getDepth());
					//System.out.println("returned evalfunc : " + retMove.getEvalVal());
					//System.out.println(" nodes evalfunc before changing :  "+ rootNode.getEvalVal());


					if(retMove.getEvalVal() < rootNode.getEvalVal()){

						rootNode.setEvalVal(retMove.getEvalVal());

						//System.out.println(" nodes evalfunc after changing :  "+ rootNode.getEvalVal());
					}
				}
			}
		}

		return rootNode;
	}





	public MiniMaxNode evaluateGameTreeWithGO(MiniMaxNode rootNode, int height, int globalDepth)
	{
		System.out.println("in eval madhuri");
		System.out.println("height : " + height);
		System.out.println("global depth : " + globalDepth);
		if (height == globalDepth)
		{
			System.out.println("reached the maxDepth");
			System.out.println("height : " + height);
			if (globalDepth % 2 == 0){
				//toggle the sign if the level is even
				System.out.println("in if of toggling level");
				rootNode.setEvalVal(rootNode.getEvalVal() * -1);
			}
			System.out.println("returning from: " + rootNode.getMoveIndex().getMoveIndex() );
			return rootNode; 
		}
		else {
			System.out.println("came in else");
			System.out.println("board is");
			//displayHelper(rootNode.nodeBoard);


			//System.out.println(rootNode.getMoveIndex().getMoveIndex());
			ArrayList<MiniMaxNode> childNodes = rootNode.getChildrenList();
			//sort the list to get the first min according to the rule
			//Collections.sort(childNodes);

			for(MiniMaxNode childNode: childNodes){
				System.out.println(childNode.moveIndex.getMoveIndex() + " : " + childNode.getEvalVal());
				//childNode.getMoveIndex());

			}



			//we have not reached the depth but its still a leaf
			if (childNodes == null || childNodes.isEmpty()){
				if (rootNode.getDepth() % 2 == 0){
					System.out.println("The children were empty here");
					//toggle the sign if the level is even
					rootNode.setEvalVal(rootNode.getEvalVal() * -1);
				} 

				return rootNode; 
			}



			for (MiniMaxNode node : childNodes){
				int count = 1;
				displayHelper(node.getNodeBoard());
				System.out.println("list size is: " + childNodes.size() );
				System.out.println("going in for loop " + count++);
				MiniMaxNode retMove = evaluateGameTreeWithGO(node, node.getDepth(), globalDepth);

				if (rootNode.getDepth() % 2 == 0){
					//even level has to be minimizer
					System.out.println("MINIMIZER : The depth for node : " + rootNode.getMoveIndex().getMoveIndex() + 
							" +is : " + rootNode.getDepth());
					System.out.println("returned evalfunc : " + retMove.getEvalVal());
					System.out.println(" nodes evalfunc before changing :  "+ rootNode.getEvalVal());

					if (retMove.getEvalVal() > rootNode.getEvalVal()){


						rootNode.setEvalVal(retMove.getEvalVal());

						System.out.println(" nodes evalfunc after changing :  "+ rootNode.getEvalVal());
					}

				} else {
					//odd level has to be a maximizer

					System.out.println("MAXIMIZER The depth for node : " + rootNode.getMoveIndex().getMoveIndex() + 
							" +is : " + rootNode.getDepth());
					System.out.println("returned evalfunc : " + retMove.getEvalVal());
					System.out.println(" nodes evalfunc before changing :  "+ rootNode.getEvalVal());


					if(retMove.getEvalVal() < rootNode.getEvalVal()){

						rootNode.setEvalVal(retMove.getEvalVal());

						System.out.println(" nodes evalfunc after changing :  "+ rootNode.getEvalVal());
					}
				}
			}
		}

		return rootNode;

	}





	public MiniMaxNode evaluateGameTreeWithGO2(MiniMaxNode rootNode, int height, int globalDepth)
	{

		//if (height == globalDepth)
		//{
		if ((height == globalDepth) && globalDepth % 2 == 0 && (rootNode.childrenList.isEmpty() 
				|| rootNode.childrenList == null)){
			//toggle the sign if the level is even
			System.out.println();
			displayHelper(rootNode.nodeBoard);
			System.out.println(" This is a leaf node");
			rootNode.setEvalVal(rootNode.getEvalVal() * -1);
			displayHelper(rootNode.nodeBoard);
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
				MiniMaxNode retMove = evaluateGameTreeWithGO2(node, node.getDepth(), globalDepth);

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




















	//Generate nextMove according to aplha beta algorithm
	public Move getAlphaBetaMove(){
		return null;
	}

	//The logic for greedy algorithm 

	public boolean reachedMaxDepth(Position p, int depth){
		return false;
	}




	public int getScore(Player currPlayer){
		return (mancalaBoard.getBoard())[(mancalaBoard.getMancala
				(currPlayer.playerNum))];
	}

	public int getEvalFunc(Player currPlayer, int[] tempBoard){
		int mancalaIndex = mancalaBoard.getMancala(currPlayer.playerNum);
		//System.out.println("i am player " + currPlayer.playerNum + "  & my mancala is "  + mancalaIndex );

		int oppMancalaIndex = mancalaBoard.getOpponentsMancala(currPlayer.playerNum);
		//System.out.println("i am opponent & my mancala is "  + oppMancalaIndex );

		//System.out.println("value in board for the indices are : " + tempBoard[mancalaIndex] + " & " + tempBoard[oppMancalaIndex] );
		return (tempBoard[mancalaIndex] - tempBoard[oppMancalaIndex]);
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

	public boolean isIllegalMove(Move myMove, int[] tempBoard){

		if (tempBoard[myMove.getMoveIndex()] == 0){
			return true;
		}
		return false;
	}

	public static void main(String[] args){
		Mancala playObj = new Mancala();
		playObj.playGame();

	}



}



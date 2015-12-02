import java.util.ArrayList;


class MiniMaxNode implements Comparable<MiniMaxNode> {
	int[] nodeBoard;
	int playerNum;
	int depth;
	int evalVal;
	Move moveIndex;
	MiniMaxNode parentNode;
	ArrayList<MiniMaxNode> childrenList; 
	boolean parentGotaFreeTurn;

	MiniMaxNode(int[] nodeBoard,
			int playerNum,
			int depth,
			int evalVal,
			MiniMaxNode parentNode, 
			Move moveIndex,
			boolean wasFromFreeTurn){

		this.nodeBoard = nodeBoard;
		this.playerNum = playerNum;
		this.depth = depth;
		this.evalVal = evalVal;
		this.parentNode = parentNode;
		childrenList = new ArrayList<MiniMaxNode>();
		this.moveIndex = moveIndex;
		this.parentGotaFreeTurn = wasFromFreeTurn;


	}

	public Move getMoveIndex() {
		return moveIndex;
	}

	public void setMoveIndex(Move moveIndex) {
		this.moveIndex = moveIndex;
	}

	public boolean isparentGotaFreeTurn() {
		return parentGotaFreeTurn;
	}

	public void setWasFromFreeTurn(boolean parentGotaFreeTurn) {
		this.parentGotaFreeTurn = parentGotaFreeTurn;
	}


	public int[] getNodeBoard() {
		return nodeBoard;
	}

	public void setNodeBoard(int[] nodeBoard) {
		this.nodeBoard = nodeBoard;
	}


	public int getPlayerNum() {
		return playerNum;
	}

	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getEvalVal() {
		return evalVal;
	}

	public void setEvalVal(int evalVal) {
		this.evalVal = evalVal;
	}

	public MiniMaxNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(MiniMaxNode parentNode) {
		this.parentNode = parentNode;
	}

	public ArrayList<MiniMaxNode> getChildrenList() {
		return childrenList;
	}

	public void addToChildrenList(MiniMaxNode child) {
		(this.childrenList).add(child);
	}


	@Override
	public int compareTo(MiniMaxNode node2) {
		return (moveIndex.getMoveIndex() - node2.getMoveIndex().getMoveIndex());
	}


}

public class MiniMaxTree{

	private MiniMaxNode root;

	public MiniMaxTree(){
		root = null;
	}

	public MiniMaxNode insert(MiniMaxNode nodeToInsert, MiniMaxNode parentNode ){

		if (root == null){

			root = nodeToInsert;
		} else {
			MiniMaxNode p = findNode(root,parentNode);
			System.out.println("adding at node ");
			//displayHelper(parentNode.nodeBoard);

			p.addToChildrenList(nodeToInsert);
		}

		//System.out.println("Root");
		//displayHelper(root.nodeBoard);
		//System.out.println();
		//System.out.println("printed root");
		return root;
	}

	public void displayHelper(int[] board){
		//System.out.println("################");
		//System.out.println("in tree insert");
		//System.out.println("################");
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




	public MiniMaxNode findNode(MiniMaxNode root, MiniMaxNode nodeToFind){

		if (root == nodeToFind){
			return root;
		}

		//then check for all children
		for (MiniMaxNode nodeI : root.getChildrenList()){
			root = findNode(nodeI, nodeToFind);
			if (root != null){
				return root;
			}

		}
		return null;

	}

	public MiniMaxNode getRoot(){
		return root;
	}



	public void displayTree(){
		System.out.println("DISPLAYING THE TREE");
		display(root);
	}
	
	public void display(MiniMaxNode root){
		if (root == null){
			return ;
		}
		else{
			for(MiniMaxNode s: root.getChildrenList()){
				display(s);
				displayHelper(s.nodeBoard);
				System.out.println("the eval is : " + s.getEvalVal());
				System.out.println();
				
			}

		}

	}
}





public class MiniMaxReturnNode {
	MiniMaxNode node;
	Move nextMove;
	
	MiniMaxReturnNode(MiniMaxNode node,Move nextMove){
			this.node = node;
			this.nextMove = nextMove;
	}
}

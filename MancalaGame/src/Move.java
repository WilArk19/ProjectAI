//Class for definig a move
public class Move implements Comparable<Move> {
	
	private int moveIndex;
	
	
	public int getMoveIndex() {
		return moveIndex;
	}

	public void setMoveIndex(int moveIndex) {
		this.moveIndex = moveIndex;
	}

	public Move(int moveIndex){
		this.moveIndex = moveIndex;
		
	}

	public Move(){}
	
	
	@Override
	public int compareTo(Move move2) {
	    return (moveIndex - move2.getMoveIndex());
	}
}

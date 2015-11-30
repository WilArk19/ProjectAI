
public class BoardFTCheck {
	int[] tempBoard;
	public boolean freeTurn;
	public boolean leadsToGameOver;
	
	BoardFTCheck(int[] tempBoard, boolean freeTurn){
		this.tempBoard = tempBoard;
		this.freeTurn = freeTurn;
	}
	
	BoardFTCheck(int[] tempBoard, boolean freeTurn, boolean leadsToGameOver){
		this.tempBoard = tempBoard;
		this.freeTurn = freeTurn;
		this.leadsToGameOver = leadsToGameOver;
	}
	 
}

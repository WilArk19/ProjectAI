public class Position {
	//number of stones in the mancala
	private int numOfStones;
	
	// is the index Mancala 
	private boolean isMancala;
	
	//the player : Human - 0 Comp - 1 
	private int playerNum;
	
	//does the position leads to a free turn
	private boolean freeTurn;
	
	public boolean isFreeTurn() {
		return freeTurn;
	}
	
	public void setFreeTurn(boolean freeTurn) {
		this.freeTurn = freeTurn;
	}
	
	public int getNumOfStones() {
		return numOfStones;
	}
	public void setNumOfStones(int numOfStones) {
		this.numOfStones = numOfStones;
	}
	
	public boolean isMancala() {
		return isMancala;
	}
	
	public void setHuman(boolean isMancala) {
		this.isMancala = isMancala;
	}
	
	public int getPlayerNum() {
		return playerNum;
	}
	
	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}
	
	
	

	
}

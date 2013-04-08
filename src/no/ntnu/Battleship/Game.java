package no.ntnu.Battleship;

public class Game {
	Board player1Board;
	Board player2Board;
	boolean p1Turn;
	
	public Game(Board p1, Board p2) {
		this.player1Board = p1;
		this.player2Board = p2;
		p1Turn = true;
	}
	
	public Board getActive() {
		if (p1Turn) return player1Board;
		return player2Board;
	}
	
	
	
}

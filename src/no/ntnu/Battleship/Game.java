package no.ntnu.Battleship;

import java.util.ArrayList;

public class Game {
	Board player1Board;
	Board player2Board;
	int p1Shots;
	int p2Shots;
	boolean p1Turn;
	ArrayList<GameListener> listeners;
	
	public Game(Board p1, Board p2, int p1InitShots, int p2InitShots) {
		this.player1Board = p1;
		this.player2Board = p2;
		p1Shots = p1InitShots;
		p2Shots = p2InitShots;
		p1Turn = false;
		listeners = new ArrayList<GameListener>();
	}
	
	/**
	 * Set the current player to the next turn and get the board.
	 * @return
	 */
	public Board getAndSwitchActive() {
		p1Turn = !p1Turn;
		if (p1Turn) return player2Board;
		return player1Board;
	}
	
	public int getShots() {
		if (p1Turn) return p1Shots;
		return p2Shots;
	}
	
	public void updateShots(int newCount) {
		if (p1Turn) {
			p2Shots = newCount;
		} else {
			p1Shots = newCount;
		}
	}
	
	public void addListener(GameListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener(GameListener listener) {
		this.listeners.remove(listener);
	}
	
	public void gameChanged() {
		for (GameListener listener:listeners) {
			listener.fireGameChangedEvent();
		}
	}
	
}

package no.ntnu.Battleship;

import java.util.ArrayList;

public class Game {
	Board player1Board;
	Board player2Board;
	boolean p1Turn;
	ArrayList<GameListener> listeners;
	
	public Game(Board p1, Board p2) {
		this.player1Board = p1;
		this.player2Board = p2;
		p1Turn = false;
		listeners = new ArrayList<GameListener>();
	}
	
	public Board getAndSwitchActive() {
		p1Turn = !p1Turn;
		if (p1Turn) return player1Board;
		return player2Board;
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

package no.ntnu.Battleship;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Game extends Activity {
	private static final String TAG = "Battleship";

	public static final String KEY_SIZE = "no.ntnu.Battleship.size";
	public static final int SIZE_SMALL = 0;
	public static final int SIZE_MEDIUM = 1;
	public static final int SIZE_LARGE = 2;

	Board player1Board;
	Board player2Board;
	int p1Shots;
	int p2Shots;
	boolean p1Turn;

	// ######################
	PlatformFactory platformFactory = new PlatformFactory();
	DeviceFactory deviceFactory = new DeviceFactory();
	// ######################

	ArrayList<GameListener> listeners;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreate");

		int size = 0;
		switch (getIntent().getIntExtra(KEY_SIZE, SIZE_SMALL)) {
		case 0:
			size = 10;
			break;
		case 1:
			size = 15;
			break;
		case 2:
			size = 20;
			break;
		default:
			break;
		}

		listeners = new ArrayList<GameListener>();

		player1Board = new Board(this, size);
		player2Board = new Board(this, size);
		
		player1Board.populate(platformFactory.createPlatforms());
		player2Board.populate(platformFactory.createPlatforms());

		// TODO: activate a board viewer
		// setContentView(boardViewer);
		// boardViewer.requestFocus();

	}

	public Game() {

	};
	
	/**
	 * this constructor is never called due to how android launches activities
	 * @param p1
	 * @param p2
	 * @param p1InitShots
	 * @param p2InitShots
	 */
	public Game(Board p1, Board p2, int p1InitShots, int p2InitShots) {
		this.player1Board = p1;
		this.player2Board = p2;
		p1Shots = p1InitShots;
		p2Shots = p2InitShots;
		p1Turn = false;

		// ######################
		player1Board.populate(platformFactory.createPlatforms());
		player2Board.populate(platformFactory.createPlatforms());
		// ######################
	}

	/**
	 * Set the current player to the next turn and get the board.
	 * 
	 * @return
	 */
	public Board getAndSwitchActive() {
		p1Turn = !p1Turn;
		if (p1Turn)
			return player2Board;
		return player1Board;
	}

	public int getShots() {
		if (p1Turn)
			return p1Shots;
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

	public void fireGameChanged() {
		for (GameListener listener : listeners) {
			listener.gameChanged();
		}
	}

}

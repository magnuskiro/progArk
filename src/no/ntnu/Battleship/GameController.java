package no.ntnu.Battleship;

import java.util.ArrayList;
import java.util.HashMap;

import no.ntnu.Battleship.graphics.GameViewer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.SoundPool;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class GameController extends Activity implements OnClickListener {
	private static final String TAG = "Battleship";

	public static final String KEY_SIZE = "no.ntnu.Battleship.size";
	public static final int SIZE_SMALL = 0;
	public static final int SIZE_MEDIUM = 1;
	public static final int SIZE_LARGE = 2;

	public GameActivity myActivity;
	SoundPool sp;
	HashMap<Integer, Integer> soundsMap;
	private int size;

	Board player1Board;
	Board player2Board;
	int p1Shots;
	int p2Shots;
	boolean p1Turn;
	private boolean[] placedPlatforms;
	private PlatformFactory pFactory;


	ArrayList<GameListener> listeners;

	GameViewer boardViewer;

	private boolean[] winState;



	/**
	 * Create a new GameController
	 * @param size the dimensions of the board (10x10, 15x15 or 20x20)
	 * @param sp SoundPool for playing sound effects
	 * @param hm list of sounds that can be played by the SoundPool
	 */
	public GameController(int size, SoundPool sp, HashMap hm) {
		listeners = new ArrayList<GameListener>();
		this.sp = sp;
		this.soundsMap = hm;
		placedPlatforms = new boolean[2];
		this.size = size;
		this.pFactory = new PlatformFactory();
		p1Turn = true;
		winState = new boolean []{false, false};

	}

	/**
	 * Retrieves the platforms to be created from the platform factory
	 * @return an array list of platforms
	 */
	public ArrayList<Platform> getPlatforms() {
		if(player1Board == null || player2Board == null) {
			return pFactory.createPlatforms();
		}
		return null;
	}

	/**
	 * Receives a list of platforms from GameViewer and passes them on to
	 * the first board which is not populated by platforms
	 * @param pforms list of platforms to be placed on board
	 * @return if placement is successful or not
	 */
	public boolean setPlatforms(ArrayList<Platform> pforms) {
		Log.d("p1turn", "" + p1Turn);
		if(player1Board == null) {
			player1Board = new Board(this, (10+5*this.size));
			if(player1Board.populate(pforms)){
				p1Shots = player1Board.getShots();
				placedPlatforms[0] = true;
				p1Turn = !p1Turn;
				return true;
			}else{
				player1Board = null;
				return false;
			}
		} else if (player2Board == null) {
			player2Board = new Board(this, (10+5*this.size));
			if(player2Board.populate(pforms)){
				p2Shots = player2Board.getShots();
				placedPlatforms[1] = true;
				return true;
			}else{
				player2Board = null;
				return false;
			}
		}
		return false;
	}

	/**
	 * Set the current player to the next turn and get the board.
	 * @return the new active board
	 */
	public Board getAndSwitchActive() {
		p1Turn = !p1Turn;
		if (p1Turn)
			return player2Board;
		return player1Board;
	}

	/**
	 * Get players remaining shots this round
	 * Not in use ATM
	 * @return number of shots available
	 */
	public int getShots() {
		if (p1Turn)
			return p1Shots;
		return p2Shots;
	}

	/**
	 * Update the players shot count
	 * Not in use ATM
	 * @param newCount updated number of remaining shots
	 */
	public void updateShots(int newCount) {
		if (p1Turn) {
			p2Shots = newCount;
		} else {
			p1Shots = newCount;
		}
	}

	/**
	 * Add a new listener
	 * @param listener
	 */
	public void addListener(GameListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Remove specified listener
	 * @param listener
	 */
	public void removeListener(GameListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * Something in the game has changed, notify listeners
	 */
	public void fireGameChanged() {
		for (GameListener listener : listeners) {
			listener.gameChanged();
		}
	}


	/**
	 * Checks to see if player 1 is the active player
	 * @return true if player 1 is active, false if not
	 */
	public boolean isPlayer1turn(){
		return p1Turn;
	}


	/**
	 * get which players have placed their platforms
	 * @return the placedPlatforms index 0:p1, 1:p2
	 */
	public boolean[] getPlacedPlatforms() {
		return placedPlatforms;
	}

	/**
	 * set which players have placed their platforms
	 * @param placedPlatforms index 0:p1, 1:p2
	 */
	public void setPlacedPlatforms(boolean[] placedPlatforms) {
		this.placedPlatforms = placedPlatforms;
	}

	/**
	 * Figure out which button was pressed
	 */
	@Override
	public void onClick(View v) {
		// Figure out which button was clicked
		switch (v.getId()) {
		case R.id.button_confirm_placement:
			openConfirmDialog();
			break;
		case R.id.button_confirm_attack:
			if (!getWinState()[0] && !getWinState()[1]){
				//kaboom?
				if (p1Turn) {
					player2Board.attack(boardViewer.getSelected());
				} else {
					player1Board.attack(boardViewer.getSelected());
				}
				refreshWinState();
			}
			break;
		}
	}


	/**
	 * Opens a dialog that asks the user if he wants to confirm the placement of his platforms
	 */
	private void openConfirmDialog() {
		new AlertDialog.Builder(myActivity).setTitle(R.string.confirm_placement_title)
		.setItems(R.array.confirm_placement, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialoginterface, int i) {
				if (i == 0) {
					if(player1Board== null || player2Board == null){
						boardViewer.placePlatforms();
					}
				}
				return;
			}
		})
		.show();
	}

	/**
	 * checks if one of the players has destroyed the opponent's platforms, and sets
	 * the appropriate win-state
	 */
	protected void refreshWinState() {
		getWinState()[0] = player2Board.isAllDestroyed();

		if (getWinState()[0])
			Log.d("GameViewer", "Player 1 won");
		getWinState()[1] = player1Board.isAllDestroyed();
		if(getWinState()[1])
			Log.d("GameViewer", "Player 2 won");
	}

	/**
	 * @return the winState
	 */
	public boolean[] getWinState() {
		return winState;
	}

	
	/**
	 * Play sound effect if sound effects is enabled in settings
	 * @param sound the index of the sound to be played
	 */
	public void playSound(int sound) {
		if(Prefs.getSFX(myActivity))
			sp.play(soundsMap.get(sound), 1, 1, 1, 0, 1);
	}

}

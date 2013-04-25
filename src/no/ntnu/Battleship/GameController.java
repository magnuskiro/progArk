package no.ntnu.Battleship;

import java.util.ArrayList;
import java.util.HashMap;

import no.ntnu.Battleship.graphics.GameViewer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
	 * this constructor is never called due to how android launches activities
	 * @param p1
	 * @param p2
	 * @param p1InitShots
	 * @param p2InitShots
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

	public ArrayList<Platform> getPlatforms() {
		if(player1Board == null || player2Board == null) {
			return pFactory.createPlatforms();
		}
		return null;
	}

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

	public void playSound(int sound) {
		if(Prefs.getSFX(myActivity))
			sp.play(soundsMap.get(sound), 1, 1, 1, 0, 1);
	}

}

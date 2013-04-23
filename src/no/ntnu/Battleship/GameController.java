package no.ntnu.Battleship;

import java.util.ArrayList;

import no.ntnu.Battleship.graphics.GameViewer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class GameController extends Activity implements OnClickListener {
	private static final String TAG = "Battleship";

	public static final String KEY_SIZE = "no.ntnu.Battleship.size";
	public static final int SIZE_SMALL = 0;
	public static final int SIZE_MEDIUM = 1;
	public static final int SIZE_LARGE = 2;
	
	Activity myActivity;
	
	private int size;
	
	Board player1Board;
	Board player2Board;
	int p1Shots;
	int p2Shots;
	boolean p1Turn;
	private boolean[] placedPlatforms;
	private PlatformFactory pFactory;

	// ######################
	PlatformFactory platformFactory = new PlatformFactory();
	DeviceFactory deviceFactory = new DeviceFactory();
	// ######################

	ArrayList<GameListener> listeners;
	
	GameViewer boardViewer;

	
	
	/**
	 * this constructor is never called due to how android launches activities
	 * @param p1
	 * @param p2
	 * @param p1InitShots
	 * @param p2InitShots
	 */
	public GameController(int size) {
		listeners = new ArrayList<GameListener>();
		player1Board = new Board(this, size);
		player2Board = new Board(this, size);
		
		placedPlatforms = new boolean[2];
		
		this.size = size;
		this.pFactory = new PlatformFactory();
		p1Turn = false;
		
	}

	public ArrayList<Platform> getPlatforms() {
		if(player1Board == null || player2Board == null) {
			return pFactory.createPlatforms();
		}
		return null;
	}
	
	public boolean setPlatforms(ArrayList<Platform> pforms) {
		if(player1Board == null) {
			player1Board = new Board(this, (10+5*this.size));
			player1Board.populate(pforms);
			p1Shots = player1Board.getShots();
			return true;
		} else if (player2Board == null) {
			player2Board = new Board(this, (10+5*this.size));
			player2Board.populate(pforms);
			p2Shots = player2Board.getShots();
			return true;
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

	public  PlatformFactory  getPlatformFactory(){
		return platformFactory;
	}
	
	public DeviceFactory getdDeviceFactory(){
		return deviceFactory;
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
		}
	}
	
	
	private void openConfirmDialog() {
		new AlertDialog.Builder(myActivity).setTitle(R.string.confirm_placement_title)
		.setItems(R.array.confirm_placement, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialoginterface, int i) {
				if (i == 0) 
					fireGameChanged();
				else
					return;
				return;
			}
		})
		.show();
	}

}

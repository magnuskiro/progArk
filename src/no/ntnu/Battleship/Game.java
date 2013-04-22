package no.ntnu.Battleship;

import java.util.ArrayList;

import no.ntnu.Battleship.graphics.BoardGraphics;
import no.ntnu.Battleship.graphics.PlatformView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Game extends Activity implements OnClickListener {
	private static final String TAG = "Battleship";

	public static final String KEY_SIZE = "no.ntnu.Battleship.size";
	public static final int SIZE_SMALL = 0;
	public static final int SIZE_MEDIUM = 1;
	public static final int SIZE_LARGE = 2;
	
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
	
	BoardGraphics boardViewer;

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
		
		placedPlatforms = new boolean[2];
		
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		boardViewer = new BoardGraphics(size, dm.widthPixels, dm.heightPixels, getApplicationContext(), this);
		
		// Define a linearlayout and add boardViewer and buttons to it
		RelativeLayout rLay = new RelativeLayout(this);
		rLay.addView(boardViewer);
		//gett PlatformViews from boardViewer 
		ArrayList<PlatformView> plats = boardViewer.getPlatformViews();
		for(PlatformView plat : plats){
			rLay.addView(plat);
		}
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(rLay);
		LinearLayout row = new LinearLayout(this);
		row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		row.setGravity(Gravity.CENTER_HORIZONTAL);
		row.addView((LinearLayout) View.inflate(this, R.layout.placement_buttons, null));
		layout.addView(row);

		// TODO: activate a board viewer
		 setContentView(layout);
		 boardViewer.requestFocus();
		 
		 View confirmPlacementButton = findViewById(R.id.button_confirm_placement);
		 confirmPlacementButton.setOnClickListener(this);

	}
	
	/**
	 * this constructor is never called due to how android launches activities
	 * @param p1
	 * @param p2
	 * @param p1InitShots
	 * @param p2InitShots
	 */
	public Game(int size) {
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
		new AlertDialog.Builder(this).setTitle(R.string.confirm_placement_title)
		.setItems(R.array.confirm_placement, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialoginterface, int i) {
				return;
			}
		})
		.show();
	}
	
	
	
	private void switchView() {
		Board switchTo = getAndSwitchActive();
//		boardViewer.setActivateBoard(switchTo);
	}
}

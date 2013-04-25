package no.ntnu.Battleship;

import java.util.ArrayList;
import java.util.HashMap;

import no.ntnu.Battleship.graphics.GameViewer;
import no.ntnu.Battleship.graphics.PlatformView;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * This class creates the gameplay screen
 * @author Jan Alexander Stormark Bremnes
 *
 */
public class GameActivity extends Activity {
	private static final String TAG = "Battleship";

	public static final String KEY_SIZE = "no.ntnu.Battleship.size";
	public static final int SIZE_SMALL = 0;
	public static final int SIZE_MEDIUM = 1;
	public static final int SIZE_LARGE = 2;
	
	GameViewer gameViewer;
	GameController gameController;
	LinearLayout layout;
	MediaPlayer mPlayer;
	SoundPool sp;
	HashMap<Integer, Integer> soundsMap;
    int DESTROYED = 1;
    int MISS = 2;
    int HIT = 3;
	
	public GameActivity(){
		
	}
	
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
		
		mPlayer = MediaPlayer.create(this, R.raw.background2);
		
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundsMap = new HashMap<Integer, Integer>();
        soundsMap.put(DESTROYED, sp.load(this, R.raw.destroyed, 1));
        soundsMap.put(MISS, sp.load(this, R.raw.miss, 1));
        soundsMap.put(HIT, sp.load(this, R.raw.hit, 1));
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		gameController = new GameController(size, sp, soundsMap);
		gameController.myActivity = this;
		
		
		gameViewer = new GameViewer(size, dm.widthPixels, getApplicationContext(), gameController);
		gameController.boardViewer = gameViewer;
		
		// Define a linearlayout and add boardViewer and buttons to it
		RelativeLayout rLay = new RelativeLayout(this);
		rLay.addView(gameViewer);
		
		//get PlatformViews from boardViewer 
		ArrayList<PlatformView> plats = gameViewer.getPlatformViews();
		for(PlatformView plat : plats){
			rLay.addView(plat);
		}
		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(rLay);
		LinearLayout row = new LinearLayout(this);
		row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		row.setGravity(Gravity.CENTER_HORIZONTAL);
		row.addView((LinearLayout) View.inflate(this, R.layout.placement_buttons, null));
		layout.addView(row);

		 setContentView(layout);
		 gameViewer.requestFocus();

		 View confirmPlacementButton = findViewById(R.id.button_confirm_placement);
		 confirmPlacementButton.setOnClickListener(gameController);
	}
	
	
	/**
	 *Called when all users have placed their platforms, in order to display the new
	 *gameplay graphics
	 */
	public void refreshView() {
		layout.removeViewAt(1);
		LinearLayout row = new LinearLayout(this);
		row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		row.addView((RelativeLayout) View.inflate(this, R.layout.fire_buttons, null));
		layout.addView(row);

		 setContentView(layout);
		 gameViewer.requestFocus();

		 View confirmPlacementButton = findViewById(R.id.button_confirm_attack);
		 confirmPlacementButton.setOnClickListener(gameController);
	}
		
	
    /**
     * Creates the menu which is displayed when the user 
     * presses the menu button 
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }
    
    /**
     * Figures out which option in the menu the user selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.settings:
    		startActivity(new Intent(this, Prefs.class));
    		return true;
    	// More items here if any
    	}
    	return false;
    }
    

}

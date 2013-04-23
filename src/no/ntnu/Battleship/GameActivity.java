package no.ntnu.Battleship;

import java.util.ArrayList;

import no.ntnu.Battleship.graphics.GameViewer;
import no.ntnu.Battleship.graphics.PlatformView;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GameActivity extends Activity{
	private static final String TAG = "Battleship";

	public static final String KEY_SIZE = "no.ntnu.Battleship.size";
	public static final int SIZE_SMALL = 0;
	public static final int SIZE_MEDIUM = 1;
	public static final int SIZE_LARGE = 2;
	
	GameViewer gameViewer;
	GameController gameController;
	
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
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		gameController = new GameController(size);
		gameController.myActivity = this;
		
		
		gameViewer = new GameViewer(size, dm.widthPixels, dm.heightPixels, getApplicationContext(), gameController);
		
		// Define a linearlayout and add boardViewer and buttons to it
		RelativeLayout rLay = new RelativeLayout(this);
		rLay.addView(gameViewer);
		//gett PlatformViews from boardViewer 
		ArrayList<PlatformView> plats = gameViewer.getPlatformViews();
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
		 gameViewer.requestFocus();
		 
		 View confirmPlacementButton = findViewById(R.id.button_confirm_placement);
		 confirmPlacementButton.setOnClickListener(gameController);
	}
}

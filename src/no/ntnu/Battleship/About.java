package no.ntnu.Battleship;

import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;

/**
 * Diplays information about the game
 * @author Jan Alexander Stormark Bremnes
 *
 */
public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.finish();
		return super.onTouchEvent(event);
	}
	
	
}

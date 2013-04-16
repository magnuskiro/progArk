package no.ntnu.Battleship;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import no.ntnu.Battleship.R;

public class Splash extends Activity {

	// Bruker fuglekvitring som temporary lyd for å se at ting funker
	MediaPlayer splashSound;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Displays the splascreen layout
		setContentView(R.layout.splash);

		// Get the soundfile and start playing
		splashSound = MediaPlayer.create(Splash.this, R.raw.birds);
		splashSound.start();

		// Makes sure the splashscreen plays for 2 seconds, then opens Main
		// Activity through opening the intent STARTINGPOINT
		Thread timer = new Thread(){
			public void run(){
				try {
					sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					Intent openStartingPoint = new Intent("no.ntnu.Battleship.STARTINGPOINT");
					startActivity(openStartingPoint);
				}
			}
		};
		
		timer.start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// Stops the sound playing
		splashSound.release();
		finish();
	}
}

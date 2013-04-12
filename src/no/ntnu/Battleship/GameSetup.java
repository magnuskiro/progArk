package no.ntnu.Battleship;

import android.content.Context;
import android.os.AsyncTask;

public class GameSetup extends AsyncTask<Void, Integer, Void> {

	public GameSetup() {

	}
	@Override
	protected Void doInBackground(Void... params) {
		try {
			this.wait(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
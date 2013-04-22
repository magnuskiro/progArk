package no.ntnu.Battleship;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class BattleShip extends Activity implements OnClickListener {

	private static final String TAG = "BattleShip"; // used for logging
     // factories ++

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        
        // Set up click listeners for all the buttons
        View continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);
        View newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
        
    }

    // Create the settings menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }
    
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
    
	@Override
	public void onClick(View v) {
		// Figure out which button was clicked
		switch (v.getId()) {
		case R.id.about_button:
			Intent i = new Intent(this, About.class);
			startActivity(i);
			break;
		case R.id.new_game_button:
			openNewGameDialog();
			break;
		case R.id.exit_button:
			finish();
			break;
		// More buttons here 
		}
	}
	
	
	/*
	 * Creates a new dialog with the elements of the size-array and adds
	 * OnClickListeners to them, allowing the user to choose board size
	 */
	private void openNewGameDialog() {
		new AlertDialog.Builder(this).setTitle(R.string.new_game_title)
			.setItems(R.array.size, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialoginterface, int i) {
					startGame(i);
				}
			})
			.show();
	}

	/**
	 * 
	 * @param i the index of the board size selected from the list
	 */
	private void startGame(int i) {
		Log.d(TAG, "clicked on " + i);
		Intent intent = new Intent(this, GameController.class);
		intent.putExtra(GameController.KEY_SIZE, i);
		startActivity(intent);
	}
}

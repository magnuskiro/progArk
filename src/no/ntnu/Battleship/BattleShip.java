package no.ntnu.Battleship;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import no.ntnu.Battleship.Tasks.GameInitTask;
import no.ntnu.Battleship.Tasks.GameModeSelectViewTask;
import android.view.View;

public class BattleShip extends Activity {

     // factories ++

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // select game type
            // start screen view
            // choose game type
//        new GameModeSelectViewTask(this).execute();

        // initialize the game.
        new GameInitTask(this).execute();

        // player interaction

    }

    public void playBattleShips(View view) {
    	Intent intent = new Intent(this, ViewBoard.class);
    	startActivity(intent);
    }

}
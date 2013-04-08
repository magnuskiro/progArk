package no.ntnu.Battleship;

import no.ntnu.Battleship.Tasks.GameInitTask;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BattleShip extends Activity {

     // factories ++

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // game init
        new GameInitTask(this).execute();

        setContentView(R.layout.main);

    }

    public void playBattleShips(View view) {
    	Intent intent = new Intent(this, ViewBoard.class);
    	startActivity(intent);
    }

}
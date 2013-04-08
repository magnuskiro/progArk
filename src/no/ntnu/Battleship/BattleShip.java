package no.ntnu.Battleship;

import android.app.Activity;
import android.os.Bundle;
import no.ntnu.Battleship.Tasks.GameInitTask;

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


}
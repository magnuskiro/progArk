package no.ntnu.Battleship.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.NumberPicker;

//To use the AsyncTask, it must be subclassed
public class GameModeSelectViewTask extends AsyncTask<Void, Integer, Void>
        implements NumberPicker.OnValueChangeListener
{

    private Context context;
    private NumberPicker numberPicker;
    int num;
    boolean finish=false;

    public GameModeSelectViewTask(Context context){
        this.context = context;

    }

    //Before running code in separate thread
    @Override
    protected void onPreExecute(){
        numberPicker = new NumberPicker(context);
        numberPicker.setOnValueChangedListener(this);
    }

    //The code to be executed in a background thread.
    @Override
    protected Void doInBackground(Void... params)
    {
			/* This is just a code that delays the thread execution 4 times,
			 * during 850 milliseconds and updates the current progress. This
			 * is where the code that is going to be executed on a background
			 * thread must be placed.
			 */

        try {
            synchronized(this);
            while(!finish){

                this.wait(100);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // TODO

        //  create new abstract factories

        // create board p1
        // populate with factories
        // create p2 board
        // populate

        // create game(b1, b2)

        // context.set(game)

        return null;
    }

    //Update the progress
    @Override
    protected void onProgressUpdate(Integer... values)
    {
    }

    //after executing the code in the thread
    @Override
    protected void onPostExecute(Void result)
    {
        //close the progress dialog
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int oldval, int newval) {
        numberPicker.setValue(newval);
        num = numberPicker.getValue();
    }
}

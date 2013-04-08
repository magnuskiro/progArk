package no.ntnu.Battleship.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

//To use the AsyncTask, it must be subclassed
public class GameInitTask extends AsyncTask<Void, Integer, Void>
{

    private Context context;
    private ProgressDialog progressDialog;

    public GameInitTask(Context context){
        this.context = context;
    }

    //Before running code in separate thread
    @Override
    protected void onPreExecute()
    {
        progressDialog = ProgressDialog.show(context,"Loading...",
                "Initialising game, please wait...", false, false);
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
        try
        {
            //Get the current thread's token
            synchronized (this)
            {
                //Initialize an integer (that will act as a counter) to zero
                int counter = 0;
                //While the counter is smaller than four
                while(counter <= 4)
                {
                    //Wait 850 milliseconds
                    this.wait(850);
                    //Increment the counter
                    counter++;
                    //Set the current progress.
                    //This value is going to be passed to the onProgressUpdate() method.
                    publishProgress(counter*25);
                }
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
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
        //set the current progress of the progress dialog
        progressDialog.setProgress(values[0]);
    }

    //after executing the code in the thread
    @Override
    protected void onPostExecute(Void result)
    {
        //close the progress dialog
        progressDialog.dismiss();
    }
}

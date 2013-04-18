package no.ntnu.Battleship.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = GamePanel.class.getSimpleName();

	private int screenWidth;
	private int screenHeight;

	private BoardGraphics boardGraphics;

	private GameThread gameThread;

	public GamePanel(Context context) {
		super(context);
		
		// Gets screenWidth and screenHeight
		getHeightAndWidth(context);
		
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// create the game loop thread
		gameThread = new GameThread(getHolder(), this);

		boardGraphics = new BoardGraphics(20, screenWidth, screenHeight);

		// make the gamePanel focusable so it can handle events
		setFocusable(true);
		
		
	}

	private void getHeightAndWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and we can safely
		// start the game loop
		gameThread.setRunning(true);
		gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				gameThread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		boardGraphics.drawBoard(canvas);
		boardGraphics.drawIndexes(canvas);
		// Rect r = new Rect(10, 10, 100, 100);

	}
}

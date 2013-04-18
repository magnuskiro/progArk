package no.ntnu.Battleship.graphics;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	private static final String TAG = GameThread.class.getSimpleName();

	// SurfaceHolder which can access the physical surface
	private SurfaceHolder surfaceHolder;

	// Actual view handling inputs and draws surface
	private GamePanel gamePanel;

	// Flag to hold game state
	private boolean running;

	public void setRunning(boolean running) {
		this.running = running;
	}

	public GameThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
	}

	@Override
	public void run() {
		Canvas canvas;
		Log.d(TAG, "Starting game loop");
		while (running) {
			canvas = null;
			// Try locking the canvas for exclusive pixel editing in the surface
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					// update game state
					// render state to the screen
					// draws the canvas on the panel
					this.gamePanel.onDraw(canvas);
				}
			} finally {
				// in case of an exception the surface is not left in an
				// inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

}

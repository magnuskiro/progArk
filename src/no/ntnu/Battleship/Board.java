package no.ntnu.Battleship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Board extends View {
	private static final String TAG = "Battleship";
	private final Game myGame;
	private int size;
	private float width;	// width of one square
	private float height;	// height of one square
	private int selX;		// X index of selected square
	private int selY;		// Y index of selected square
	private final Rect selRect = new Rect();
	
	public Board(Context context, int size) {
		super(context);
		this.myGame = (Game) context;
		this.size = size;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		float s = size;
		Log.d(TAG, "onSizeChanged: s " +  s + " w " + w + " h " + h);
		width = w / s;
		height = w / s;
		getRect(selX, selY, selRect);
		Log.d(TAG, "onSizeChanged: width " + width + ", height " + height);
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	private void getRect(int x, int y, Rect rect) {
		rect.set((int) (x * width), (int) (y * height), 
				(int) (x * width + width), (int) (y * height + height));
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// Draw the background...
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.ocean_blue));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		
		// Draw the board...
		Paint dark = new Paint();
		dark.setColor(android.graphics.Color.BLACK);
		
		// Draw the grid lines
		for (int i = 0; i <= size; i++) {
			canvas.drawLine(0, i * width, getWidth(), i * width, dark);
			canvas.drawLine(i * width, 0, i * width, getWidth(), dark);
		}
		
		// Draw some text...
		canvas.drawText("hahaha", getWidth() / 2, getHeight() - 30, dark);
		
		// Draw the selection...
		Log.d(TAG, "selRect = " + selRect);
		Paint selected = new Paint();
		selected.setColor(getResources().getColor(R.color.square_selected));
		canvas.drawRect(selRect, selected);
	}
	
	
	private void select(int x, int y) {
		invalidate(selRect);
		selX = Math.min(Math.max(x, 0), size - 1);
		selY = Math.min(Math.max(y, 0), size - 1);
		getRect(selX, selY, selRect);
		invalidate(selRect);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);
		
		select((int) (event.getX() / width), (int) (event.getY() / height));
		Log.d(TAG, "onTouchEvent: x = " + event.getX() + " y = " + event.getY());
		return true;
	}
	
	
}








/*
public class Board {
	private final int sizex = 10;
	private final int sizey = 10;
	
	private boolean[][] isShot;
	private boolean[][] isPlatform;
	private ArrayList<Platform> platforms;
	Game myGame;
	
	public Board(Game thisGame) {
		isShot = new boolean[sizex][sizey];
		isPlatform = new boolean[sizex][sizey];
		platforms = new ArrayList<Platform>();
		this.myGame = thisGame;
	}
	
	public void populate(ArrayList<Platform> newPlatforms) {
		this.platforms = newPlatforms;
	}
	
	public void attack(int[][] shots) {
		
		//TODO: input shots, calculate hits, misses, etc.
		myGame.gameChanged();
	}
	
}
*/

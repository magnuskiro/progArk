package no.ntnu.Battleship.graphics;

import java.util.ArrayList;

import no.ntnu.Battleship.Board;
import no.ntnu.Battleship.GameController;
import no.ntnu.Battleship.GameListener;
import no.ntnu.Battleship.Platform;
import no.ntnu.Battleship.R;
import no.ntnu.Battleship.TileNum;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * Graphical representation of a {@link GameController}, will connect to a provided controller
 * and pass user interactions to it, and react to the changes.
 * @author Håvard
 *
 */
public class GameViewer extends View implements GameListener{

	Resources res;
	Paint background;
	Paint dark;
	Paint bright;
	Paint selected;
	Paint win;
	Paint transparent;
	Canvas canvas;

	private float tileSize;
	private int boardSize;
	private int screenWidth;
	public static int selX;		// X index of selected square
	public static int selY;		// Y index of selected square
	private final Rect selRect = new Rect();
	private static final String TAG = "GameViewer";

	ArrayList<Platform> p1Platforms;
	ArrayList<Platform> p2Platforms;

	Board activeBoard;
	GameController game;
	Bitmap miss;
	Bitmap hit;
	Bitmap destroyed;
	Bitmap player1;
	Bitmap player2;
	Bitmap p1wins;
	Bitmap p2wins;

	ArrayList<PlatformView> platformViews;
	public long time;


	/**
	 * Main constructor
	 * @param boardSize - the number of tiles along one side of the board
	 * @param screenWidth - the width of the device screen in pixels
	 * @param context - the application {@link Context}
	 * @param game - the {@link GameController} this view connects to
	 */
	public GameViewer(int boardSize, int screenWidth, Context context, GameController game) {
		super(context);
		this.tileSize = screenWidth / boardSize;
		this.boardSize = boardSize;
		this.screenWidth = screenWidth;
		this.game = game;
		this.res = getResources();
		game.addListener(this);

		background = new Paint();
		background.setColor(res.getColor(R.color.ocean_blue));
		dark = new Paint();
		dark.setColor(android.graphics.Color.BLACK);
		selected = new Paint();
		selected.setColor(res.getColor(R.color.square_selected));
		bright = new Paint();
		bright.setColor(android.graphics.Color.WHITE);
		win = new Paint();
		win.setColor(android.graphics.Color.WHITE);
		win.setTextSize(50);
		transparent = new Paint();
		transparent.setAlpha(60);

		//loading and scaling the bitmaps
		miss = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.ship_miss_96),
				(int)tileSize, (int)tileSize, false);
		hit = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.ship_hit_96),
				(int)tileSize, (int)tileSize, false);
		destroyed = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.wreck_96),
				(int)tileSize, (int)tileSize, false);
		player1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.one), 
				(int)tileSize * 3, (int)tileSize * 5, false);
		player2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.two), 
				(int)tileSize * 3, (int)tileSize * 5, false);
		p1wins = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.p1wins), 
				screenWidth, (int)(screenWidth * 0.422), false);
		p2wins = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.p2wins), 
				screenWidth, (int)(screenWidth * 0.422), false);


		p1Platforms = game.getPlatforms();
		p2Platforms = game.getPlatforms();

		platformViews = new ArrayList<PlatformView>();
		for (Platform plat:p1Platforms){
			PlatformView pView = new PlatformView(context, plat, res, tileSize);
			pView.setOnTouchListener(new MyTouchListener());
			platformViews.add(pView);

		}

		setOnDragListener(new MyDragListener());

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		float s = boardSize;
		tileSize = w / s;
		getRect(selX, selY, selRect);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public void gameChanged() {
		// TODO react to changes
		Log.d("GameViewer", "gameChanged");
		activeBoard = game.getAndSwitchActive();
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas){
		this.canvas = canvas;
		Log.d("drawing", "drawing");

		// Draw the board...
		canvas.drawRect(0, 0, getWidth(), getWidth() + 50, background);


		// Draw the grid lines
		for (int i = 0; i <= boardSize; i++) {
			canvas.drawLine(0f,(float)( i * tileSize), (float)getWidth(), (float)( i * tileSize), dark);
			canvas.drawLine((float)( i * tileSize), 0f, (float)( i * tileSize), (float)getWidth(), dark);
		}

		boolean[] winState = game.getWinState();


		boolean[] placedplats = game.getPlacedPlatforms(); 
		if(placedplats[0] && placedplats[1]){
			TileNum[][] tileNum = activeBoard.getTiles();
			//only after both  players have placed their platforms
			Log.d("drawing", "both placed");
			// Draw the selection...
			canvas.drawRect(selRect, selected);
			//add bitmaps to tiles
			for (int i = 0; i<tileNum.length; i++){
				for (int j = 0; j<tileNum[i].length; j++){
					if(tileNum[i][j] == TileNum.EMPTY){
						//nothing to do
					}else if(tileNum[i][j] == TileNum.MISS){
						canvas.drawBitmap(miss, i*tileSize, j*tileSize, null);
					}else if(tileNum[i][j] == TileNum.HIT){
						canvas.drawBitmap(hit, i*tileSize, j*tileSize, null);
					}else {
						canvas.drawBitmap(destroyed, i*tileSize, j*tileSize, null);
					}
				}
			}
		}	
		if(winState[0]){
			canvas.drawBitmap(p1wins, 0, ((getWidth() / 2) - screenWidth * 0.422f), null);
		}else if(winState[1]){
			canvas.drawBitmap(p2wins, 0, ((getWidth() / 2) - screenWidth * 0.422f), null);
		}else if(game.isPlayer1turn()){
			//			canvas.drawText("Playerone", 0,getWidth() , bright);
			canvas.drawBitmap(player1, (screenWidth - (int)tileSize * 3) / 2, (screenWidth - (int)tileSize * 5) / 2, transparent);
		}else{
			//			canvas.drawText("Playertwo", 0,getWidth() , bright);
			canvas.drawBitmap(player2, (screenWidth - (int)tileSize * 3) / 2, (screenWidth - (int)tileSize * 5) / 2, transparent);
		}
	}

	/**
	 * Helper method to set the selection rectangle at the desired position
	 * @param x - position along the x-axis
	 * @param y - position along the y-axis
	 * @param rect - the rectangle
	 */
	private void getRect(int x, int y, Rect rect) {
		rect.set((int) (x * tileSize), (int) (y * tileSize), 
				(int) (x * tileSize + tileSize), (int) (y * tileSize + tileSize));
	}

	
	/**
	 * Moves the selected rectangle to the desired position, and triggers a redraw
	 * @param x - position along the x-axis
	 * @param y - position along the y-axis
	 */
	private void select(int x, int y) {
		invalidate(selRect);
		selX = Math.min(Math.max(x, 0), boardSize - 1);
		selY = Math.min(Math.max(y, 0), boardSize - 1);
		getRect(selX, selY, selRect);
		Log.d("GameViewer", "Selected rectangle " + selX + " " + selY);
		invalidate(selRect);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);

		boolean[] placedPlats = game.getPlacedPlatforms(); 
		if(placedPlats[0] && placedPlats[1]){//no selecting tiles during platform placement
			select((int) (event.getX() / tileSize), (int) (event.getY() / tileSize));
		}
		return true;
	}

	/**
	 *  Set the size of the view
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(screenWidth, screenWidth);
	}

	/**
	 * @return an {@link ArrayList} of {@link PlatformView}s for the active player.
	 */
	public ArrayList<PlatformView> getPlatformViews() {
		return platformViews;
	}

	/**
	 * Tells the {@link GameViewer} to send the positions of the platforms to the {@link GameController}
	 */
	public void placePlatforms(){
		int[] pos = new int[2];
		boolean isHorizontal;
		for(PlatformView platV:platformViews){
			RelativeLayout.LayoutParams params = (LayoutParams) platV.getLayoutParams();
			pos[0] = (int) (params.leftMargin /tileSize);
			pos[1] = (int)(params.topMargin/tileSize + platV.getPlatform().getlength() -1); 
			if(platV.getRotation() == 0){//upright
				isHorizontal = false;
			}else{//horizontal
				isHorizontal = true;
			}
			platV.getPlatform().setPosition(pos);
			platV.getPlatform().setOrientation(isHorizontal);

			Log.d("positions", "positions, x: "+ pos[0] + " y: " + pos[1]);
		}
		boolean[] placedplats = game.getPlacedPlatforms(); 
		if(!placedplats[0]){
			if(game.setPlatforms(p1Platforms)){
				Log.d("positions",  "p1 was placed");
				switchPlatforms();
			}else{
				Log.d("positions", "p1 has misplaced his platforms");
			}
		}else if (!placedplats[1]){
			if(game.setPlatforms(p2Platforms)){
				switchPlatforms();
				Log.d("positions",  "p2 was placed");
			}else{
				Log.d("positions", "p2 has misplaced his platforms");
			}
		}
	}

	/**
	 * tells the {@link GameViewer} to switch the platforms the {@link PlatformView}s are representing,
	 * if both players have placed their platforms, the PlatformViews will be hiddn, as they are no longer needed
	 */
	public void switchPlatforms(){
		boolean[] placedplats = game.getPlacedPlatforms(); 
		if(placedplats[0] && placedplats[1]){
			for(PlatformView platV:platformViews){
				platV.setVisibility(INVISIBLE);
			}
			Log.d(TAG, "getAndSwitchActive");
			activeBoard = game.getAndSwitchActive();
			game.myActivity.refreshView();
		}else{
			for(int i = 0; i < platformViews.size(); i++){
				int platLength;
				PlatformView platV = platformViews.get(i);
				if(game.isPlayer1turn()){
					Log.d("positions", "switching to p1 plats");
					platLength = (int) (p1Platforms.get(i).getlength() * tileSize);
					platV.setPlatform(p1Platforms.get(i));
				}else{
					Log.d("positions", "switching to p2 plats");
					platV.setPlatform(p2Platforms.get(i));
					platLength = (int) (p2Platforms.get(i).getlength() * tileSize);
				}
				RelativeLayout.LayoutParams params = new LayoutParams((int)tileSize,(platLength));
				platV.setLayoutParams(params);
				platV.setRotation(0);
			}
		}
		invalidate();
	}

	/**
	 * Custom listener applied to the {@link PlatformView}-children of the {@link GameViewer}.
	 * Assigns a custom drawshadowBuilder to those views, and triggers a drag event.
	 * @author Håvard
	 *
	 */
	class MyTouchListener  implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				time = event.getDownTime();
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new PlatformShadowBuilder(v, tileSize);

				v.startDrag(data, shadowBuilder, v, 0);
				return true;
			} else {
				return false;
			}
		}

	}

	/**
	 * only add this listener to views that only have {@link PlatformView} as children.
	 * (A cast makes sure that it will crash otherwise)
	 * @author Håvard
	 *
	 */
	class MyDragListener implements OnDragListener {
		Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
		Drawable normalShape = getResources().getDrawable(R.drawable.shape);

		@Override
		public boolean onDrag(View v, DragEvent event) {
			int action = event.getAction();
			PlatformView view = (PlatformView) event.getLocalState();
			float platLength = view.getPlatform().getlength() * tileSize;
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)tileSize,(int)platLength);

			int x = (int) (event.getX() - event.getX()%tileSize);
			int y = (int) (event.getY() - event.getY()%tileSize);

			switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				// Do nothing
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				Log.d("movement", "drag entered");
				v.setBackground(enterShape);
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				Log.d("movement", "drag exited");
				v.setBackground(normalShape);

				Log.d("movement","event x: " + event.getX() + " y: " + event.getY());
				Log.d("movement","event grid x: " + (int)event.getX()/tileSize + " y: " + (int)event.getY()/tileSize);

				params.leftMargin = Math.max(x, 0);
				params.topMargin = (int) Math.min(Math.max( y, 0), boardSize*tileSize - platLength);

				Log.d("movement","leftmarg: "+params.leftMargin);
				Log.d("movement","topmarg: "+params.topMargin);

				view.setLayoutParams(params);
				break;
			case DragEvent.ACTION_DROP:

				Log.d("movement","event x: " + event.getX() + " y: " + event.getY());
				Log.d("movement","event grid x: " + (int)event.getX()/tileSize + " y: " + (int)event.getY()/tileSize);

				if (view.getRotation() == 0){
					params.leftMargin = Math.max(x, 0);
					params.topMargin = (int) Math.min((int)Math.max( y - (platLength-tileSize), 0), boardSize*tileSize - platLength);
				} else{//platform is rotated
					params.leftMargin = (int) Math.min( x, boardSize*tileSize - platLength);
					params.topMargin = (int) Math.min((int)( y - (platLength-tileSize)), boardSize*tileSize - platLength);
				}

				Log.d("movement","leftmarg: "+params.leftMargin);
				Log.d("movement","topmarg: "+params.topMargin);

				view.setLayoutParams(params);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				long timeDiff = view.getDrawingTime() - time; 
				Log.d("movement","drawingtime minus eventtime: " +timeDiff);
				if( timeDiff< 200){

					if(view.getRotation() == 0){
						view.setRotation(90);
						params = (LayoutParams) view.getLayoutParams();
						params.leftMargin = (int) Math.min(view.getLeft(), (boardSize*tileSize - platLength));
					}else{
						view.setRotation(0);
						params = (LayoutParams) view.getLayoutParams();
						params.topMargin = (int) Math.max(view.getTop(), 0);
					}
					view.setLayoutParams(params);
				}

				v.setBackground(normalShape);
				Log.d("movement", "drag ended");
			default:
				break;
			}
			return true;
		}
	}


	/**
	 * @return the coordinates of the selected square on the board
	 */
	public int[][] getSelected() {
		Log.d("GameViewer", "getSelected " + selX + " " + selY);
		int[][] selected = new int[1][2];
		selected[0][0] = selX;
		selected[0][1] = selY;
		return selected;
	}

}

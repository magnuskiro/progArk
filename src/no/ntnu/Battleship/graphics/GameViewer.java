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

public class GameViewer extends View implements GameListener{

	Resources res;
	Paint background;
	Paint dark;
	Paint bright;
	Paint selected;

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
	Bitmap ship2;
	Bitmap ship3;
	Bitmap ship4;
	Bitmap ship5;

	ArrayList<PlatformView> platformViews;
	public long time;



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

		//loading and scaling the bitmaps
		miss = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.ship_miss_96),
				(int)tileSize, (int)tileSize, false);
		hit = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.ship_hit_96),
				(int)tileSize, (int)tileSize, false);
		destroyed = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.wreck_96),
				(int)tileSize, (int)tileSize, false);
		ship2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.ship2_96),
				(int)tileSize, (int)tileSize * 2, false);
		ship3 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.ship3_96),
				(int)tileSize, (int)tileSize * 3, false);
		ship4 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.ship4_96),
				(int)tileSize, (int)tileSize * 4, false);
		ship5 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.ship5_96),
				(int)tileSize, (int)tileSize * 5, false);




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
		boolean win = true;
		
		activeBoard = game.getAndSwitchActive();
		invalidate();

	}

	@Override
	protected void onDraw(Canvas canvas){
		Log.d("drawing", "drawing");

		// Draw the board...
		canvas.drawRect(0, 0, getWidth(), getWidth() + 50, background);


		// Draw the grid lines
		for (int i = 0; i <= boardSize; i++) {
			canvas.drawLine(0f,(float)( i * tileSize), (float)getWidth(), (float)( i * tileSize), dark);
			canvas.drawLine((float)( i * tileSize), 0f, (float)( i * tileSize), (float)getWidth(), dark);
		}

		if(game.isPlayer1turn()){
			canvas.drawText("Playerone", 0,getWidth() , bright);
		}else{
			canvas.drawText("Playertwo", 0,getWidth() , bright);
		}

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
		}else if(!placedplats[0]){//player one placing mode
			//			TODO: display p1 platforms
		}else{//player two placing mode
			//			TODO: display p2 patforms
		}
	}


	private void getRect(int x, int y, Rect rect) {
		rect.set((int) (x * tileSize), (int) (y * tileSize), 
				(int) (x * tileSize + tileSize), (int) (y * tileSize + tileSize));
	}

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
	 * @return list of platformViews for the active player.
	 */
	public ArrayList<PlatformView> getPlatformViews() {
		return platformViews;
	}

	/**
	 * tells the view to send the positions of the platforms to the gameController
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
			Log.d("positions",  "p1 was placed");
			game.setPlatforms(p1Platforms);			
		}else if (!placedplats[1]){
			Log.d("positions",  "p2 was placed");
			game.setPlatforms(p2Platforms);
		}
		switchPlatforms();
	}

	/**
	 * tells the view to switch the platforms the platformViews are representing
	 */
	public void switchPlatforms(){
		boolean[] placedplats = game.getPlacedPlatforms(); 
		if(placedplats[0] && placedplats[1]){
			for(PlatformView platV:platformViews){
				platV.setVisibility(INVISIBLE);
			}
			Log.d(TAG, "getAndSwitchActive");
			activeBoard = game.getAndSwitchActive();
			invalidate();
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
	}

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
	 * only add this listener to views that only have  platformviews as children.
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

	

	public int[][] getSelected() {
		Log.d("GameViewer", "getSelected " + selX + " " + selY);
		int[][] selected = new int[1][2];
		selected[0][0] = selX;
		selected[0][1] = selY;
		return selected;
	}

}

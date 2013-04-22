package no.ntnu.Battleship.graphics;

import java.util.ArrayList;

import no.ntnu.Battleship.Board;
import no.ntnu.Battleship.Game;
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
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class BoardGraphics extends View implements GameListener{

	Resources res;
	Paint background;
	Paint dark;
	Paint selected;

	private float tileSize;
	private int boardSize;
	private int screenWidth;
	private int screenHeight;
	private int selX;		// X index of selected square
	private int selY;		// Y index of selected square
	private final Rect selRect = new Rect();

	ArrayList<Platform> p1Platforms;
	ArrayList<Platform> p2Platforms;

	Board activeBoard;
	Game game;
	Bitmap miss;
	Bitmap hit;
	Bitmap destroyed;
	Bitmap ship2;
	Bitmap ship3;
	Bitmap ship4;
	Bitmap ship5;

	ArrayList<PlatformView> platformViews;
	public long time;



	public BoardGraphics(int boardSize, int screenWidth, int screenHeight, Context context, Game game) {
		super(context);
		this.tileSize = screenWidth / boardSize;
		this.boardSize = boardSize;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.game = game;
		this.res = getResources();

		game.addListener(this);

		background = new Paint();
		background.setColor(res.getColor(R.color.ocean_blue));
		dark = new Paint();
		dark.setColor(android.graphics.Color.BLACK);
		selected = new Paint();
		selected.setColor(getResources().getColor(R.color.square_selected));

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



		activeBoard = game.getAndSwitchActive();

		p1Platforms = game.getPlatformFactory().createPlatforms();
		p2Platforms = game.getPlatformFactory().createPlatforms();

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

	}

	@Override
	protected void onDraw(Canvas canvas){
		Log.d("drawing", "drawing");

		// Draw the board...
		canvas.drawRect(0, 0, getWidth(), getWidth(), background);


		// Draw the grid lines
		for (int i = 0; i <= boardSize; i++) {
			canvas.drawLine(0f,(float)( i * tileSize), (float)getWidth(), (float)( i * tileSize), dark);
			canvas.drawLine((float)( i * tileSize), 0f, (float)( i * tileSize), (float)getWidth(), dark);
		}

		TileNum[][] tileNum = activeBoard.getTiles();

		// Draw the selection...
		canvas.drawRect(selRect, selected);

		boolean[] placedplats = game.getPlacedPlatforms(); 
		if(placedplats[0] && placedplats[1]){
			Log.d("drawing", "both placed");
			//only after both  players have placed their platforms
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
		invalidate(selRect);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);

		select((int) (event.getX() / tileSize), (int) (event.getY() / tileSize));
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

	class MyTouchListener  implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				time = event.getDownTime();
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
				v.startDrag(data, shadowBuilder, v, 0);
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	class MyDragListener implements OnDragListener {
		Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
		Drawable normalShape = getResources().getDrawable(R.drawable.shape);

		@Override
		public boolean onDrag(View v, DragEvent event) {
			int action = event.getAction();
			View view = (View) event.getLocalState();
			ViewGroup owner = (ViewGroup) view.getParent();
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)tileSize,(int)tileSize*3);

			switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				// Do nothing
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				Log.d("movement", "drag entered");
				v.setBackgroundDrawable(enterShape);
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				Log.d("movement", "drag exited");
				v.setBackgroundDrawable(normalShape);

				Log.d("movement","event x: " + event.getX() + " y: " + event.getY());
				Log.d("movement","event grid x: " + (int)event.getX()/tileSize + " y: " + (int)event.getY()/tileSize);

				params.leftMargin = Math.max((int)( event.getX() - event.getX()%tileSize), 0);
				params.topMargin = (int) Math.min(Math.max((int)( event.getY() - event.getY()%tileSize), 0), 10*tileSize - view.getHeight());

				Log.d("movement",""+params.leftMargin);
				Log.d("movement",""+params.topMargin);

				view.setLayoutParams(params);
				break;
			case DragEvent.ACTION_DROP:

				Log.d("movement","event x: " + event.getX() + " y: " + event.getY());
				Log.d("movement","event grid x: " + (int)event.getX()/tileSize + " y: " + (int)event.getY()/tileSize);

				params.leftMargin = Math.max((int)( event.getX() - event.getX()%tileSize), 0);
				params.topMargin = (int) Math.min(Math.max((int)( event.getY() - event.getY()%tileSize), 0), 10*tileSize - view.getHeight());

				Log.d("movement",""+params.leftMargin);
				Log.d("movement",""+params.topMargin);

				view.setLayoutParams(params);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				if(view.getDrawingTime() - time < 200){
					Log.d("movement","eventtime minus drawingtime: " +(view.getDrawingTime() - time));
					if(view.getRotation() == 0){
						view.setRotation(90);
					}else{
						view.setRotation(0);
					}
				}

				v.setBackgroundDrawable(normalShape);
				Log.d("movement", "drag ended");
			default:
				break;
			}
			return true;
		}
	}

}

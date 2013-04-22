package no.ntnu.Battleship.graphics;

import java.lang.ref.WeakReference;
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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
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
				DragShadowBuilder shadowBuilder = new MyDragShadowBuilder(v, tileSize);

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
			ViewGroup owner = (ViewGroup) view.getParent();
			float platLength = view.getPlatform().getlength() * tileSize;
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)tileSize,(int)platLength);

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
				params.topMargin = (int) Math.min(Math.max((int)( event.getY() - event.getY()%tileSize), 0), boardSize*tileSize - platLength);

				Log.d("movement","leftmarg: "+params.leftMargin);
				Log.d("movement","topmarg: "+params.topMargin);

				view.setLayoutParams(params);
				break;
			case DragEvent.ACTION_DROP:

				Log.d("movement","event x: " + event.getX() + " y: " + event.getY());
				Log.d("movement","event grid x: " + (int)event.getX()/tileSize + " y: " + (int)event.getY()/tileSize);

				if (view.getRotation() == 0){
					params.leftMargin = Math.max((int)( event.getX() - event.getX()%tileSize), 0);
					params.topMargin = (int) Math.min((int)Math.max( event.getY() - (event.getY()%tileSize) - (platLength-tileSize), 0), boardSize*tileSize - platLength);
				} else{//platform is rotated
					params.leftMargin = (int) Math.min((int)( event.getX() - event.getX()%tileSize), boardSize*tileSize - platLength);
					params.topMargin = (int) Math.min((int)( event.getY() - event.getY()%tileSize - (platLength-tileSize)), boardSize*tileSize - platLength);
					Log.d("movement", "rotated y-pos: min(" + (event.getY() - event.getY()%tileSize - (platLength-tileSize))+ "," + (boardSize*tileSize-platLength) +")");
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

	static 	class MyDragShadowBuilder extends View.DragShadowBuilder{
		private final WeakReference<View> mView;
		private float tSize;

		public MyDragShadowBuilder(View v, float tSize) {
            mView = new WeakReference<View>(v);
			this.tSize = tSize;
		}

		
		@Override
		public void onProvideShadowMetrics (Point size, Point touch){
			final View view = mView.get();
			if (view != null) {
				size.set(view.getWidth(), view.getHeight());
				touch.set(size.x / 2, (int) (size.y - (tSize/2)));
			} else {
				Log.e(View.VIEW_LOG_TAG, "Asked for drag thumb metrics but no view");
			}
		}
		
		
		@Override
		public void onDrawShadow(Canvas canvas) {
            final View view = mView.get();
            if (view != null) {
                view.draw(canvas);
            } else {
                Log.e(View.VIEW_LOG_TAG, "Asked to draw drag shadow but no view");
            }
        }


	}

}

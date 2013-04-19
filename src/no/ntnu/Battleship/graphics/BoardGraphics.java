package no.ntnu.Battleship.graphics;

import java.util.ArrayList;

import no.ntnu.Battleship.Board;
import no.ntnu.Battleship.Game;
import no.ntnu.Battleship.GameListener;
import no.ntnu.Battleship.Platform;
import no.ntnu.Battleship.R;
import no.ntnu.Battleship.TileNum;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BoardGraphics extends View implements GameListener{

	Resources res;
	private Paint paint;
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

	//	private ArrayList<Rect> boardTiles;
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



	public BoardGraphics(int boardSize, int screenWidth, int screenHeight, Context context, Game game) {
		super(context);
		this.tileSize = screenWidth / boardSize;
		this.boardSize = boardSize;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.game = game;
		this.res = getResources();

		game.addListener(this);

		paint = new Paint();
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


		//		boardTiles = createBoardTiles(boardSize, tileSize);

		activeBoard = game.getAndSwitchActive();

		p1Platforms = game.getPlatformFactory().createPlatforms();
		p2Platforms = game.getPlatformFactory().createPlatforms();
		
		platformViews = new ArrayList<PlatformView>();
		for (Platform plat:p1Platforms){
			platformViews.add(new PlatformView(context, plat, res, tileSize));
			
		}
		
	}


	/*
	public void drawBoard(Canvas canvas) {

		// Hvis partall kolonner må første tile i hver rad ha samme farge som
		// siste tile i forrige rad
		boolean evenNumberOfRows = false;
		int columnCounter = 0;
		if (boardSize % 2 == 0) {
			evenNumberOfRows = true;
		}

		TileNum[][] tileNum = activeBoard.getTiles();

		// Holder orden på at annenhver tile er grå/mørkegrå
		boolean evenOrOdd = true;
		// Setter paintstylen til fill
		paint.setStyle(Style.FILL);
		// Tegner hver tile
		int i = 0;
		for (Rect tile : boardTiles) {
			if (evenOrOdd) {
				paint.setColor(getResources().getColor(R.color.blue_wave));
			} else {
				paint.setColor(getResources().getColor(R.color.ocean_blue));
			}
			if(tileNum[i%boardSize][i/boardSize] == TileNum.EMPTY){

				// Mer annenhver stuff
				// Tilen tegnes
				canvas.drawRect(tile, paint);
			}else if(tileNum[i%boardSize][i/boardSize] == TileNum.MISS){
				canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ship_miss_96), null, tile, paint);
			}else if(tileNum[i%boardSize][i/boardSize] == TileNum.HIT){
				canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ship_hit_96), null, tile, paint);
			}else {
				canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.wreck_96), null, tile, paint);
			}
			// Annenhvertelleren oppdateres
			if (evenNumberOfRows && columnCounter == boardSize - 1) {
				columnCounter = 0;
			} else {
				evenOrOdd = !evenOrOdd;
				columnCounter++;
			}
			i++;
		}
	}



	public void drawIndexes(Canvas canvas) {
		paint.setStyle(Style.FILL);
		paint.setColor(Color.RED);

		int displacementFromTop = screenHeight - screenWidth + (int) tileSize;

		int displacementFromEdge = (int) (0);

		paint.setTextSize((float) tileSize - 2);

		int topX;
		int topY;
		int left;
		int top;
		int right;
		int bot;

		Rect drawWithin = new Rect();

		for (int i = 0; i < boardSize; i++) {


			// Koordinatene for bokstaver (rad)
			topX = (int) (i * tileSize + tileSize) + displacementFromEdge;
			topY = displacementFromTop + displacementFromEdge;
			// bruk charcode
			canvas.drawText("" + Character.valueOf((char) ('A' + i)), topX,
					topY, paint);

			// Koordinatene for siffer (kolonne)
			topX = 0 + displacementFromEdge;
			topY = (int) (i * tileSize + tileSize + displacementFromTop)
					+ displacementFromEdge;
			canvas.drawText("" + (i + 1), topX, topY, paint);
		}
	}
	 */


	/**
	 * Lager en arraylist med tiles
	 * 
	 * @param boardSize
	 *            Størrelsen på brettet, viktig for å vite plasseringer i rader
	 *            og kolonner
	 * @param tileSize
	 *            Størrelsen på siden til hver enkelt tile (de er firkantede)
	 * @return En ny arraylist med tilse som rectangles, alle med hver sin x1,
	 *         y1, x2, y2 kordinat ferdig sat
	 */
	private ArrayList<Rect> createBoardTiles(int boardSize, double tileSize) {
		// Matrisen med alle tilsene som vi returnerer
		ArrayList<Rect> tiles = new ArrayList<Rect>();

		int displacementFromTop = (int) tileSize;

		// Fyllingen av tiles
		for (int i = 0; i < boardSize * boardSize; i++) {
			// Lager ett nytt rect
			Rect tile = new Rect();

			// Finner posisjonen til tilen i raden, aka kolonnen dens (til
			// venstre side, ikke midten eller høyre)
			int positionInRow = i % boardSize;

			// Finner raden til tilen, aka topstreken, ikke midten eller under
			int row = (int) (Math.floor(i / boardSize));

			int left = (int) (/* positionInRow + */positionInRow * tileSize);
			int top = (int) (/* row + */row * tileSize);
			int right = (int) (/* positionInRow + */left + tileSize);
			int bot = (int) (/* row + */top + tileSize);

			// Forskyver tiles så brettet kommer nederst på skjermen
			left += tileSize;
			top += displacementFromTop;
			right += tileSize;
			bot += displacementFromTop;

			// Setter left, top, right, bottom
			tile.set(left, top, right, bot);

			// Legger tilen til arraylisten som returneres
			tiles.add(tile);
		}
		return tiles;
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


}

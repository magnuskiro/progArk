package no.ntnu.Battleship.graphics;

import java.util.ArrayList;

import no.ntnu.Battleship.Board;
import no.ntnu.Battleship.Game;
import no.ntnu.Battleship.GameListener;
import no.ntnu.Battleship.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.view.View;

public class BoardGraphics extends View implements GameListener{

	private Paint paint;

	private double tileSize;
	private int boardSize;
	private int screenWidth;
	private int screenHeight;

	private ArrayList<Rect> boardTiles;

	Board activeBoard;
	Game game;
	
	
	public BoardGraphics(int boardSize, int screenWidth, int screenHeight, Context context, Game game) {
		super(context);
		// this.tileSize = Math.floor(screenWidth / (boardSize + 2));
		this.tileSize = screenWidth / (boardSize + 2);
		this.boardSize = boardSize;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.game = game;
		
		game.addListener(this);

		paint = new Paint();

		boardTiles = createBoardTiles(boardSize, tileSize);
		
		activeBoard = game.getAndSwitchActive();
	}

	public void drawBoard(Canvas canvas) {

		// Hvis partall kolonner må første tile i hver rad ha samme farge som
		// siste tile i forrige rad
		boolean evenNumberOfRows = false;
		int columnCounter = 0;
		if (boardSize % 2 == 0) {
			evenNumberOfRows = true;
		}

		// Holder orden på at annenhver tile er grå/mørkegrå
		boolean evenOrOdd = true;
		// Setter paintstylen til fill
		paint.setStyle(Style.FILL);
		// Tegner hver tile
		for (Rect tile : boardTiles) {
			// Mer annenhver stuff
			if (evenOrOdd) {
				paint.setColor(getResources().getColor(R.color.blue_wave));
			} else {
				paint.setColor(getResources().getColor(R.color.ocean_blue));
			}
			// Tilen tegnes
			canvas.drawRect(tile, paint);
			// Annenhvertelleren oppdateres
			if (evenNumberOfRows && columnCounter == boardSize - 1) {
				columnCounter = 0;
			} else {
				evenOrOdd = !evenOrOdd;
				columnCounter++;
			}
		}
	}

	// public void drawIndexes(Canvas canvas) {
	// paint.setStyle(Style.FILL);
	// paint.setColor(Color.GRAY);
	//
	// int displacementFromTop = screenHeight - screenWidth + (int) tileSize;
	//
	// int displacementFromEdge = (int) (0);
	//
	// paint.setTextSize((float) tileSize-2);
	//
	// int topX;
	// int topY;
	//
	// for (int i = 0; i < boardSize; i++) {
	// // Koordinatene for bokstaver (rad)
	// topX = (int) (i * tileSize + tileSize) + displacementFromEdge;
	// topY = displacementFromTop + displacementFromEdge;
	// // bruk charcode
	// canvas.drawText("" + i, topX, topY, paint);
	//
	// // Koordinatene for siffer (kolonne)
	// topX = 0 + displacementFromEdge;
	// topY = (int) (i * tileSize + tileSize + displacementFromTop) +
	// displacementFromEdge;
	// canvas.drawText("" + i, topX, topY, paint);
	// }
	// }

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

			// //rad
			// left = (int) (i*tileSize + tileSize);
			// top = displacementFromTop;
			// right = (int) (left + tileSize);
			// bot = (int) (top + tileSize);
			// drawWithin.set(left, top, right, bot);
			// paint.setColor(Color.GREEN);
			// canvas.drawRect(drawWithin, paint);
			// paint.setColor(Color.RED);
			// canvas.drawText("" + i, drawWithin.left, drawWithin.top, paint);

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

	/*
	 * int positionInRow = i % boardSize;
	 * 
	 * // Finner raden til tilen, aka topstreken, ikke midten eller under int
	 * row = (int) (Math.floor(i / boardSize));
	 * 
	 * int displacementFromTop = screenHeight - screenWidth + (int)tileSize;
	 * 
	 * int left = (int) (positionInRow + positionInRow * tileSize); int top =
	 * (int) (row + row * tileSize); int right = (int) (positionInRow +
	 * positionInRow * tileSize + tileSize); int bot = (int) (row + row *
	 * tileSize + tileSize);
	 * 
	 * 
	 * 
	 * paint.setColor(Color.RED); paint.setTextSize(10); c.drawText("Score: " +
	 * topScore, topBarRect.left + 1, topBarRect.bottom - 1, paint);
	 * c.drawText("Score: " + botScore, botBarRect.left + 1, botBarRect.bottom -
	 * 1, paint);
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
		drawBoard(canvas);
	}
	
	
}

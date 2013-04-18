package no.ntnu.Battleship.graphics;

import java.util.ArrayList;

import no.ntnu.Battleship.Platform;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class PlatformAddingGraphics {
	
	private Paint paint;

	private double tileSize;
	private int boardSize;
	private int screenWidth;
	private int screenHeight;
	
	private ArrayList<ArrayList<Rect>> platformTiles;
	private ArrayList<Platform> platforms;
	
	
	public PlatformAddingGraphics(int boardSize, int screenWidth, int screenHeight, ArrayList<Platform> platforms){
		this.boardSize = boardSize;
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.platforms = platforms;
		
		tileSize = screenWidth / (boardSize + 2);
		
		platformTiles = makePlatformTiles();
		
		paint = new Paint();
	}
	
	
	public void drawPlatforms(Canvas canvas){
		paint.setColor(Color.GREEN);
		paint.setStyle(Style.FILL);
		for (ArrayList<Rect> platform : platformTiles) {
			for (Rect tile : platform) {
				canvas.drawRect(tile, paint);
			}
		}
		
		
	}
	
	
	
	
	private ArrayList<ArrayList<Rect>> makePlatformTiles(){
		ArrayList<ArrayList<Rect>> platformTiles = new ArrayList<ArrayList<Rect>>();
		ArrayList<Rect> onePlatformsTiles;
		Rect tile;
		int length;
		
		int left;
		int top;
		int right;
		int bot;
		
		for (Platform platform : platforms) {
			onePlatformsTiles = new ArrayList<Rect>();
			length = platform.getlength();
			for (int i = 0; i < length; i++) {
				left = (int) ( (platformTiles.size()) * (tileSize + 10) + 10);
				top = (int) (i*tileSize);
				right = (int) (left + tileSize);
				bot = (int) (top + tileSize);
				tile = new Rect(left, top, right, bot);
				onePlatformsTiles.add(tile);
			}
			
			platformTiles.add(onePlatformsTiles);
		}
		
		return platformTiles;
	}


	public void handleClicked(int x, int y) {
		// TODO Auto-generated method stub
		for (ArrayList<Rect> platform: platformTiles) {
			for (Rect tile : platform) {
				//Sjekker om trykket er innenfor en platform
				if(tile.contains(x, y)){
					//Hvis platformen er horisontal
					if(platform.get(0).top == platform.get(1).top){
						for (int i = 0; i < platform.size(); i++) {
							platform.get(i).left += i * tileSize;
							platform.get(i).top += i * tileSize;
							platform.get(i).right += i * tileSize;
							platform.get(i).bottom += i * tileSize;
						}						
					}
					//Ellers hvis platformen er vertikal
					else{
						for (int i = 0; i < platform.size(); i++) {
							platform.get(i).left -= i * tileSize;
							platform.get(i).top -= i * tileSize;
							platform.get(i).right -= i * tileSize;
							platform.get(i).bottom -= i * tileSize;
						}	
					}
					//Vi har snudd, avbryt all videre sjekking
					return;
				}
			}
		}
	}


	public void handleMove(int x, int y) {
		// TODO Auto-generated method stub
		for (ArrayList<Rect> platform: platformTiles) {
			for (Rect tile : platform) {
				//Sjekker om trykket er innenfor en platform
				if(tile.contains(x, y)){
					tile.left = (int) (x - (tileSize/2));
					tile.top = (int) (y - (tileSize/2));
					tile.right = (int) (x + (tileSize/2));
					tile.bottom = (int) (y + (tileSize/2));
					//Hvis platformen er horisontal
					int index = platform.indexOf(tile);
					if(platform.get(0).top == platform.get(1).top){
						for (int i = 0; i < platform.size(); i++) {
							platform.get(i).left = (int) (-(index - i) * tileSize + tile.left);
							platform.get(i).top = tile.top;
							platform.get(i).right = (int) (-(index - i) * tileSize + tile.right);
							platform.get(i).bottom = tile.bottom;
						}						
					}
					//Ellers hvis platformen er vertikal
					else{
						for (int i = 0; i < platform.size(); i++) {
							platform.get(i).left = tile.left;
							platform.get(i).top = (int) (-(index - i) * tileSize + tile.top);;
							platform.get(i).right = tile.right;
							platform.get(i).bottom = (int) (-(index - i) * tileSize + tile.bottom);
						}	
					}
					//Vi har snudd, avbryt all videre sjekking
					return;
				}
			}
		}
	}
	
	
	
}







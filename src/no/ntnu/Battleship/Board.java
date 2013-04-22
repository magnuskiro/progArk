package no.ntnu.Battleship;

import java.util.ArrayList;

public class Board {
	
	private final int boardSize;
	
	private boolean[][] isShot;
	private Platform[][] platformGrid;
	private ArrayList<Platform> platforms;
	Game myGame;
	
	public Board(Game thisGame, int size) {
		boardSize = size;
		isShot = new boolean[boardSize][boardSize];
		platformGrid = new Platform[boardSize][boardSize];
		platforms = new ArrayList<Platform>();
		this.myGame = thisGame;
	}
	
	public void populate(ArrayList<Platform> newPlatforms) {
		//populate platformGrid!
		int[][] loc;
		for(Platform plat:newPlatforms) {
			loc = plat.getPosition();
			for(int i = 0; i<loc.length; i++) {
				platformGrid[loc[i][0]][loc[i][1]] = plat;
			}
		}
		this.platforms = newPlatforms;
	}
	
	public void attack(int[][] shots) {
		for(int i = 0; i<shots.length; i++) {
			isShot[shots[i][0]][shots[i][1]] = true;
			if(platformGrid[shots[i][0]][shots[i][1]] != null) {
				platformGrid[shots[i][0]][shots[i][1]].attack(shots[i]);
			}
		}
		myGame.updateShots(getShots());
		myGame.fireGameChanged();
	}
	
	public int getShots() {
		int countShots = 0;
		for(Platform plat:platforms) {
			countShots = plat.getShots();
		}
		return countShots;
	}
	
	public TileNum[][] getTiles() {
		TileNum[][] ret = new TileNum[boardSize][boardSize];
		for (int i = 0; i<boardSize; i++) {
			for (int j = 0; j<boardSize; j++) {
				if(!isShot[i][j]) {
					ret[i][j] = TileNum.EMPTY;
				} else if(platformGrid[i][j] == null) {
					ret[i][j] = TileNum.MISS;
				} else { //Platform is HIT!
					if(platformGrid[i][j].isDestroyed()) {
						ret[i][j] = TileNum.DESTROYED;
					} else {
						ret[i][j] = TileNum.HIT;
					}
				}
			}
		}
		return ret;
	}
	
	public int getBoardSize() {
		return boardSize;
	}
	
}


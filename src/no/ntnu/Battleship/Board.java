package no.ntnu.Battleship;

import java.util.ArrayList;

import android.R.plurals;
import android.util.Log;

/**
 * Class representing one player's half of the complete game board.
 * Can have various sizes, but is always a square
 * @author Stig Tore
 *
 */
public class Board {
	
	private final int boardSize;
	private int DESTROYED = 1;
	private int MISS = 2;
	private int HIT = 3;
	
	private boolean[][] isShot;
	private Platform[][] platformGrid;
	private ArrayList<Platform> platforms;
	GameController myGame;
	
	/**
	 * Main constructor
	 * @param thisGame - the {@link GameController} that  manages this board
	 * @param size - the size of this board
	 */
	public Board(GameController thisGame, int size) {
		boardSize = size;
		isShot = new boolean[boardSize][boardSize];
		platformGrid = new Platform[boardSize][boardSize];
		platforms = new ArrayList<Platform>();
		this.myGame = thisGame;
	}
	
	/**
	 * Attempts to populate this board with an {@link ArrayList} of {@link Platform}s
	 * @param newPlatforms - list of the {@link Platform}s to populate the board
	 * @return - whether or not the populating was successful
	 */
	public boolean populate(ArrayList<Platform> newPlatforms) {
		//populate platformGrid!
		boolean sucess = true;
		int[][] loc;
		for(Platform plat:newPlatforms) {
			loc = plat.getPosition();
			for(int i = 0; i<loc.length; i++) {
				if(platformGrid[loc[i][0]][loc[i][1]] == null){
				platformGrid[loc[i][0]][loc[i][1]] = plat;
				}else{
					sucess = false;
					break;
				}
			}
		}
		this.platforms = newPlatforms;
		
		return sucess;
	}
	
	/**
	 * Tells the board that it has been shot at the provided coordinates.
	 * Will check those coordinates for platforms, and if there is one, tells it
	 * thatis has been hit.
	 * @param shots - the coordinates of the shots
	 */
	public void attack(int[][] shots) {
		Log.d("Board", "attack at " + shots[0][0] + " " + shots[0][1]);
		for(int i = 0; i<shots.length; i++) {
			isShot[shots[i][0]][shots[i][1]] = true;
			if(platformGrid[shots[i][0]][shots[i][1]] != null) {
				platformGrid[shots[i][0]][shots[i][1]].hit(shots[i]);
				if (platformGrid[shots[i][0]][shots[i][1]].isDestroyed()) {
					myGame.playSound(DESTROYED);
				} else {
					myGame.playSound(HIT);
				}
			} else {
				myGame.playSound(MISS);
			}
		}
		myGame.updateShots(getShots());
		myGame.fireGameChanged();
	}
	
	/**
	 * If the number of shots available per turn is determined by the number of
	 * {@link Platform}s on the board that are not destroyed, returns that number.
	 * @return - the number of shots this board can deliver in one turn 
	 */
	public int getShots() {
		int countShots = 0;
		for(Platform plat:platforms) {
			countShots = plat.getShots();
		}
		return countShots;
	}
	
	/**
	 * 
	 * @return an enumerated array, representing the status of the board, as it would be seen
	 * by the opponent
	 */
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
	
	/**
	 * 
	 * @return the size of this board, in squares along one side
	 */
	public int getBoardSize() {
		return boardSize;
	}
	
	/**
	 * @return whether or not all platforms on this board are destroyed.
	 */
	public boolean isAllDestroyed(){
	boolean d = true;
	
	for(Platform p : platforms) {
		if (!p.isDestroyed()){
			d = false;
		}
	}
	
	return d;
	}
	
}


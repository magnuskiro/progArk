package no.ntnu.Battleship;

import java.util.ArrayList;

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

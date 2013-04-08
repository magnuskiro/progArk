package no.ntnu.Battleship;

import java.util.ArrayList;

public class Board {
	private final int sizex = 10;
	private final int sizey = 10;
	
	private boolean[][] isShot;
	private boolean[][] isPlatform;
	private ArrayList<Platform> platforms;
	
	public Board() {
		isShot = new boolean[sizex][sizey];
		isPlatform = new boolean[sizex][sizey];
		platforms = new ArrayList<Platform>();
	}
	
	
	
}

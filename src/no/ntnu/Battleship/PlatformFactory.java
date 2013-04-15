package no.ntnu.Battleship;

import java.util.ArrayList;

import no.ntnu.Battleship.platforms.Ship;

/**
 * Used to easily create a set of platforms
 * @author Håvard
 *
 */
public class PlatformFactory {
	
	public PlatformFactory(){
		//TODO: fetch settings from somewhere
	}
	
	/**
	 * creates the set of platforms required for one player
	 * @return
	 */
	public ArrayList<Platform> createPlatforms() {
		ArrayList<Platform> platforms = new ArrayList<Platform>();
		
//		TODO: use  settings to affect creation
		
		platforms.add(new Ship(5));
		platforms.add(new Ship(4));
		platforms.add(new Ship(3));
		platforms.add(new Ship(3));
		platforms.add(new Ship(2));
		
		return platforms;
	}

}

package no.ntnu.Battleship;

import java.util.ArrayList;


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
		
		platforms.add(new Platform(5));
		platforms.add(new Platform(4));
		platforms.add(new Platform(3));
		platforms.add(new Platform(3));
		platforms.add(new Platform(2));
		
		return platforms;
	}

}

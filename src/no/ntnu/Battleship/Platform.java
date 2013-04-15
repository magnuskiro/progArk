package no.ntnu.Battleship;

/**
 * Each unit on the board is represented by a platform. Each platform has one or more devices,
 * depending on its size. 
 * @author Håvard
 *
 */
public interface Platform {

	/**
	 * Tells the platform to fire at the specified location
	 * @param coordinates
	 */
	void attack(int[] coordinates);
	
	/**
	 * Lets user select which weapon the platform will use, if such a mode is used. 
	 */
	void chooseWeapon();
	
	/**
	 * Returns number of available shots
	 * may be merged with getShots()
	 * @return
	 */
	int getWeaponShots();
	
	/**
	 * Returns number of available shots
	 * may be merged with getWeaponShots()
	 * @return
	 */
	int getShots();
	
	/**
	 * Returns the position of the front-part of the platform
	 * @return
	 */
	int[] getCoordinates();
	
	/**
	 * Returns the length of the platform
	 * @return
	 */
	int getlength();
	
	
}

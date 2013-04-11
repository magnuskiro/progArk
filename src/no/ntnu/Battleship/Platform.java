package no.ntnu.Battleship;

public interface Platform {

	void attack();
	
	void chooseWeapon();
	
	int getWeaponShots();
	
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

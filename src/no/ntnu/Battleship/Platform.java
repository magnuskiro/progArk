package no.ntnu.Battleship;

public interface Platform {

	void attack();
	
	void chooseWeapon();
	
	int getWeaponShots();
	
	int getShots();
	
	int[] getCoordinates();
	
	
}

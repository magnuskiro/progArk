package no.ntnu.Battleship;

public interface Device {
	
	/**
	 * Returns the damage-radius of each projectile
	 * @return
	 */
	public int getAoE();
	
	/**
	 * Returns the number of projectiles
	 * @return
	 */
	public int getProjectiles();
	
	/**
	 * Returns the distance between each projectile
	 * @return
	 */
	public int getSpread();
	
	/**
	 * Returns the spread-pattern
	 * @return
	 */
	public int getSpreadType();

}

package no.ntnu.Battleship;

/**
 * All platforms are equipped with one ore more device, depending on platform size.
 * @author Håvard
 *
 */
public class Device {
	
	private int type;
	private int aoe;
	private int projectiles;
	private int spread;
	private int spreadType;
	
	/**
	 * 
	 * @param type - type, as defined in settings
	 */
	public Device(int type) {
		this.type = type;
	}

	/**
	 * Returns the damage-radius of each projectile
	 * @return
	 */
	public int getAoE() {
		return aoe;
	}
	
	/**
	 * Returns the number of projectiles
	 * @return
	 */
	public int getProjectiles() {
		return projectiles;
	}
	
	/**
	 * Returns the distance between each projectile
	 * @return
	 */
	public int getSpread() {
		return spread;
	}
	
	/**
	 * Returns the spread-pattern
	 * @return
	 */
	public int getSpreadType() {
		return spreadType;
	}
	
	/**
	 * 
	 * @return the type of device
	 */
	public int getType(){
		return type;
	}

}

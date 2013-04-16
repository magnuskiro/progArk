package no.ntnu.Battleship;

/**
 * Each unit on the board is represented by a platform. Each platform has one or more devices,
 * depending on its size. 
 * @author Håvard
 *
 */
public class Platform {

	private int			length;
	private int[]		coordinates;
	private int 		numberOfDevices; //how many devices are currently attached
	private Device[]	weapons; //what weapon is at what position (index)
	private boolean[]	hits; //which parts of the ship has been hit
	
	
	public Platform(int length){
		this.length = length;
	}
	
	/**
	 * Tells the platform to fire at the specified location
	 * @param coordinates
	 */
	void attack(int[] coordinates){
		
	}
	
	/**
	 * Lets user select which weapon the platform will use, if such a mode is used. 
	 */
	void chooseWeapon() {
	}
	
	/**
	 * Returns number of available shots
	 * may be merged with getShots()
	 * @return
	 */
	int getWeaponShots() {
		return 0;
	}
	
	/**
	 * Returns number of available shots
	 * may be merged with getWeaponShots()
	 * @return
	 */
	int getShots() {
		return 0;
	}
	
	/**
	 * Returns the position of the front-part of the platform
	 * @return
	 */
	int[] getCoordinates() {
		return coordinates;
	}
	
	/**
	 * Returns the length of the platform
	 * @return
	 */
	int getlength() {
		return length;
	}
	
	/**
	 * Adds a device to the platform if there is room.
	 * if the platform is full, nothing happens. 
	 * @param device
	 * @return whether or not the device was added
	 */
	boolean addDevice(Device device) {
		if(numberOfDevices < length){
			
		}
		return false;
	}
	
	/**
	 * removes the first device matching the provided type
	 * 
	 * @param device
	 * @return whether or not the device was removed
	 */
	boolean removeDevice(Device device) {
		return false;
	}
	
}

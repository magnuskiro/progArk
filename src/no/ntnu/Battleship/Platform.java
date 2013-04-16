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
	private int			orientation; //nesw-1234
	private int 		numberOfDevices; //how many devices are currently attached
	private Device[]	devices; //what weapon is at what position (index)
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
		boolean added = false;
		if(numberOfDevices < length){
			for(int i = 0; i < devices.length && !added; i++){
				if( devices[i] == null){
					devices[i] = device;
					added = true;
				}
			}
		}
		return added;
	}

	/**
	 * removes the first device matching the provided type
	 * 
	 * @param device
	 * @return whether or not the device was removed
	 */
	boolean removeDevice(Device device) {
		boolean removed = false;
		for(int i = 0; i < devices.length && !removed; i++){
			if( devices[i] == device){
				devices[i] = null;
				removed = true;
			}
		}
		return removed;
	}


	/**
	 * nesw-1234
	 * @return
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * nesw-1234
	 * @param orientation
	 */
	public void setOrientation(int orientation) {
		if(orientation <5 && 0 < orientation){
			this.orientation = orientation;
		}
	}

}
package no.ntnu.Battleship;

/**
 * Each unit on the board is represented by a platform. Each platform has one or more devices,
 * depending on its size. 
 * @author Håvard
 *
 */
public class Platform {

	private int			length;
	private int[][]		coordinates;
	private boolean		isHorizontal;
	private int 		numberOfDevices; //how many devices are currently attached
	private Device[]	devices; //what weapon is at what position (index)
	private boolean[]	hits; //which parts of the ship has been hit, true=hit


	public Platform(int length){
		this.length = length;
		coordinates = new int[length][2];
		setPosition(new int[] {0, 5});
		isHorizontal = false;
		hits = new boolean[length];
	}

	/**
	 * Tells the platform to fire at the specified location
	 * @param coordinates
	 */
	public void attack(int[] coordinates){
		//TODO
	}

	/**
	 * Lets user select which weapon the platform will use, if such a mode is used. 
	 */
	public void chooseWeapon() {
		//TODO
	}

	/**
	 * Returns number of available shots
	 * may be merged with getShots()
	 * @return
	 */
	public int getWeaponShots() {
		//TODO
		return 0;
	}

	/**
	 * Returns number of available shots
	 * may be merged with getWeaponShots()
	 * @return
	 */
	public int getShots() {
		//TODO
		return 0;
	}

	/**
	 * Returns the position of the front-part of the platform
	 * @return
	 */
	public int[][] getPosition() {
		return coordinates;
	}

	/**
	 * Set the position of the platform, will calculate based on whether or not
	 * it is horizontal
	 * @param pos - position of ass
	 */
	public void setPosition(int[] pos){
		coordinates[0][0] = pos[0];
		coordinates[0][1] = pos[1];
		updateCoordinates();
	}

	/**
	 * Returns the length of the platform
	 * @return
	 */
	public int getlength() {
		return length;
	}

	/**
	 * Adds a device to the platform if there is room.
	 * if the platform is full, nothing happens. 
	 * @param device
	 * @return whether or not the device was added
	 */
	public boolean addDevice(Device device) {
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
	public boolean removeDevice(Device device) {
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
	 * @return
	 */
	public boolean isHorizontal() {
		return isHorizontal;
	}

	/**
	 * @param isHorizontal
	 */
	public void setOrientation(boolean isHorizontal) {
		this.isHorizontal = isHorizontal;
		updateCoordinates();
	}

	/**
	 * shared functionality used when updating position or orientation.
	 */
	private void updateCoordinates() {
		for (int i = 1; i < length; i++){
			if(isHorizontal){
				coordinates[i][0] = coordinates[0][0] + i;
				coordinates[i][1] = coordinates[0][1];
			}else{
				coordinates[i][0] = coordinates[0][0];
				coordinates[i][1] = coordinates[0][1] - i;
			}
		}
	}

	/**
	 * tells the platform that it has been hit at the provided coordinate
	 * @param position
	 */
	public void hit(int[] position){
		for (int i = 0; i < length; i++){
			if(position[0] == coordinates[i][0] && position[1] == coordinates[i][1]){
				hits[i] = true;
			}
		}
	}


	/**
	 * Checks whether the platform is still alive
	 * @return
	 */
	public boolean isDestroyed(){
		boolean d = true;

		for (int i = 0; i <  hits.length; i++){
			if(!hits[i]){
				d = false;
			}
		}

		return d;
	}
}
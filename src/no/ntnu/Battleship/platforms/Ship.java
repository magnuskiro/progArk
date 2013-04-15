package no.ntnu.Battleship.platforms;

import no.ntnu.Battleship.Device;
import no.ntnu.Battleship.DeviceFactory;
import no.ntnu.Battleship.Platform;

public class Ship implements Platform{

	private int		length;
	private int[]	coordinates;
	private Device[]	weapons;//what weapon is at  what position (index)
	private boolean[]	hits;//which parts of the ship has been hit
	
	
	public Ship(int length){
		this.length = length;
	}

	@Override
	public void attack(int[] coordinates) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void chooseWeapon() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getWeaponShots() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getShots() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getCoordinates() {
		return coordinates;
	}

	@Override
	public int getlength() {
		return length;
	}

	@Override
	public boolean addDevice(Device device) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeDevice(Device device) {
		// TODO Auto-generated method stub
		return false;
	}

	

}

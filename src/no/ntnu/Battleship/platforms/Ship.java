package no.ntnu.Battleship.platforms;

import no.ntnu.Battleship.Platform;

public class Ship implements Platform{

	private int		length;
	private int[]	coordinates;
	private int[]	weaponPos;//what weapontype (int) is at  what position (index)
	private boolean[]	hits;//what parts of the ship has been hit

	@Override
	public void attack() {
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

}

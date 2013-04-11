package no.ntnu.Battleship.platforms;

import no.ntnu.Battleship.Platform;

public class Ship implements Platform{

	private int length;
	private int[] coordinates;

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

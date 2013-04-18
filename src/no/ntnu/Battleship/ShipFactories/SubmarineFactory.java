package no.ntnu.Battleship.ShipFactories;

import java.util.ArrayList;

import no.ntnu.Battleship.AbstractShipFactory;

public class SubmarineFactory implements AbstractShipFactory{

	private String type = "Submarine";
	private int size = 3;
	private ArrayList<int[]> coordinates;
	
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public ArrayList<int[]> getCoordinates() {
		// TODO Auto-generated method stub
		return coordinates;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public ArrayList<Object> getDevices() {
		// TODO Auto-generated method stub
		return null;
	}

}

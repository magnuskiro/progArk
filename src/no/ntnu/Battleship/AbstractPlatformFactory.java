package no.ntnu.Battleship;

import java.util.ArrayList;

public interface AbstractPlatformFactory {
	public int getSize();
	public ArrayList<int[]> getCoordinates();
	public String getType();
}

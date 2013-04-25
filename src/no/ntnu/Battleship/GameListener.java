package no.ntnu.Battleship;

/**
 * Implement this to be able to listen to changes in a {@link GameController}
 */
public interface GameListener {
	/**
	 * called when  this listener's {@link GameController} triggers a change-event
	 */
	public void gameChanged();
}

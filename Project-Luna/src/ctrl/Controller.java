package ctrl;

import model.Mission;

import org.lwjgl.util.vector.Vector2f;

/**
 * 
 * @version 1.00
 * <br><br>
 * 
 * Main controller class for the whole program. This is
 * where everything makes it or breaks it. Has central 
 * methods for updating the positions of all objects 
 * on the game field, and also handles loading the 
 * missions, updating the camera position and 
 * AI movement. 
 *
 */
public class Controller {
	
	// class name, do not change (except when changing the actual class name)
	private final String CLASS = "CONTROLLER";
	
	public enum GameState{ IN_GAME, IN_MENU }
	
	/** Current state of the game (see {@link GameState})*/
	public GameState gameState;
		

	/** Current camera position as represented by a {@link Vector2f} */
	// the camera position should be publicly accessible
	public Vector2f cameraPos;
	
	
	/**
	 * Loads a mission from external files and prepares them for the game Window
	 */
	public Mission loadMission(){
		// TODO 
		return null;
	}
	
	/**
	 * Main update method, should be called every game time update
	 */
	public void update(){
		updateTank();
		updateBullet();
		updateAI();
		updateCamera();
	}

	
	private void updateTank() {
		// TODO Auto-generated method stub
	}

	private void updateBullet() {
		// TODO Auto-generated method stub
	}

	private void updateAI() {
		// TODO Auto-generated method stub
	}
	
	private void updateCamera() {
		// TODO Auto-generated method stub
	}
	
	

}

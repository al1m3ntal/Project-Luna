package ctrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import model.Mission;
import model.Tile;

import org.lwjgl.util.vector.Vector2f;

import util.TextureManager;

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
	
	// MISSIONS
	public static final String MISSION_DEBUG = "mission_dev";
	public static final String MISSION_BRIDGE = "bridge";


	
	
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
	public Mission loadMission(String missionName){
		Mission mission = new Mission(missionName); 
		
		// TODO catch errors
		// like when the map textures do not exist, or the .map file is corrupt/missing
		
		// load mission textures 
		// -> consisting of the background and foreground 
		mission.mapBackground = TextureManager.load("missions/" + missionName + ".png");
		mission.mapForeground = TextureManager.load("missions/" + missionName + "_foreground.png");
		
		// Load mission map 
		mission.map = loadMapFile(missionName);
		
		return mission; 
	
	}
	
	private Tile[][] loadMapFile(String missionName){
		try{
			File file = new File("missions/" + missionName +".map");
			FileInputStream in = new FileInputStream(file);
			ObjectInputStream objin = new ObjectInputStream(in);
			Tile[][] temp =	(Tile[][]) objin.readObject();
			objin.close();
			in.close();
			return temp;
		}catch(Exception e) {return null;}
	}
	
	// TODO load player chosen tank 
	
	
	
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

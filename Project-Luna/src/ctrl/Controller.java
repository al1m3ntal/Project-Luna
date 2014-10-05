package ctrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import model.Mission;
import model.Tank;
import model.TankStats;
import model.Tile;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

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


	//current played mission:
	private Mission mission;
	
	// Projectile textures (The texture is in accordance with the projectile type)
	public Texture[] texProjectile = new Texture[3];
	
	
	// class name, do not change (except when changing the actual class name)
	private final String CLASS = "CONTROLLER";
	
	public enum GameState{ IN_GAME, IN_MENU }
	
	/** Current state of the game (see {@link GameState})*/
	public GameState gameState;
		
	
	
	
	
	

	/** Current camera position as represented by a {@link Vector2f} */
	// the camera position should be publicly accessible
	public Vector2f cameraPos;
	
	public Controller()	{
		cameraPos = new Vector2f();
	}
	
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
		// load tanks and such 
		mission.playerTank = new Tank(new TankStats(0));
		mission.playerTank.texBase = util.TextureManager.load("res/tanks/tank2base.png");
		mission.playerTank.texTurret = util.TextureManager.load("res/tanks/tank2turret.png");
		
		// load the projectile textures
		// TODO add the other textures for the different projectile types 
		texProjectile[0] = TextureManager.load("res/projectiles/bullet.png");
		texProjectile[1] = TextureManager.load("res/projectiles/bullet.png");
		texProjectile[2] = TextureManager.load("res/projectiles/bullet.png");
		
		// TODO load AI tanks 
		

		
		
		this.mission = mission; //? what is this ? 
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
	 * gets the deltaT time in milliseconds
	 */
	public void update(int deltaT){
		updateTank(deltaT);
		updateBullet(deltaT);
		updateAI();
		updateCamera();
	}

	
	private void updateTank(int deltaT) {		
		/* player tank */
		mission.playerTank.update(deltaT);
		/* ai tanks */
		for (Tank tank : mission.aiTanks) {
			tank.update(deltaT);
		}
	}

	private void updateBullet(int deltaT) {
		// TODO
	}

	private void updateAI() {
		// TODO Auto-generated method stub
	}
	
	private void updateCamera() {
		//TODO get that info from Window? or Init?
		int height = 1080;
		int width = 1920;
		/* set camera on tank */ 
		cameraPos.x = mission.playerTank.position.x - width/2 + 300/2;	//50/2 for the tank witdh, because at the moment the tank is drawn with size (50,50)...
		cameraPos.y = mission.playerTank.position.y - height/2 + 300/2;
		
		/* if the tanks approaches the end of the map, the camera should not move further */
		if(cameraPos.x < 0)
			cameraPos.x = 0;
		else if(cameraPos.x + width > mission.map.length * 20)
			cameraPos.x = mission.map.length * 20 - width;
		if(cameraPos.y < 0)
			cameraPos.y = 0;
		else if(cameraPos.y + height > mission.map[0].length * 20)
			cameraPos.y = mission.map[0].length * 20 - height;
	}
	
	

}

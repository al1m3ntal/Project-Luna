package ctrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import model.Mission;
import model.Projectile;
import model.Tank;
import model.TankStats;
import model.Tile;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import util.Animation;
import util.AnimationManager;
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

	// class name, do not change (except when changing the actual class name)
	private final String CLASS = "CONTROLLER";

	public enum GameState{ IN_GAME, IN_MENU }

	/** Current state of the game (see {@link GameState})*/
	public GameState gameState;
	
	
	// MISSIONS
	public static final String MISSION_DEBUG  = "mission_dev";
	public static final String MISSION_BRIDGE = "bridge";


	//current played mission:
	private Mission mission;
	
	// Projectile textures (The texture is in accordance with the projectile type)
	public Texture[] texProjectile = new Texture[3];
	
	/* ANIMATiONS */
	public Texture[] animTankFire = new Texture[8];
	public Texture[] animExplosion = new Texture[38];
	
	
	

	/** Current camera position as represented by a {@link Vector2f} */
	// the camera position should be publicly accessible
	public Vector2f cameraPos = new Vector2f();
	

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
		
		
		// Prepare animations for the mission 
		AnimationManager.prepareAnimation(AnimationManager.ANIM_TANK_FIRE, animTankFire);
		
		
		
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
		//mission.playerTank.update(deltaT
		//		, calcColissionTiles_top(mission.playerTank)
		//		, calcColissionTiles_right(mission.playerTank)
		//		, calcColissionTiles_bottom(mission.playerTank)
		//		, calcColissionTiles_left(mission.playerTank));
		mission.playerTank.update(deltaT, mission.map);
		/* ai tanks */
		for (Tank tank : mission.aiTanks) {
			tank.update(deltaT
					, calcColissionTiles_top(tank)
					, calcColissionTiles_right(tank)
					, calcColissionTiles_bottom(tank)
					, calcColissionTiles_left(tank));
		}
	}

	private void updateBullet(int deltaT) {
		for (Projectile projectile : mission.projectiles) {
			projectile.update();
		}
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
	
		
	private List<Tile> calcColissionTiles_top(Tank tank)
	{
		List<Tile> tiles = new ArrayList<Tile>();
		int tank_pos_x = (int) tank.position.x/20;
		int tank_pos_y = (int) tank.position.y/20;
		
		int top_border = 3;
		if((tank.rotBase > 330 || tank.rotBase < 30) || (tank.rotBase < 210 && tank.rotBase > 150))
		{
			top_border = 3;
		}
		else if((tank.rotBase > 300 || tank.rotBase < 60) || (tank.rotBase < 240 && tank.rotBase > 120))
		{
			top_border = 4;
			tiles.add(mission.map[tank_pos_x +5][tank_pos_y +top_border]);
			
			tiles.add(mission.map[tank_pos_x +11][tank_pos_y +top_border]);
		}
		else
		{
			top_border = 5;
			tiles.add(mission.map[tank_pos_x +4][tank_pos_y +top_border]);
			tiles.add(mission.map[tank_pos_x +5][tank_pos_y +top_border]);
			
			tiles.add(mission.map[tank_pos_x +11][tank_pos_y +top_border]);
			tiles.add(mission.map[tank_pos_x +12][tank_pos_y +top_border]);
		}		

		tiles.add(mission.map[tank_pos_x +6][tank_pos_y +top_border]);
		tiles.add(mission.map[tank_pos_x +7][tank_pos_y +top_border]);
		tiles.add(mission.map[tank_pos_x +8][tank_pos_y +top_border]);
		tiles.add(mission.map[tank_pos_x +9][tank_pos_y +top_border]);
		tiles.add(mission.map[tank_pos_x +10][tank_pos_y +top_border]);

		
		return tiles;
	}
	
	private List<Tile> calcColissionTiles_right(Tank tank)
	{
		List<Tile> tiles = new ArrayList<Tile>();
		int tank_pos_x = (int) tank.position.x/20;
		int tank_pos_y = (int) tank.position.y/20;
		
		int right_border = 13;
		if((tank.rotBase > 90-30 && tank.rotBase < 90+30) || (tank.rotBase < 270+30 && tank.rotBase > 270-30))
		{
			//right_border = 13;
		}
		else if((tank.rotBase > 90-60 || tank.rotBase < 90+60) || (tank.rotBase < 270+60 && tank.rotBase > 270-60))
		{
			//right_border = 14;
			tiles.add(mission.map[tank_pos_x +right_border][tank_pos_y +5]);
			
			tiles.add(mission.map[tank_pos_x +right_border][tank_pos_y +11]);
		}
		else
		{
			//right_border = 15;
			tiles.add(mission.map[tank_pos_x +right_border][tank_pos_y +4]);
			tiles.add(mission.map[tank_pos_x +right_border][tank_pos_y +5]);
		
			tiles.add(mission.map[tank_pos_x +right_border][tank_pos_y +11]);
			tiles.add(mission.map[tank_pos_x +right_border][tank_pos_y +12]);
		}		

		tiles.add(mission.map[tank_pos_x +right_border][tank_pos_y +6]);
		tiles.add(mission.map[tank_pos_x +right_border][tank_pos_y +7]);
		tiles.add(mission.map[tank_pos_x +right_border][tank_pos_y +8]);
		tiles.add(mission.map[tank_pos_x +right_border][tank_pos_y +9]);
		tiles.add(mission.map[tank_pos_x +right_border][tank_pos_y +10]);

		
		return tiles;
	}
	
	private List<Tile> calcColissionTiles_bottom(Tank tank)
	{
		List<Tile> tiles = new ArrayList<Tile>();
		int tank_pos_x = (int) tank.position.x/20;
		int tank_pos_y = (int) tank.position.y/20;
		
		int bottom_border = 13;
		if((tank.rotBase > 330 || tank.rotBase < 30) || (tank.rotBase < 210 && tank.rotBase > 150))
		{
			//bottom_border = 13;
		}
		else if((tank.rotBase > 300 || tank.rotBase < 60) || (tank.rotBase < 240 && tank.rotBase > 120))
		{
			//bottom_border = 14;
			tiles.add(mission.map[tank_pos_x +5][tank_pos_y +bottom_border]);
			
			tiles.add(mission.map[tank_pos_x +11][tank_pos_y +bottom_border]);
		}
		else
		{
			//bottom_border = 15;
			tiles.add(mission.map[tank_pos_x +4][tank_pos_y +bottom_border]);
			tiles.add(mission.map[tank_pos_x +5][tank_pos_y +bottom_border]);
			
			tiles.add(mission.map[tank_pos_x +11][tank_pos_y +bottom_border]);
			tiles.add(mission.map[tank_pos_x +12][tank_pos_y +bottom_border]);
		}		
		

		tiles.add(mission.map[tank_pos_x +6][tank_pos_y +bottom_border]);
		tiles.add(mission.map[tank_pos_x +7][tank_pos_y +bottom_border]);
		tiles.add(mission.map[tank_pos_x +8][tank_pos_y +bottom_border]);
		tiles.add(mission.map[tank_pos_x +9][tank_pos_y +bottom_border]);
		tiles.add(mission.map[tank_pos_x +10][tank_pos_y +bottom_border]);

		
		return tiles;
	}
	
	private List<Tile> calcColissionTiles_left(Tank tank)
	{
		List<Tile> tiles = new ArrayList<Tile>();
		int tank_pos_x = (int) tank.position.x/20;
		int tank_pos_y = (int) tank.position.y/20;
		
		int left_border = 3;
		if((tank.rotBase > 90-30 && tank.rotBase < 90+30) || (tank.rotBase < 270+30 && tank.rotBase > 270-30))
		{
			//left_border = 3;
		}
		else if((tank.rotBase > 90-60 || tank.rotBase < 90+60) || (tank.rotBase < 270+60 && tank.rotBase > 270-60))
		{
			//left_border = 4;
			tiles.add(mission.map[tank_pos_x +left_border][tank_pos_y +5]);
			
			tiles.add(mission.map[tank_pos_x +left_border][tank_pos_y +11]);
		}
		else
		{
			//left_border = 5;
			tiles.add(mission.map[tank_pos_x +left_border][tank_pos_y +4]);
			tiles.add(mission.map[tank_pos_x +left_border][tank_pos_y +5]);
		
			tiles.add(mission.map[tank_pos_x +left_border][tank_pos_y +11]);
			tiles.add(mission.map[tank_pos_x +left_border][tank_pos_y +12]);
		}		
		

		tiles.add(mission.map[tank_pos_x +left_border][tank_pos_y +6]);
		tiles.add(mission.map[tank_pos_x +left_border][tank_pos_y +7]);
		tiles.add(mission.map[tank_pos_x +left_border][tank_pos_y +8]);
		tiles.add(mission.map[tank_pos_x +left_border][tank_pos_y +9]);
		tiles.add(mission.map[tank_pos_x +left_border][tank_pos_y +10]);

		
		return tiles;
	}


}

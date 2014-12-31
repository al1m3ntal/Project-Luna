package model;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.opengl.Texture;

import util.Animation;


/**
 * @version 1.00
 * <br><br>
 *
 * The mission class includes a texture (map), a 2-Dimensional
 * integer array that represents the map tiles, the player's tank
 * as well as a list of AI tanks.
 *
 */
public class Mission {
	
	/** Name of this mission */
	public String name;
	
	/** Tiled map of this mission. */
	public Tile[][] map;

	/** Tiles, that are blocked, as calculated during Initializing */
	public List<Tile> blockedTiles;
	
	/** Player's Tank */
	public Tank playerTank;
	
	/** List of AI tanks */
	public List<Tank> aiTanks;
	
	/** List of all current projectiles on the map */
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	public Texture mapBackground; 
	public Texture mapForeground;
	
	public Mission(String missionName) {
		aiTanks = new ArrayList<Tank>();
		blockedTiles = new ArrayList<Tile>();
	}
	
	public void Initialize()
	{
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if(map[i][j].type == TileType.OBSTACLE)
					blockedTiles.add(map[i][j]);
			}
		}
	}
	
	
	
	

}

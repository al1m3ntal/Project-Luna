package model;

import java.util.List;

import org.newdawn.slick.opengl.Texture;


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
	
	/** Player's Tank */
	public Tank playerTank;
	
	/** List of AI tanks */
	public List<Tank> aiTanks;
	
	public Texture mapBackground; 
	public Texture mapForeground;
	
	public Mission(String missionName) {
	}
	
	
	
	

}

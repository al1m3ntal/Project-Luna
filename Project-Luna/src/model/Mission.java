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
	
	/** Tiled map of this mission. */
	public Tile[][] map = new Tile[200][200];
	
	/** Map texture of this mission. */
	public Texture mapTexture;
	
	/** Player's Tank */
	public Tank playerTank;
	
	/** List of AI tanks */
	List<Tank> aiTanks;
	
	
	public Mission() {
		
		// create empty tile map 
		for(int x = 0; x < map.length ; x ++)
			for(int y = 0; y < map[0].length ; y ++)
				map[x][y] = new Tile(TileType.FREE, x, y);

		// TODO load map tiles from file

		// TODO load map texture
		
		// TODO create Player tank 
		
		// TODO create List of AI tanks
		
		
	}
	
	
	
	

}

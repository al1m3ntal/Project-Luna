package model;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Game;
import org.newdawn.slick.opengl.Texture;

/**
 * @version 1.00 <br>
 * <br>
 * 
 *          Tank class representing...err... a tank!
 *
 */
public class Tank {

	// Current SPEED of this tank as represented by a 2D vector
	public Vector2f speed;
	// Current Hit Points of this tank
	public int hp;
	// Current TURRET ROTATION of this tank
	public float rotTurret;
	// Current BASE ROTATION (tank) of this tank
	public float rotBase;
	// 300x300px Texture for the tank BASE
	public Texture texBase;
	// 300x300px Texture for the tank TURRET
	public Texture texTurret;
	// Current tank position as represented by a 2D vector
	// NOTE: NOT VECTOR2F FROM SLICK LIBRARY
	public Vector2f position;
	// Tank stats that is used by this tank
	public TankStats stats;
	// time the last shot was fired (temporary value)
	public long lastShotFired;
	
	// true if the tanks rotation is not blocked by a obstacle tile
	public boolean canTurnRight;
	public boolean canTurnLeft;

	/**
	 * Creates a tank using {@link TankStats}
	 * 
	 * @param stats
	 */
	public Tank(TankStats stats) {
		this.stats = stats;
		position = new Vector2f(150, 850);
		speed = new Vector2f(0, 0);
		
		canTurnRight = true;
		canTurnLeft = true;
	}

	/**
	 * Updates the tanks position, speed and rotation gets the deltaT time in
	 * seconds
	 * !!!! UNUSED !!!!
	 */
	public void update(double deltaT, List<Tile> top, List<Tile> right,
			List<Tile> bottom, List<Tile> left) {

		/* check for collision */
		if (speed.y < 0)
			// check if tiles to the right
			for (Tile tile : top) {
				if (tile.type == TileType.OBSTACLE)
					speed.y = 0;
				else if (tile.type == TileType.HAZARD)
					speed.y *= 0.98;
				else if (tile.type == TileType.WATER)
					speed.y *= 0.96;
			}
		else if (speed.y > 0)
			// check if tiles to the right
			for (Tile tile : bottom) {
				if (tile.type == TileType.OBSTACLE)
					speed.y = 0;
				else if (tile.type == TileType.HAZARD)
					speed.y *= 0.98;
				else if (tile.type == TileType.WATER)
					speed.y *= 0.96;
			}
		if (speed.x > 0)
			// check if tiles to the right
			for (Tile tile : right) {
				if (tile.type == TileType.OBSTACLE)
					speed.x = 0;
				else if (tile.type == TileType.HAZARD)
					speed.x *= 0.98;
				else if (tile.type == TileType.WATER)
					speed.x *= 0.96;
			}
		else if (speed.x < 0)
			// check if tiles to the left
			for (Tile tile : left) {
				if (tile.type == TileType.OBSTACLE)
					speed.x = 0;
				else if (tile.type == TileType.HAZARD)
					speed.x *= 0.98;
				else if (tile.type == TileType.WATER)
					speed.x *= 0.96;
			}

		position.x += speed.x;// * deltaT;

		position.y += speed.y;// * deltaT;
		// bleed of the speed over time
		speed.x *= 0.9f;
		speed.y *= 0.9f;

		// rotation should be between 0 - 359 degrees (tbh it probably doesnt
		// matter)
		if (rotBase < 0)
			rotBase += 360;
		else if (rotBase >= 360)
			rotBase -= 360;
	}

	/**
	 * Updates the tanks position, speed and rotation gets the deltaT time in
	 * seconds
	 */
	public void update(double deltaT, Tile[][] map) {

		//x and y position of tank
		int x = (int) (position.x / 20);
		int y = (int) (position.y / 20);
		

		/* 315 - 045 */
		/* 135 - 225 */
		if((rotBase >= 360 - 45 || rotBase <= 0 + 45) || (rotBase >= 180 - 45 && rotBase <= 180 + 45))
		{
			//y
			if(speed.y < 0 && (map[x+5][y+2].type == TileType.OBSTACLE || map[x+6][y+2].type == TileType.OBSTACLE
					|| map[x+7][y+2].type == TileType.OBSTACLE || map[x+8][y+2].type == TileType.OBSTACLE
					|| map[x+9][y+2].type == TileType.OBSTACLE
					|| map[x+5][y+3].type == TileType.OBSTACLE || map[x+6][y+3].type == TileType.OBSTACLE
					|| map[x+7][y+3].type == TileType.OBSTACLE || map[x+8][y+3].type == TileType.OBSTACLE
					|| map[x+9][y+3].type == TileType.OBSTACLE
					|| map[x+5][y+4].type == TileType.OBSTACLE || map[x+6][y+4].type == TileType.OBSTACLE
					|| map[x+7][y+4].type == TileType.OBSTACLE || map[x+8][y+4].type == TileType.OBSTACLE
					|| map[x+9][y+4].type == TileType.OBSTACLE))			
				speed.y = 0;	
			else if(speed.y > 0 && (map[x+5][y+10].type == TileType.OBSTACLE ||map[x+6][y+10].type == TileType.OBSTACLE
					|| map[x+7][y+10].type == TileType.OBSTACLE || map[x+8][y+10].type == TileType.OBSTACLE
					|| map[x+9][y+10].type == TileType.OBSTACLE
					|| map[x+5][y+11].type == TileType.OBSTACLE || map[x+6][y+11].type == TileType.OBSTACLE
					|| map[x+7][y+11].type == TileType.OBSTACLE || map[x+8][y+11].type == TileType.OBSTACLE
					|| map[x+9][y+11].type == TileType.OBSTACLE
					|| map[x+5][y+12].type == TileType.OBSTACLE || map[x+6][y+12].type == TileType.OBSTACLE
					|| map[x+7][y+12].type == TileType.OBSTACLE || map[x+8][y+12].type == TileType.OBSTACLE
					|| map[x+9][y+12].type == TileType.OBSTACLE))			
				speed.y = 0;	
			//x
			if(speed.x < 0 && (map[x+4][y+5].type == TileType.OBSTACLE ||map[x+4][y+6].type == TileType.OBSTACLE
					|| map[x+4][y+7].type == TileType.OBSTACLE || map[x+4][y+8].type == TileType.OBSTACLE
					|| map[x+4][y+9].type == TileType.OBSTACLE))			
				speed.y = 0;	
			else if(speed.y > 0 && (map[x+10][y+5].type == TileType.OBSTACLE ||map[x+10][y+6].type == TileType.OBSTACLE
					|| map[x+10][y+7].type == TileType.OBSTACLE || map[x+10][y+8].type == TileType.OBSTACLE
					|| map[x+10][y+9].type == TileType.OBSTACLE))			
				speed.y = 0;
			
			//tank looking up:
			if(rotBase >= 360 - 45 || rotBase <= 0 + 45)
			{
				if(map[x+10][y+3].type == TileType.OBSTACLE || map[x+10][y+4].type == TileType.OBSTACLE || map[x+10][y+5].type == TileType.OBSTACLE)
					canTurnRight = false;
				else
					canTurnRight = true;
								
				if(map[x+4][y+3].type == TileType.OBSTACLE || map[x+4][y+4].type == TileType.OBSTACLE || map[x+4][y+5].type == TileType.OBSTACLE)
					canTurnLeft = false;
				else
					canTurnLeft = true;
			}
			//tank looking down:
			else
			{
				if(map[x+4][y+9].type == TileType.OBSTACLE || map[x+4][y+10].type == TileType.OBSTACLE || map[x+4][y+11].type == TileType.OBSTACLE)
					canTurnRight = false;
				else
					canTurnRight = true;
								
				if(map[x+10][y+9].type == TileType.OBSTACLE || map[x+10][y+10].type == TileType.OBSTACLE || map[x+10][y+11].type == TileType.OBSTACLE)
					canTurnLeft = false;
				else
					canTurnLeft = true;
			}
		}
		/* 045 - 135 */
		/* 225 - 315 */
		if((rotBase >= 90 - 45 && rotBase <= 90 + 45) || (rotBase >= 270 - 45 && rotBase <= 270 + 45))
		{
			//x
			if(speed.x < 0 && (map[x+2][y+5].type == TileType.OBSTACLE ||map[x+2][y+6].type == TileType.OBSTACLE
					|| map[x+2][y+7].type == TileType.OBSTACLE || map[x+2][y+8].type == TileType.OBSTACLE
					|| map[x+2][y+9].type == TileType.OBSTACLE
					|| map[x+3][y+5].type == TileType.OBSTACLE || map[x+3][y+6].type == TileType.OBSTACLE
					|| map[x+3][y+7].type == TileType.OBSTACLE || map[x+3][y+8].type == TileType.OBSTACLE
					|| map[x+3][y+9].type == TileType.OBSTACLE
					|| map[x+4][y+5].type == TileType.OBSTACLE || map[x+4][y+6].type == TileType.OBSTACLE
					|| map[x+4][y+7].type == TileType.OBSTACLE || map[x+4][y+8].type == TileType.OBSTACLE
					|| map[x+4][y+9].type == TileType.OBSTACLE))			
				speed.x = 0;
			else if(speed.x > 0 && (map[x+12][y+5].type == TileType.OBSTACLE ||map[x+12][y+6].type == TileType.OBSTACLE
					|| map[x+12][y+7].type == TileType.OBSTACLE || map[x+12][y+8].type == TileType.OBSTACLE
					|| map[x+12][y+9].type == TileType.OBSTACLE
					|| map[x+11][y+5].type == TileType.OBSTACLE || map[x+11][y+6].type == TileType.OBSTACLE
					|| map[x+11][y+7].type == TileType.OBSTACLE || map[x+11][y+8].type == TileType.OBSTACLE
					|| map[x+11][y+9].type == TileType.OBSTACLE
					|| map[x+10][y+5].type == TileType.OBSTACLE || map[x+10][y+6].type == TileType.OBSTACLE
					|| map[x+10][y+7].type == TileType.OBSTACLE || map[x+10][y+8].type == TileType.OBSTACLE
					|| map[x+10][y+9].type == TileType.OBSTACLE))			
				speed.x = 0;
			//y
			if(speed.y < 0 && (map[x+5][y+4].type == TileType.OBSTACLE ||map[x+6][y+4].type == TileType.OBSTACLE
					|| map[x+7][y+4].type == TileType.OBSTACLE || map[x+8][y+4].type == TileType.OBSTACLE
					|| map[x+9][y+4].type == TileType.OBSTACLE))			
				speed.y = 0;	
			else if(speed.y > 0 && (map[x+5][y+10].type == TileType.OBSTACLE ||map[x+6][y+10].type == TileType.OBSTACLE
					|| map[x+7][y+10].type == TileType.OBSTACLE || map[x+8][y+10].type == TileType.OBSTACLE
					|| map[x+9][y+10].type == TileType.OBSTACLE))			
				speed.y = 0;	
			
			
			//tank looking right:
			if(rotBase >= 90 - 45 && rotBase <= 90 + 45)
			{
				if(map[x+9][y+10].type == TileType.OBSTACLE || map[x+10][y+10].type == TileType.OBSTACLE || map[x+11][y+10].type == TileType.OBSTACLE)
					canTurnRight = false;
				else
					canTurnRight = true;
								
				if(map[x+9][y+4].type == TileType.OBSTACLE || map[x+10][y+4].type == TileType.OBSTACLE || map[x+11][y+4].type == TileType.OBSTACLE)
					canTurnLeft = false;
				else
					canTurnLeft = true;
			}
			//tank looking left:
			else
			{
				if(map[x+3][y+4].type == TileType.OBSTACLE || map[x+4][y+4].type == TileType.OBSTACLE || map[x+5][y+4].type == TileType.OBSTACLE)
					canTurnRight = false;
				else
					canTurnRight = true;
								
				if(map[x+3][y+10].type == TileType.OBSTACLE || map[x+4][y+10].type == TileType.OBSTACLE || map[x+5][y+10].type == TileType.OBSTACLE)
					canTurnLeft = false;
				else
					canTurnLeft = true;
			}
			
		}

		
		
		
		
		position.x += speed.x;// * deltaT;

		position.y += speed.y;// * deltaT;
		// bleed of the speed over time
		speed.x *= 0.9f;
		speed.y *= 0.9f;

		// rotation should be between 0 - 359 degrees (tbh it probably doesnt
		// matter)
		if (rotBase < 0)
			rotBase += 360;
		else if (rotBase >= 360)
			rotBase -= 360;
	}

}

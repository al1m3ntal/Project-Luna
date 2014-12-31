package model;

import java.awt.Rectangle;
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
	 */
	public void Update(double deltaT, List<Tile> blockedTiles)
	{
		if(speed.x != 0)
		{
			for (Tile tile : blockedTiles) {
				if(intersects(tile))
				{
					speed.x = 0;
					break;
				}
			}
		}
		if(speed.y != 0)
		{
			for (Tile tile : blockedTiles) {
				if(intersects(tile))
				{
					speed.y = 0;
					break;
				}
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
	
	/**
	 * checks if the (blocked) tile intersects with the frame of the tank.
	 * Because the tank can rotate, its rotation has to be taken account in
	 * @param t
	 * @return
	 */
	private boolean intersects(Tile t)
	{
		Rectangle rect_tile = new Rectangle((int)(t.position.x * 20), (int)(t.position.y * 20), 20, 20);
		Rectangle rect_tank = new Rectangle(
				(int)(position.x + speed.x + 100 - (Math.abs(Math.sin(rotBase*Math.PI/180)) * 50))
				, (int)(position.y + speed.y + 50 + (Math.abs(Math.sin(rotBase*Math.PI/180)) * 50))
				, (int) (100 + (Math.abs(Math.sin(rotBase*Math.PI/180))) * 100)
				, (int) (200 - Math.abs(Math.sin(rotBase*Math.PI/180)) * 100));

		
		if(rect_tile.intersects(rect_tank))
			return true;
		return false;
	}
}

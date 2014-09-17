package model;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;


/**
 * @version 1.00
 * <br><br>
 * 
 * Tank class representing...err... a tank! 
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

	
	/**
	 * Creates a tank using {@link TankStats}
	 * @param stats 
	 */
	public Tank(TankStats stats){
		this.stats = stats;
	}

	
}

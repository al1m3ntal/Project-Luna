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
	
	// Current SPEED of this tank
	private int speed;
	// Current Hit Points of this tank
	private int hp;
	// Current TURRET ROTATION of this tank
	private float rotTurret;
	// Current BASE ROTATION (tank) of this tank
	private float rotBase;
	// 300x300px Texture for the tank BASE 
	private Texture texBase;
	// 300x300px Texture for the tank TURRET
	private Texture texTurret;
	// Current tank position as represented by a 2D vector
	// NOTE: NOT VECTOR2F FROM SLICK LIBRARY
	private Vector2f position;
	// Tank stats that is used by this tank 
	private TankStats stats;

	
	/**
	 * Creates a tank using {@link TankStats}
	 * @param stats 
	 */
	public Tank(TankStats stats){
		this.stats = stats;
	}

	
	/* GETTERS AND SETTERS */
	public void setSpeed(int speed){ this.speed = speed;	}
	public int getSpeed() {	return speed; }
	public int getHp() { return hp; }
	public void setHp(int hp) {	this.hp = hp; }
	public float getRotTurret() { return rotTurret;}
	public void setRotTurret(float rotTurret) { this.rotTurret = rotTurret; }
	public float getRotBase() {	return rotBase;	}
	public void setRotBase(float rotBase) { this.rotBase = rotBase;	}
	public Texture getTexBase() { return texBase; }
	public void setTexBase(Texture texBase) { this.texBase = texBase; }
	public Texture getTexTurret() { return texTurret; }
	public void setTexTurret(Texture texTurret) { this.texTurret = texTurret; }
	public Vector2f getPosition() { return position;}
	public void setPosition(Vector2f position) { this.position = position; }
	public TankStats getStats() { return stats; }
	
}

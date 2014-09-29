package model;


/**
 *
 * @version 1.00
 * <br><br>
 * 
 * Class that represents the traits (called 'stats' from now on) of a tank. 
 * To get the stats, call the #getStats() of the {@link Tank} class.
 * <br> <br>
 * These stats represent final values and as such cannot be changed.
 * <br> <br> 
 *
 */
public class TankStats {
	
	
	// Maximum speed a tank can travel at (in pixels?)
	private int maxSpeed;
	// speed at which the base of the tank can rotate
	private int rotSpeedBase;
	// speed at which the turret of the tank can rotate
	private int rotSpeedTurret;
	// maximum amount of hitpoints a tank can have
	private int hp;
	// damage the main gun does
	private int damage;
	// time in milliseconds that the main gun needs in order to shoot again
	private int reloadTime;
	// speed at which a tank can accelerate to get up to maxSpeed
	private float accleration;
	
	/**
	 * Creates a TankStats object from scratch.
	 * <br><br>
	 * 1: creates a default TankStats object
	 * @param stats 
	 */
	public TankStats(int stats){
		switch (stats) {
			default:
			case 0:
				maxSpeed 		= 50;
				rotSpeedBase 	= 5;
				rotSpeedTurret	= 20;
				hp 				= 100;
				damage 			= 25;
				reloadTime 		= 2500;
				accleration 	= 0.5f;
				break;
			
			case 1:
				maxSpeed 		= 75;
				rotSpeedBase 	= 8;
				rotSpeedTurret	= 35;
				hp 				= 75;
				damage 			= 15;
				reloadTime 		= 1750;
				accleration 	= 0.75f;
				break;
		}
	}

	
	
	
	/* GETTERS */
	
	
	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getRotSpeedBase() {
		return rotSpeedBase;
	}

	public int getRotSpeedTurret() {
		return rotSpeedTurret;
	}

	public int getHp() {
		return hp;
	}

	public int getDamage() {
		return damage;
	}

	public int getReloadTime() {
		return reloadTime;
	}

	public float getAccleration() {
		return accleration;
	}
	
	
	
	

}

package model;

import org.lwjgl.util.vector.Vector2f;


/**
 * Class representing projectiles such as tank shells, bullets and LASERS
 * @author alex
 *
 */
public class Projectile {

	public static final int PROJECTILE_BULLET = 0;
	public static final int PROJECTILE_SHELL = 1;
	public static final int PROJECTILE_LASER = 2;
	
	public int type;
	

	public Vector2f position = new Vector2f();
	public Vector2f speed = new Vector2f();
	public int size = 10;
	public int damage;
	public float rotation;
	
	
	public Projectile(int type,int x, int y, float degrees){
		this.type = type;
	
		position.x = x;
		position.y = y;
		this.rotation = degrees;
		
		switch(type){
		
		default:
		case PROJECTILE_BULLET: 
			//TODO add more info here 
			size = 10;
			damage = 10;
			break;
		case PROJECTILE_SHELL: 
			// TODO 
			break;
		case PROJECTILE_LASER: 
			// TODO
			break;
		
		
		
		}
		calculateSpeed(degrees+270);
	}


	// Depending on the degrees at which this projectile was fired, the X and Y speeds can 
	// be calculated using math... 
	private void calculateSpeed(float degrees) {
		// Using simple trigonometry we can calculate the sides of a triangle by given (shooting) angel and 
		// the length of the hypotenuse, which we will keep as 1 for simplicity's sake
		// sin(a) = opposite / hypotenuse
		// cos(a) = adjacent / hypotenuse
		//-> lengths of the opposites and adjacent sides can be calculated by using the following formula
		// (Hypotenuse is 1, so we can disregard it completely)
		// opposite = sin(a) / 1
		// adjacent = cos(a) / 1 
		System.out.println("calculating speed of projectile at " + degrees + "°");
		speed.y = (float) Math.sin(Math.toRadians(degrees))*20;
		speed.x = (float) Math.cos(Math.toRadians(degrees))*20;
		System.out.println("-> x:" + speed.x + " y:" + speed.y);
		
	}
	
	public void update(){
		position.x += speed.x;
		position.y += speed.y;
	}
	
	
	
	
	
}

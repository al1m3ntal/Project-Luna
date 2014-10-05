package model;

import javax.annotation.PostConstruct;

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
	public int sizeX = 10;
	public int sizeY = 10;
	public int damage;
	public float rotation;
	
	
	public Projectile(int type, int x, int y, float degrees){
		this.type = type;
		this.rotation = degrees;
		calculateOrigin(x, y, degrees+270);
		
		switch(type){
		
		default:
		case PROJECTILE_BULLET: 
			//TODO add more info here 
			sizeX = 10;
			sizeY = 10;
			damage = 10;
			break;
		case PROJECTILE_SHELL: 
			sizeX = 10;
			sizeY = 60;
			damage = 10;
			// TODO 
			break;
		case PROJECTILE_LASER: 
			// TODO
			break;
		
		
		
		}
		calculateSpeed(degrees+270);
	}


	private void calculateOrigin(int x, int y, float degrees) {
		// this is the middle point of the tank (and turret) from where the projectile was shot
		// since the projectile has to originate from the end of the turret and not the middle
		// point, we can calculate its actual starting position by knowing the angle (degrees) 
		// and the size of the tank (radius of drawing circle) which is 150
		
		// Note: The hypotenuse is equal to the radius of the drawing circle, which is 150
		// -> Delta Y is then defined by sin(degrees) = opposite / 150
		// -> opposite = sin(degrees)*150 (and we need to add 150 because the drawing circle 
		// originates from the screen's 0/0 and not the middle of the circle)
		
		// Delta X is the same as above, but with using cos(degrees) instead
		
		position.x = x + (float) Math.cos(Math.toRadians(degrees)) * 150 + 150;
		position.y = y + (float) Math.sin(Math.toRadians(degrees)) * 150 + 150;
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
		speed.x = (float) Math.cos(Math.toRadians(degrees))*20;
		speed.y = (float) Math.sin(Math.toRadians(degrees))*20;
	}
	
	public void update(){
		position.x += speed.x;
		position.y += speed.y;
	}
	
	
	
	
	
}

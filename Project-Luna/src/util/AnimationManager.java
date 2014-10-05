package util;

import org.newdawn.slick.opengl.Texture;

public class AnimationManager {

	// all the sprite definitions come here 
	public static final int ANIM_TANK_FIRE = 0;
	

	public static int spriteHeightTankFire;
	public static int spriteWidthTankFire;
	
	// load a sprite for drawing, this should only be called for the necessary sprites
	// before the drawing process begins. Do not load animations in the drawing process.
	public static void prepareAnimation(int animation, Texture[] sprites){
		
		switch(animation){
		default:
		case ANIM_TANK_FIRE: 
			for(int x = 0 ; x < sprites.length; x ++)
				sprites[x] = TextureManager.load("res/sprites/tank_fire/sprite_tank_fire_" + x + ".png");
		
			// set the width and height of the animation sprites
			if ( sprites[0] != null ){
				spriteHeightTankFire = sprites[0].getImageHeight();
				spriteWidthTankFire = sprites[0].getImageWidth();
			}
			
			break;

		
		}
		
		
	}
	

	
	
}

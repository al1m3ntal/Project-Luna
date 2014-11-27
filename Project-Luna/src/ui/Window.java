package ui;


import java.util.ArrayList;

import model.Mission;
import model.Projectile;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;

import ctrl.Controller;
import util.Animation;
import util.AnimationManager;
import util.Log;
import static org.lwjgl.opengl.GL11.*;

public class Window {

	// class name, do not change
	private final String CLASS = "WINDOW";
	
	private final double PI = 3.14159265359;
	
	/* time at last frame */
	long lastFrame;

	
	/* Dimensions and properties of this window */
	private int height;
	private int width;
	private boolean vsync = true;
	

	/* the controller, which is responsible for all the calculations and upates */
	private Controller controller;
	
	/* The current mission that is being played */
	private Mission mission; 
	private ArrayList<Animation> animations = new ArrayList<Animation>();
	
	
	
	
	public Window(Controller controller){
		this.controller = controller;

		// set up display
		// we need to set the window dimensions first, before we can initialize OpenGL
		height = 1080;
		width = 1920;
		initGL();
		
		
		// LOAD A TEST MISSION FOR NOW 
		
		// load the assets
		mission = controller.loadMission(Controller.MISSION_BRIDGE);

		//set time for first frame:
		getDelta(); // call once before loop to initialize lastFrame
		
		// game loop 
		while (!Display.isCloseRequested()){
			// Clear The Screen And The Depth Buffer
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			pollInput(getDelta());
					
			//Draw map first, for it should be in the background for obvious reasons
			drawMap();
			
			// update all game events 
			controller.update(getDelta());
			// update the display 
			Display.update();			
		}
		
		Display.destroy();
		Log.print(CLASS, "--- PROGRAM END ---");
	}
	
	
	/**
	 * Initialize the GL display
	 * <br><br>
	 * Set window.height, window.width first before calling this method, otherwise the 
	 * window dimensions will be 0 x 0.
	 */
	private void initGL() {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			Display.setVSyncEnabled(vsync);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		glEnable(GL_TEXTURE_2D);

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// enable alpha blending
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glViewport(0, 0, width, height);
		glMatrixMode(GL_MODELVIEW);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}
	
	/**
	 * Draws all the tanks of the mission
	 */
	private void drawTanks(){
		/* draw base */
		drawImage(mission.playerTank.texBase, (int)(mission.playerTank.position.x - controller.cameraPos.x), (int)(mission.playerTank.position.y - controller.cameraPos.y), 300, 300, mission.playerTank.rotBase);
		/* draw turret */
		drawImage(mission.playerTank.texTurret, (int)(mission.playerTank.position.x - controller.cameraPos.x), (int)(mission.playerTank.position.y - controller.cameraPos.y), 300, 300,  mission.playerTank.rotTurret);
	}
	
	/**
	 * Draws all the bullets of the mission
	 */
	private void drawBullets(){
		// iterate through the list of projectiles
		for (Projectile	p : mission.projectiles) {
			
			// if the projectile got destroyed or went out of bounds then we delete it
			if (p.position.x < 0 || p.position.y < 0 || p.position.x > mission.map.length*20 || p.position.y > mission.map[0].length*20){
				p = null;
			}
			

			if ( p == null )
				continue;
			
			
			// draw the projectile
			drawImage(	controller.texProjectile[p.type], 
						(int)(p.position.x - controller.cameraPos.x - p.sizeX/2), 
						(int)(p.position.y - controller.cameraPos.y - p.sizeY/2), 
						p.sizeX, // TODO fix size 
						p.sizeY, // TODO fix size
						p.rotation);
		}
	}
	
	public void drawAnimations(){
	
		for(Animation anim : animations){
			
			if (anim.hasEnded())
				continue;
			
			// get the current time 
			long currentTime = System.currentTimeMillis();
			// check if we need to update the animation 
			if ( currentTime > anim.startTime + anim.stepTime){
				// we need to draw the next step of this animation, and 
				// restart the timer 
				// update the animation
				anim.startTime = currentTime;
				anim.update();
			}
		
			drawImage(	controller.animTankFire[anim.currentStep-1],
						(int) (anim.x - controller.cameraPos.x),
						(int) (anim.y - controller.cameraPos.y),
						AnimationManager.spriteWidthTankFire,
						AnimationManager.spriteHeightTankFire,
						anim.rotation);
			
		}
		
	}

	/**
	 * Draws the map of the controller
	 */
	private  void drawMap(){
		// draw mission background
		
		drawImage(	mission.mapBackground, 
					0 - (int)controller.cameraPos.x, 
					0 - (int)controller.cameraPos.y, 
					mission.map.length*20,  // <-- change all the 20's to 10's for original size
					mission.map[0].length*20, 
					0f	);

		// TODO draw tanks now 
		drawTanks();
		drawBullets();
		drawAnimations();
		
		// draw mission foreground 
		if ( mission.mapForeground != null ) // <--- This IF case serves no purpose, since the system will exit when a texture tries to load that does not exist
			drawImage(	mission.mapForeground, 
						0 - (int)controller.cameraPos.x, 
						0 - (int)controller.cameraPos.y, 
						mission.map.length*20, 
						mission.map[0].length*20, 
						0f	);
		
	}
	
	/**
	 * Poll the Input on the Window
	 * 0/0 should be in the top-left corner, but is in the bottom-left
	 * therefore convert it properly
	 */
	private void pollInput(int deltaT) {
		
		
		calcTurretRotationV2(	Mouse.getX(),
								height - Mouse.getY(), 
								(int)mission.playerTank.position.x - (int)controller.cameraPos.x, 
								(int)mission.playerTank.position.y - (int)controller.cameraPos.y);
		
		
		//Left MouseButton clicked
		if (Mouse.isButtonDown(0)) {
			// shoot !
			// get the current time 
			long currentTime = System.currentTimeMillis();
			//System.out.println("Still waiting " + ( mission.playerTank.lastShotFired - currentTime + 2500) + " for gun cooldown");
			if ( currentTime > mission.playerTank.lastShotFired + mission.playerTank.stats.getReloadTime()){
				//System.out.println("Shoot !");
				mission.playerTank.lastShotFired = currentTime;
				
				// add the projectile to the list of mission projectiles 
				mission.projectiles.add(new Projectile(	mission.playerTank.stats.getProjectileType(), 
														(int)mission.playerTank.position.x,
														(int)mission.playerTank.position.y,
														mission.playerTank.rotTurret));
				// play the "tank fire" animation now
				Animation tankFire = new Animation();
				tankFire.x = (int) (mission.playerTank.position.x + (float) Math.cos(Math.toRadians(mission.playerTank.rotTurret+270)) * 225+75);
				tankFire.y = (int) (mission.playerTank.position.y + (float) Math.sin(Math.toRadians(mission.playerTank.rotTurret+270)) * 225+75);
				tankFire.rotation = mission.playerTank.rotTurret;
				tankFire.stepCount = 8;
				tankFire.stepTime = 50;
				animations.add(tankFire);
			}
			
			
			
			
		}
		/* driving */
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			mission.playerTank.speed.x += Math.sin(mission.playerTank.rotBase*PI/180) * mission.playerTank.stats.getAccleration() * deltaT;		
			mission.playerTank.speed.y -= Math.cos(mission.playerTank.rotBase*PI/180) * mission.playerTank.stats.getAccleration() * deltaT;
		}		
		else if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			mission.playerTank.speed.x -= Math.sin(mission.playerTank.rotBase*PI/180) * mission.playerTank.stats.getAccleration() * deltaT;		
			mission.playerTank.speed.y += Math.cos(mission.playerTank.rotBase*PI/180) * mission.playerTank.stats.getAccleration() * deltaT;			
		}	
		/* rotation */
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			if(mission.playerTank.canTurnLeft)
				mission.playerTank.rotBase -= mission.playerTank.stats.getRotSpeedBase() * deltaT;
		}			
		else if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			if(mission.playerTank.canTurnRight)
				mission.playerTank.rotBase += mission.playerTank.stats.getRotSpeedBase() * deltaT;
		}
			
		
	}
	
	/**
	 * draw an image at the specified position with the specified size and rotation
	 * @param texture [{@link Texture}] image to be drawn
	 * @param x [int] Left
	 * @param y [int] Top
	 * @param w [int] Right
	 * @param h [int] bottom
	 * @param rotation [float] Angel at which to draw the image
	 */
	private void drawImage(Texture texture, int x, int y, int w, int h, float rotation) {
		// store the current model matrix
		glPushMatrix();
		texture.bind(); // or glBind(texture.getTextureID());
		
		glTranslatef(x+w/2, y+h/2, 0);
		glRotatef(rotation, 0f, 0f, 1f);
		glTranslatef(-w/2, -h/2, 0);
		
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(0, h);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(w, h);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(w, 0);
		glEnd();
		
		// restore the model view matrix to prevent contamination
		glPopMatrix();
	}
	
	private void drawImageRect(Texture texture, int x, int y, int w, int h, int rectx, int recty, int rectWidth, int rectHeight, float rotation){
		// store the current model matrix
		glPushMatrix();
		texture.bind(); // or glBind(texture.getTextureID());
		
		glTranslatef(x+w/2, y+h/2, 0);
		glRotatef(rotation, 0f, 0f, 1f);
		glTranslatef(-w/2, -h/2, 0);
		
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(0, h);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(w, h);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(w, 0);
		glEnd();
		
		// restore the model view matrix to prevent contamination
		glPopMatrix();
		
	}
	
	
	
	private void drawLine(float x, float y, float x2, float y2) {
		//glClear(GL_COLOR_BUFFER_BIT);
	    glColor3f(0.0f, 1.0f, 0.2f);
	    glBegin(GL_LINE_STRIP);
	    glVertex2d(x , y );
	    glVertex2d(x2, x2);
	    glEnd();
	    glColor3f(1.0f, 1.0f, 1.0f);
	}
	
	
	private void calcTurretRotationV2(int mouseX, int mouseY, int tankX, int tankY){
		// calculate the differences in height and width
		int deltaY = tankY - mouseY + 150;
		int deltaX = tankX - mouseX + 150;
		// calculate the turret rotation
		mission.playerTank.rotTurret =  (float) Math.toDegrees(Math.atan2(deltaY, deltaX)) +270;
	}
	
	
	
	
	/**
	 * USE calcTurretRotationV2 instead (faster by like 0.0000001 seconds)
	 * Calculates the current turret rotation by creating 2 vectors and getting the angle between them
	 * @param mouseX
	 * @param mouseY
	 * @param tankY
	 * @param tankX
	 */
	private void calcTurretRotation(int mouseX, int mouseY, int tankX, int tankY){
		// need 2 vectors and calculate the angle between them for the rotation
		// 1st vector is a (1,0) vector originating from the tank's current position (or (-1,0) if the pointer is below the tank)
		// 2nd vector is the current mouse pointer in relation to the turret location
		// This vector is of type slick (not lwglj.util)
		Vector2f vector1 = new Vector2f();
		Vector2f vector2 = new Vector2f();

		int deltaY = 0; 
		int deltaX  = 0;
		int directionX = 0; 
		int directionY = 0; 
		
		
		// calculate the differences in height and width
		deltaY = tankY - mouseY + 150;
		deltaX = tankX - mouseX + 150;

		if ( deltaY > 0)
			directionY = 1;
		if ( deltaY < 0)
			directionY = -1;
		
		vector1.y = directionY;
		// get the height difference between the mouse pointer and the current tank position 
		vector2.y = directionY * Math.abs(tankY - mouseY + 150);

		// get the x difference between the mouse pointer and the current tank position 
		if ( deltaX > 0)
			directionX = -1;
		if ( deltaX < 0 )
			directionX = 1;
		
		// set the x position for the vectors
		vector2.x = directionX * Math.abs(tankX - mouseX + 150);
		vector1.x = 0;
		
		// calculate the turret rotation
		mission.playerTank.rotTurret =  (float) Math.toDegrees(Math.atan2(deltaY, deltaX)) +270;
		
		
	}
	
	
	/**
	 * Get the time in milliseconds
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
		//return (Sys.getTime() * 1000) / Sys.getTimerResolution();
		return System.nanoTime() / 1000000;
	}
	
	/**
	 * calculates the time since the last update
	 * in milliseconds
	 * 
	 * RETURNS 1 BECAUSE SHIT DOES NOT WORK
	 * @return
	 */
	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;

	   // return delta;	   
	    return 1;
	}
	
}

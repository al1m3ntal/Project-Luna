package ui;


import java.awt.Button;

import model.Mission;
import model.Tank;
import model.TankStats;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import ctrl.Controller;
import util.Log;

public class Window {

	// class name, do not change
	private final String CLASS = "WINDOW";
	
	private final double PI = 3.14159265359;
	
	/* Dimensions and properties of this window */
	private int height;
	private int width;
	private boolean vsync = true;
	

	/* the controller, which is responsible for all the calculations and upates */
	private Controller controller;
	
	/* The current mission that is being played */
	private Mission mission; 
	
	/* tank images */
	private Texture img_playertank_base;
	private Texture img_playertank_turret;

	
	public Window(Controller controller){
		this.controller = controller;

		// set up display
		// we need to set the window dimensions first, before we can initialize OpenGL
		height = 1080;
		width = 1920;
		initGL();
		
		// load the assets
		mission = controller.loadMission(Controller.MISSION_BRIDGE);
		
		mission.playerTank = new Tank(new TankStats(0));
		img_playertank_base = util.TextureManager.load("res/tanks/tank1base.png");
		img_playertank_turret = util.TextureManager.load("res/tanks/tank1turret.png");
		
		// game loop 
		while (!Display.isCloseRequested()){
			// Clear The Screen And The Depth Buffer
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			pollInput();
					
			//Draw map first, for it should be in the background for obvious reasons
			drawMap();
			
			// update all game events 
			controller.update();
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

		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// enable alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glViewport(0, 0, width, height);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	/**
	 * Draws all the tanks of the controller
	 */
	private void drawTanks(){
		/* draw base */
		drawImage(img_playertank_base, (int)(mission.playerTank.position.x - controller.cameraPos.x), (int)(mission.playerTank.position.y - controller.cameraPos.y), 100, 100, mission.playerTank.rotBase);
		/* draw turret */
		drawImage(img_playertank_turret, (int)(mission.playerTank.position.x - controller.cameraPos.x), (int)(mission.playerTank.position.y - controller.cameraPos.y), 100, 100,  mission.playerTank.rotTurret);
	}
	
	/**
	 * Draws all the bullets of the controller
	 */
	private void drawBullets(){
		
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
	private void pollInput() {
		//Left MouseButton clicked
		if (Mouse.isButtonDown(0)) {
			int x = Mouse.getX();
			int y = height - Mouse.getY();
			//TODO do something with it
		}
		/* driving */
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			mission.playerTank.speed.x += Math.sin(mission.playerTank.rotBase*PI/180) * mission.playerTank.stats.getAccleration();		
			mission.playerTank.speed.y -= Math.cos(mission.playerTank.rotBase*PI/180) * mission.playerTank.stats.getAccleration();
		}		
		else if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			mission.playerTank.speed.x -= Math.sin(mission.playerTank.rotBase*PI/180) * mission.playerTank.stats.getAccleration();		
			mission.playerTank.speed.y += Math.cos(mission.playerTank.rotBase*PI/180) * mission.playerTank.stats.getAccleration();			
		}	
		/* rotation */
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			mission.playerTank.rotBase -= 3.0f;
		else if(Keyboard.isKeyDown(Keyboard.KEY_D))
			mission.playerTank.rotBase += 3.0f;
		
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
		GL11.glPushMatrix();
		texture.bind(); // or GL11.glBind(texture.getTextureID());
		
		GL11.glTranslatef(x+w/2, y+h/2, 0);
		GL11.glRotatef(rotation, 0f, 0f, 1f);
		GL11.glTranslatef(-w/2, -h/2, 0);
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(0, 0);
		GL11.glTexCoord2f(0, texture.getHeight());
		GL11.glVertex2f(0, h);
		GL11.glTexCoord2f(texture.getWidth(), texture.getHeight());
		GL11.glVertex2f(w, h);
		GL11.glTexCoord2f(texture.getWidth(), 0);
		GL11.glVertex2f(w, 0);
		GL11.glEnd();
		
		// restore the model view matrix to prevent contamination
		GL11.glPopMatrix();
	}
	
	
	
}

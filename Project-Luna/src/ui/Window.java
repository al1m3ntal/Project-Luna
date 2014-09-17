package ui;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import ctrl.Controller;
import util.Log;

public class Window {

	private final String CLASS = "WINDOW";
	
	/* Height of the displayed window */
	private static int window_height;

	/* the controller, which is responsible for all the calculations. */
	private static Controller controller;
	
	public Window(){
		//TODO uebergib Controller
		// set up display
		window_height = 1080;
		initGL(1920, window_height);
		
		
		
		// game loop ? 
		while (!Display.isCloseRequested()){
			// Clear The Screen And The Depth Buffer
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
					
			//Draw map first, for it should be in the background for obvious reasons
			drawMap();
			drawBullets();
			drawTanks();
			
			//TODO call controller.update()						
						
			Display.update();			
		}
		
		Display.destroy();
		Log.print(CLASS, "--- PROGRAM END ---");
	}
	
	
	/**
	 * Initialise the GL display
	 *
	 * @param width
	 *            The width of the display
	 * @param height
	 *            The height of the display
	 */
	private static void initGL(int width, int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			Display.setVSyncEnabled(true);
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
	private static void drawTanks()
	{
		
	}
	
	/**
	 * Draws all the bullets of the controller
	 */
	private static void drawBullets()
	{
		
	}

	/**
	 * Draws the map of the controller
	 */
	private static void drawMap()
	{
		
	}

	
	/**
	 * Poll the Input on the Window
	 * 0/0 should be in the top-left corner, when it is in the bottom-left
	 * therefore convert it properly
	 */
	private static void pollInput() {
		//Left MouseButton clicked
		if (Mouse.isButtonDown(0)) {
			int x = Mouse.getX();
			int y = window_height - Mouse.getY();
			//TODO do something with it
		}
	}
	
	/**
	 * draw an image at the specified position and the specified size and rotation
	 */
	private static void drawImage(Texture texture, int x, int y, int w, int h, float rotation) {
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

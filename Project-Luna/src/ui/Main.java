package ui;


import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Main {
	
	static Texture texture;
	//last clicked mouse_position. Init with 100
	static int mouse_x = 100;
	static int mouse_y = 100;
	
	public static void main(String[] args) {
		
		// init OpenGL here
		initGL(1000,1000);
		//init resources
		init();
		
		//Game-Loop
		while (!Display.isCloseRequested()) {
		// render OpenGL here
			pollInput();
			render(mouse_x,mouse_y);
			Display.update();		
		}
		
		Display.destroy();
		
	}
	
	public static void pollInput() {
		if (Mouse.isButtonDown(0)) {
		int x = Mouse.getX();
		int y = Mouse.getY();
		System.out.println("MOUSE DOWN @ X: " + x + " Y: " + y);
		mouse_x = x;
		mouse_y = y;
		}
	}
	
	
	/**
	* Initialise the GL display
	*
	* @param width The width of the display
	* @param height The height of the display
	*/
	private static void initGL(int width, int height) {
	try {
	Display.setDisplayMode(new DisplayMode(width,height));
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

	GL11.glViewport(0,0,width,height);
	GL11.glMatrixMode(GL11.GL_MODELVIEW);

	GL11.glMatrixMode(GL11.GL_PROJECTION);
	GL11.glLoadIdentity();
	GL11.glOrtho(0, width, height, 0, 1, -1);
	GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}


	/**
	* Initialise resources
	*/
	public static void init() {
	 
	try {
	// load texture from PNG file
	texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/desert.png"));
	System.out.println(">> Image height: "+texture.getImageHeight());
	} catch (IOException e) {
	e.printStackTrace();
	}
	}
	
	/**
	* draw a quad with the image on it
	*/
	public static void render(int x, int y) {
		// Clear The Screen And The Depth Buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
	Color.white.bind();
	texture.bind(); // or GL11.glBind(texture.getTextureID());

	GL11.glBegin(GL11.GL_QUADS);
	GL11.glTexCoord2f(0,0);
	GL11.glVertex2f(x,y);
	GL11.glTexCoord2f(1,0);
	GL11.glVertex2f(x+texture.getTextureWidth(),y);
	GL11.glTexCoord2f(1,1);
	GL11.glVertex2f(x+texture.getTextureWidth(),y+texture.getTextureHeight());
	GL11.glTexCoord2f(0,1);
	GL11.glVertex2f(x,y+texture.getTextureHeight());
	GL11.glEnd();
	}
	
}

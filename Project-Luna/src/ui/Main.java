package ui;


import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Main {
	
	static Texture texture;
	
	public static void main(String[] args) {
		
		try {
			Display.setDisplayMode(new DisplayMode(1000,1000));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		init();
		
		// init OpenGL here
		while (!Display.isCloseRequested()) {
		// render OpenGL here
			pollInput();
			
		Display.update();		
		}
		
		Display.destroy();
		
	}
	
	public static void pollInput() {
		if (Mouse.isButtonDown(0)) {
		int x = Mouse.getX();
		int y = Mouse.getY();
		System.out.println("MOUSE DOWN @ X: " + x + " Y: " + y);
		}
	}
	


	/**
	* Initialise resources
	*/
	public static void init() {
	 
	try {
	// load texture from PNG file
	texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/desert.png"));
	 System.out.println(texture.getHeight());
	} catch (IOException e) {
	e.printStackTrace();
	}
	}
	
}

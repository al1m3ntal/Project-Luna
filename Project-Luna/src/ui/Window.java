package ui;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import util.Log;

public class Window {

	private final String CLASS = "WINDOW";
	
	
	public Window(){
		
		// set up display
		try{
			Display.setDisplayMode(new DisplayMode(1920, 1080));
			Display.create();
		} catch(LWJGLException e){
			e.printStackTrace();
			System.exit(0);
		}
		
		
		
		// game loop ? 
		while (!Display.isCloseRequested()){
			// render openGL here
			Display.update();
			
		}
		
		Display.destroy();
		Log.print(CLASS, "--- PROGRAM END ---");
	}
	
	
	
	
	
}

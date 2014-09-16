package ui;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import util.Log;


public class AlexTutorial {

	public static void main(String[] args) {

		// set up display
		try{
			Display.setDisplayMode(new DisplayMode(1920, 1080));
			Display.create();
		} catch(LWJGLException e){
			e.printStackTrace();
			System.exit(0);
		}
		
		
		while (!Display.isCloseRequested()){
			// reder openGL here
			Display.update();
			
		}
		
		Log.print("ALEX_TUTORIAL", "Welcome Message");
		Log.printErr("ALEX_TUTORIAL", "Welcome Message");
		
		
		
		Display.destroy();
		
	}

}

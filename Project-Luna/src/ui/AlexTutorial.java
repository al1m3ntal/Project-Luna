package ui;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;


public class AlexTutorial {

	public static void main(String[] args) {

		System.err.println("STARTING TUTORIAL PROGRAMM");

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
		
		
		Display.destroy();
		
	}

}

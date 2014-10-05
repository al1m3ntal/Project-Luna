package util;

import org.newdawn.slick.opengl.Texture;

public class Animation {

	public int type;
	public int stepCount;
	public int x;
	public int y;
	public float rotation;
	public long stepTime = 1000;
	public long startTime;
	public int currentStep = 0;
	public boolean repeat = false;
	private boolean stopped = false;
	
	public void update(){
		
		if(stepCount == 0){
			Log.printErr("ANIMATION", "Step count is 0, make sure to set the step count equal to that of the sprites[] length");
			return;
		}
		
		
		currentStep++;
		if ( currentStep >= stepCount ){
			if ( repeat)
				currentStep = 0;
			else
				stopped = true;

			// TODO 
			// add listener for animation listeners to stop drawing this animation after it has 
			// ended or to just out right remove it from their animation lists
		}
	}
	
	public boolean hasEnded(){
		return stopped; 
	}
	
	
	
}

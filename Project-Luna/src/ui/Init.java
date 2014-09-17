package ui;

import ctrl.Controller;
import util.Log;

public class Init {

	public static void main(String[] args) {

		Log.print("INIT", "Starting PROjECT-LUNA");
		new Window(new Controller());
		
		
	}

}

package ui;

import java.awt.Font;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import util.Log;

public class Main {
	
	/** Window params */
	static int window_width = 1000;
	static int window_height = 1000;
	
	/** Boolean flag on whether AntiAliasing is enabled or not */
	private static boolean antiAlias = true;
	
	//Textures
	static Texture texture_desert;
	static Texture texture_marker;
	//Fonts
	static TrueTypeFont font;
	// last pressed mouse-position. Init with 100
	static int mouse_x = 100;
	static int mouse_y = 100;

	public static void main(String[] args) {

		// init OpenGL here
		initGL(window_width, window_height);
		// init resources
		texture_desert = initTexture("res/desert.png");
		texture_marker = initTexture("res/area_marker.png");
		
		font = initFont("Times New Roman");
		
		//check if everything is initialized properly:
		if(texture_desert == null || texture_marker == null)
		{
			Log.printErr("Main","Ini-Error: At least one texture was not loaded properly.");
			return;
		}
		
		// Game-Loop
		while (!Display.isCloseRequested()) {
			// render OpenGL here
			// Clear The Screen And The Depth Buffer
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			pollInput();
			drawImage(texture_desert, 0, 0, window_width, window_height);
			drawImage(texture_marker, mouse_x-25, mouse_y-25,50,50);
			
			drawText(font, Color.black, 5, 5, "Mark the hidden entrance!");
			Display.update();
		}

		Display.destroy();

	}

	/**
	 * Poll the Input on the Window
	 * 0/0 should be in the top-left corner, when it is in the bottom-left
	 * therefore convert it properly
	 */
	public static void pollInput() {
		if (Mouse.isButtonDown(0)) {
			int x = Mouse.getX();
			int y = window_height - Mouse.getY();
			mouse_x = x;
			mouse_y = y;
		}
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
	 * Initialise resources
	 */
	public static Texture initTexture(String path) {
		Texture texture = null;
		try {
			// load texture from PNG file
			texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream(path));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return texture;
	}
	
	public static TrueTypeFont initFont(String type) {
		// load a default java font
		Font awtFont = new Font(type, Font.BOLD, 24);
		TrueTypeFont font = new TrueTypeFont(awtFont, antiAlias);

//		// load font from file
//		try {
//		InputStream inputStream = ResourceLoader.getResourceAsStream("myfont.ttf");
//
//		Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
//		awtFont2 = awtFont2.deriveFont(24f); // set font size
//		font2 = new TrueTypeFont(awtFont2, antiAlias);
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
		return font;
	}

	
	/**
	 * draw a quad with the image on it at the specified position and the specified size
	 */
	public static void drawImage(Texture texture, int x, int y, int w, int h) {
		// store the current model matrix
		GL11.glPushMatrix();
		texture.bind(); // or GL11.glBind(texture.getTextureID());
		GL11.glTranslatef(x, y, 0);
		
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
	
	/**
	 * draw Text at the specified position 
	 */
	public static void drawText(TrueTypeFont font, Color color, int x, int y, String text) {
		Color.white.bind();
		font.drawString(x, y, text, color);
		//Set the color to white again
		Color.white.bind();
	}

}

package util;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * 
 * @version 1.00
 * <br><br>
 * 
 * Providing means to load any needed texture.
 *
 */
public class TextureManager {

	/**
	 * Loads a PNG image from given path
	 * @param path to file
	 */
	public static Texture load(String path) {
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
	
	
	
	/**
	 * Loads a java AWT image from the given file path. <br><br>
	 * Used in the map editor
	 */
	public static java.awt.Image loadImage(String path){
		return new javax.swing.ImageIcon(path).getImage();
	}
	
}

package util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

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
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return texture;
	}
	
	
	
	/**
	 * Loads a java AWT image from the given file path. <br><br>
	 * Used in the map editor
	 */
	public static Image loadImage(String path){
		
		try{
			Image img = ImageIO.read(new File(path));
			return img; 
		}catch(Exception e){
			return null; 
		}
		
		//return new ImageIcon(path).getImage();
	}
	
	
}

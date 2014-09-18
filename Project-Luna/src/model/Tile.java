package model;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector2f;

// implements serializable to make this class save-able in a binary java data object file (.dat)
public class Tile implements Serializable{

	public TileType type;
	public Vector2f position = new Vector2f();

	public Tile(TileType tileType, int x , int y){
		this.type = tileType;
		position.x = x;
		position.y = y;
	}
	
	public Tile(TileType tileType, Vector2f position){
		this.type = tileType;
		this.position = position;
	}
	
}

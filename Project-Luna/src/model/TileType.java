package model;

public enum TileType {
	
	// nothing on the tile, player can drive over it
	FREE, 
	// something in the way, player cannot driver over it
	OBSTACLE,
	// water
	WATER,
	// hazardous tile, like poison!
	HAZARD

}

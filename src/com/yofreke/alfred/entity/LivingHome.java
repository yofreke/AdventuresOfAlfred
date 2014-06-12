package com.yofreke.alfred.entity;

import com.yofreke.alfred.level.Level;
import com.yofreke.alfred.level.tile.BuildingTile;
import com.yofreke.alfred.level.tile.Tile;

public class LivingHome {

	private Level level;
	private BuildingTile building;
	private int x, y;
	
	public LivingHome(Level level, int x, int y){
		this.level = level;
		this.x = x;
		this.y = y;
		
		Tile t = Tile.tiles[level.getTile(x, y)];
		this.building = t instanceof BuildingTile ? (BuildingTile) t : null;
	}
	
	public BuildingTile getBuilding() { return building; }
	
	public int getX() { return x; }
	public int getY() { return y; }
}

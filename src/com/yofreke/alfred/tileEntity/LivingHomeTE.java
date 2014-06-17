package com.yofreke.alfred.tileEntity;

import java.util.ArrayList;

import com.yofreke.alfred.entity.Living;
import com.yofreke.alfred.level.Level;
import com.yofreke.alfred.level.tile.BuildingTile;
import com.yofreke.alfred.level.tile.Tile;

public class LivingHomeTE extends TileEntity {

	private BuildingTile building;
	private ArrayList<Living> residents = new ArrayList<Living>();
	
	private int capacity = 1;
	
	public LivingHomeTE(Level level, int x, int y) {
		super(level, x, y);
		
		Tile t = Tile.tiles[level.getTile(x, y)];
		this.building = t instanceof BuildingTile ? (BuildingTile) t : null;
	}
	
	public BuildingTile getBuilding() { return building; }
	
	public int getX() { return x; }
	public int getY() { return y; }
	
	public void registerResident(Living living) {
		this.residents.add(living);
	}
	public void unregisterResident(Living resident) {
		this.residents.remove(resident);
	}
	public int getResidentCount() {
		return this.residents.size();
	}
	
	public int getMaxCapacity() {
		return capacity;
	}

	public void update(){
	}
}

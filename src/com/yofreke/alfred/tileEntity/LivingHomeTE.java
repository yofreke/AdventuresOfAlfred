package com.yofreke.alfred.tileEntity;

import java.util.ArrayList;

import com.yofreke.alfred.entity.Living;
import com.yofreke.alfred.item.Item;
import com.yofreke.alfred.item.ItemStack;
import com.yofreke.alfred.level.Level;
import com.yofreke.alfred.level.tile.BuildingTile;
import com.yofreke.alfred.level.tile.Tile;
import com.yofreke.alfred.states.IngameState;

public class LivingHomeTE extends TileEntity {

	private BuildingTile building;
	private ArrayList<Living> residents = new ArrayList<Living>();
	
	private int capacity = 1;
	
	private BuildingTile buildingTile;
	
	private String collectionType = null;
	private int collectionAmount = 0;
	
	public LivingHomeTE(BuildingTile buildingTile, Level level, int x, int y) {
		super(level, x, y);
		this.buildingTile = buildingTile;
		
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
	
	public int getCapacity() {
		return capacity;
	}
	public void upgradeCapacity() {
		this.capacity++;
		
		buildingTile.addWorker(level, x, y);
	}
	
	public void deposit(ItemStack itemstack){
		Item item = Item.items[itemstack.getId()];
		IngameState.purse.transact(item.name, itemstack.getQuality());
		
		if(item.name.equals(this.getCollectionType())) {
			this.collectionAmount += itemstack.getQuality();
		}
	}
	
	public String getCollectionType() {
		return this.collectionType;
	}
	public LivingHomeTE setCollectionType(String name) {
		this.collectionType = name;
		return this;
	}
	public int getCollectedAmount() {
		return this.collectionAmount;
	}

	public void update(){
	}
}

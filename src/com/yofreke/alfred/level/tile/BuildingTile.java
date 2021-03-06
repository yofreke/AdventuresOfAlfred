package com.yofreke.alfred.level.tile;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.yofreke.alfred.entity.Living;
import com.yofreke.alfred.item.Item;
import com.yofreke.alfred.item.ItemStack;
import com.yofreke.alfred.level.Level;
import com.yofreke.alfred.states.IngameState;
import com.yofreke.alfred.tileEntity.LivingHomeTE;
import com.yofreke.alfred.tileEntity.TileEntity;

public abstract class BuildingTile extends Tile implements ITileEntityPair {

	protected Color drawColor = new Color(1,1,1,0.7f);
	
	public BuildingTile(int id, int texIndex) {
		super(id, texIndex);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity getEntity(Level level, int worldX, int worldY) {
		return new LivingHomeTE(this, level, worldX, worldY);
	}
	
	public void onAdd(Level level, int x, int y){
		super.onAdd(level, x, y);
		this.addWorker(level, x, y);
	}
	
	public void addWorker(Level level, int x, int y) {
		Living worker = getNewWorker(level, x, y);
		if(worker != null){
			worker.setHome((LivingHomeTE) level.getTileEntity(x, y));
			level.addEntity(worker);
		}
	}
	
	protected Living getNewWorker(Level level, int x, int y){
		return null;//new Lumberjack(level, x * 8.5f, y * 8.5f);
	}
	
	public abstract void renderHeldTile(Graphics g);
}

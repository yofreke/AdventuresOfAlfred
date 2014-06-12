package com.yofreke.alfred.tileEntity;

import com.yofreke.alfred.level.Level;
import com.yofreke.alfred.level.tile.Tile;

public class StumpTE extends TileEntity {

	private int age;
	private int regrowTime;
	
	public StumpTE(Level level, int x, int y) {
		super(level, x, y);
		regrowTime = 3000 + level.rand.nextInt(1000);
	}

	public void update(){
		super.update();
		age++;
		if(age >= regrowTime && !level.isBlocked(x, y, true)){
			level.setTileAndData(x, y, Tile.tree.id, level.getData(x, y));
		}
	}
}

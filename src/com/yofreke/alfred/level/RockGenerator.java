package com.yofreke.alfred.level;

import java.util.Random;

import com.yofreke.alfred.level.tile.Tile;

public class RockGenerator extends Generator {

	@Override
	public void genAt(Level level, Random rand, int x, int y) {
		if(!valid(level, x, y)) return;
		
		int rad = 2 + rand.nextInt(3);
		int radCut = (int) (rad * 0.35);
		
		for(int y1 = -rad; y1 < rad; y1++){
			for(int x1 = -rad; x1 < rad; x1++){
				if(rand.nextInt(Math.abs(x1) + 1) > radCut || rand.nextInt(Math.abs(y1) + 1) > radCut){
					continue;
				}
				if(valid(level, x + x1, y + y1) && valid(level, x + x1 + 1, y + y1) && valid(level, x + x1 + 1, y + y1 + 1) && valid(level, x + x1, y + y1 + 1)){
					setBlock(level, x + x1, y + y1, Tile.stone.id);
				}
			}
		}
	}
	
	private boolean valid(Level level, int x, int y){
		int id = level.getTile(x, y);
		return id == Tile.grass.id || id == Tile.tree.id;
	}
	
	private void setBlock(Level level, int x, int y, int id){
		level.setTile(x, y, id);
		level.setTile(x + 1, y, id);
		level.setTile(x + 1, y + 1, id);
		level.setTile(x, y + 1, id);
	}
}

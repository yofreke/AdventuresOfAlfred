package com.yofreke.alfred.level.tile;

import com.yofreke.alfred.level.Level;


public class SandTile extends RoundedTile {

	public SandTile(int id, int texIndex) {
		super(id, texIndex);
	}

	protected boolean testLoc(Level level, int x, int y){
		int i = level.getTile(x, y);
		return i == id || i == grass.id;
	}
}

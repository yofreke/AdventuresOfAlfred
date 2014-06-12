package com.yofreke.alfred.level.tile;

import org.newdawn.slick.Graphics;

import com.yofreke.alfred.level.Level;

public class StoneTile extends RoundedTile {

	public StoneTile(int id, int texIndex) {
		super(id, texIndex);
		checkTouching = false;
	}
	
	public void renderAt(Graphics g, Level level, int x, int y, int dx, int dy){
		dirt.renderNormAt(g, x, y, dx, dy);
		super.renderAt(g, level, x, y, dx, dy);
	}
}

package com.yofreke.alfred.level.tile;

import org.newdawn.slick.Graphics;

import com.yofreke.alfred.level.Level;

public class GrassTile extends RoundedTile {

	public GrassTile(int id, int texIndex) {
		super(id, texIndex);
	}

	protected boolean testLoc(Level level, int x, int y){
		int i = level.getTile(x, y);
		return i == id || i == tree.id || i == flag.id;
	}
	
	public void renderNormAt(Graphics g, int x, int y, int dx, int dy){
		renderSpecs(g, x, y, dx, dy);
		g.drawImage(getImage(), dx, dy);
	}
}

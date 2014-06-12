package com.yofreke.alfred.level.tile;

import org.newdawn.slick.Graphics;

import com.yofreke.alfred.Perlin;
import com.yofreke.alfred.level.Level;

public class TreeTile extends Tile {

	public TreeTile(int id, int texIndex) {
		super(id, texIndex);
		canDamage = true;
		renderPass = 1;
	}
	
	public void degrade(Level level, int x, int y){
		level.setTile(x, y, grass.id);
	}
	
	public void renderAt(Graphics g, Level level, int x, int y, int dx, int dy){
		grass.renderNormAt(g, x, y, dx, dy);
		int data = level.getData(x, y);
		float noise = Perlin.IntNoise(x * y);
		g.drawImage(texSheet.getSubImage(24, 8 + (data * 16), 16, 16), dx - 3 + (noise * 3), dy - 8 + (((noise + x) % 2 - 1) * 3));
		//g.drawRect(dx, dy, 16, 16);
	}
}

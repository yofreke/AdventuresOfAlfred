package com.yofreke.alfred.level.tile;

import org.newdawn.slick.Graphics;

import com.yofreke.alfred.Perlin;
import com.yofreke.alfred.level.Level;

public class WaterTile extends Tile {

	public WaterTile(int id, int texIndex) {
		super(id, texIndex);
	}

	public void renderAt(Graphics g, Level igs, int x, int y, int dx, int dy){
		super.renderAt(g, igs, x, y, dx, dy);
		if(Perlin.IntNoise(x * y) > 0){
			renderSpecs(g, x, y, dx, dy);	
		}
	}
}

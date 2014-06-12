package com.yofreke.alfred.level.tile;

import org.newdawn.slick.Graphics;

import com.yofreke.alfred.Perlin;
import com.yofreke.alfred.level.Level;
import com.yofreke.alfred.tileEntity.StumpTE;
import com.yofreke.alfred.tileEntity.TileEntity;

public class TreeStumpTile extends Tile implements ITileEntityPair {

	public TreeStumpTile(int id, int texIndex) {
		super(id, texIndex);
	}

	@Override
	public TileEntity getEntity(Level level, int worldX, int worldY) {
		return new StumpTE(level, worldX, worldY);
	}

	public void renderAt(Graphics g, Level level, int x, int y, int dx, int dy){
		grass.renderNormAt(g, x, y, dx, dy);
		int data = level.getData(x, y);
		float noise = Perlin.IntNoise(x * y);
		g.drawImage(texSheet.getSubImage(24, 40 + (data * 8), 16, 8), dx - 3 + (noise * 3), dy - 8 + (((noise + x) % 2 - 1) * 3));
	}
}

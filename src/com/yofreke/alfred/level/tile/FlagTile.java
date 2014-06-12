package com.yofreke.alfred.level.tile;

import org.newdawn.slick.Graphics;

import com.yofreke.alfred.level.Level;
import com.yofreke.alfred.tileEntity.TileEntity;
import com.yofreke.alfred.tileEntity.TownCenterTE;

public class FlagTile extends Tile implements ITileEntityPair {

	public FlagTile(int id, int texIndex) {
		super(id, texIndex);
	}
	
	public void renderAt(Graphics g, Level level, int x, int y, int dx, int dy){
		grass.renderNormAt(g, x, y, dx, dy);
		
		g.drawImage(texSheet.getSubImage(texX, texY, 16, 16), dx + 1, dy - 10);
	}

	@Override
	public TileEntity getEntity(Level level, int worldX, int worldY) {
		return new TownCenterTE(level, worldX, worldY);
	}
}

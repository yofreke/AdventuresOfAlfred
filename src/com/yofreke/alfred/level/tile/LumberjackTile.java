package com.yofreke.alfred.level.tile;

import org.newdawn.slick.Graphics;

import com.yofreke.alfred.entity.Living;
import com.yofreke.alfred.entity.Lumberjack;
import com.yofreke.alfred.level.Chunk;
import com.yofreke.alfred.level.Level;

public class LumberjackTile extends BuildingTile {

	public LumberjackTile(int id, int texIndex) {
		super(id, texIndex);
		name = "Lumber Mill";
		renderPass = 1;
		ignorePositions = new int[]{-1,0, 1,0};
	}
	
	public void renderAt(Graphics g, Level level, int x, int y, int dx, int dy){
		grass.renderNormAt(g, x, y, dx, dy);
		g.drawImage(texSheet.getSubImage(40, 16, 32, 16), dx - 8, dy - 8);
	}
	
	protected Living getNewWorker(Level level, int x, int y){
		return new Lumberjack(level, x * Chunk.TILE_SIZE + 4, y * Chunk.TILE_SIZE + 4);
	}
	
	@Override
	public void renderHeldTile(Graphics g) {
		g.drawImage(texSheet.getSubImage(40, 16, 32, 16), -12, -16, drawColor);
	}
}

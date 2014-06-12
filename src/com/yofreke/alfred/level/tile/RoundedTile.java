package com.yofreke.alfred.level.tile;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.yofreke.alfred.level.Chunk;
import com.yofreke.alfred.level.Level;

public class RoundedTile extends Tile {

	protected boolean checkTouching = true;
	
	public RoundedTile(int id, int texIndex) {
		super(id, texIndex);
	}

	public void renderAt(Graphics g, Level level, int x, int y, int dx, int dy){
		
		boolean l = testLoc(level, x - 1, y);
		boolean r = testLoc(level, x + 1, y);
		boolean u = testLoc(level, x, y - 1);
		boolean d = testLoc(level, x, y + 1);
		boolean allSides = u && d && r && l;
		
		int tx = (texIndex % 16) * Chunk.TILE_SIZE;
		int ty = (int) (texIndex / 16) * Chunk.TILE_SIZE;
		
		if(!allSides && checkTouching){
			if(id != sand.id && touching(level, x, y, sand)){
				sand.renderNormAt(g, x, y, dx, dy);
			} else if(id != gravel.id && touching(level, x, y, gravel)){
				gravel.renderNormAt(g, x, y, dx, dy);
			} else if(touching(level, x, y, water)){
				water.renderNormAt(g, x, y, dx, dy);
			} else if(id != dirt.id && touching(level, x, y, dirt) || touching(level, x, y, stone)){
				dirt.renderNormAt(g, x, y, dx, dy);
			} else if(id != grass.id && touching(level, x, y, grass) || touching(level, x, y, tree)){
				grass.renderNormAt(g, x, y, dx, dy);
			}
		}
		
		if(!u && !l){
			g.drawImage(texSheet.getSubImage(tx, ty, 4, 4), dx, dy);
		} else {
			g.drawImage(texSheet.getSubImage(tx + (l ? 8 : 0), ty + (u ? 8 : 0), 4, 4), dx, dy);
		}
		
		if(!u && !r){
			g.drawImage(texSheet.getSubImage(tx + 20, ty, 4, 4), dx + 4, dy);
		} else {
			g.drawImage(texSheet.getSubImage(tx + (r ? 12 : 20), ty + (u ? 8 : 0), 4, 4), dx + 4, dy);
		}
		
		if(!d && !r){
			g.drawImage(texSheet.getSubImage(tx + 20, ty + 20, 4, 4), dx + 4, dy + 4);
		} else {
			g.drawImage(texSheet.getSubImage(tx+ (r ? 12 : 20), ty + (d ? 12 : 20), 4, 4), dx + 4, dy + 4);
		}
		
		if(!d && !l){
			g.drawImage(texSheet.getSubImage(tx, ty + 20, 4, 4), dx, dy + 4);
		} else {
			g.drawImage(texSheet.getSubImage(tx + (l ? 8 : 0), ty + (d ? 12 : 20), 4, 4), dx, dy + 4);
		}
		
		if(allSides){
			renderSpecs(g, x, y, dx, dy);
		}
		
	}
	
	public Image getImage(){
		return texSheet.getSprite(texIndex % 16 + 1, (int)(texIndex / 16) + 1);
	}
	
	protected boolean testLoc(Level level, int x, int y){
		return level.getTile(x, y) == id;
	}
}

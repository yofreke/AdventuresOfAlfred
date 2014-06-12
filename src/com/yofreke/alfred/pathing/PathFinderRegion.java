package com.yofreke.alfred.pathing;

import java.awt.Point;

import com.yofreke.alfred.entity.Living;
import com.yofreke.alfred.level.Level;
import com.yofreke.alfred.level.tile.Tile;

public class PathFinderRegion {

	private static int[] costs;
	static {
		costs = new int[Tile.tiles.length];
		costs[Tile.tree.id] = 10;
		costs[Tile.stone.id] = 1000;
		costs[Tile.water.id] = 1000;
	}
	
	private Level level;
	private Living entity;
	
	private int margin = 10;
	
	//public int startX, startY;
	public Point start;
	public Point end;
	public int ox, oy;
	public int w, h;
	
	private boolean[] usedTiles;
	
	public PathFinderRegion(Level level, Living entity, int startX, int startY, int endX, int endY){
		this.level = level;
		this.entity = entity;
		
		int l, r, t, b;
		if(startX < endX){
			l = startX;
			r = endX;
		} else {
			l = endX;
			r = startX;
		}
		if(startY < endY){
			t = startY;
			b = endY;
		} else {
			t = endY;
			b = startY;
		}
		
		this.ox = l - margin;
		this.oy = t - margin;
		this.w = r - l + margin * 2;
		this.h = b - t + margin * 2;
		usedTiles = new boolean[w * h];
		
		start = new Point(endX - ox, endY - oy);
		end = new Point(startX - ox, startY - oy);
		
		//System.out.println(this.toString());
	}
	
	public void setUsed(Point p){
		usedTiles[p.y * w + p.x] = true;
	}
	public boolean getUsed(Point p){
		return usedTiles[p.y * w + p.x];
	}
	
	public int getCost(int x, int y){
		int id = level.getTile(x + ox, y + oy);
		return 10 + costs[id];
	}
	
	public boolean isValid(int x, int y){
		// check contained
		if(x < 0 || y < 0 || x >= w || y >= h) return false;
		// check isnt used
		if(usedTiles[y * w + x]) return false;
		// check tiles
		if(!level.isValidTile(x + ox, y + oy)) return false;
		if(level.getIgnore(x + ox, y + oy)) return false;
		return entity.canPass(level.getTile(x + ox, y + oy));
	}
	
	public int distToEnd(int x, int y){
		return Math.abs(x - end.x) + Math.abs(y - end.y);
	}
	
	public String toString(){
		return "Region ("+start.x+","+start.y+"), ("+end.x+","+end.y+") ["+w+"x"+h+"] ["+ox+","+oy+"]";
	}
}

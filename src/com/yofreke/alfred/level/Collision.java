package com.yofreke.alfred.level;

import com.yofreke.alfred.level.tile.Tile;

public class Collision {

	public Tile t;
	public int x, y;
	
	public Collision(Tile t, int x, int y){
		this.t = t;
		this.x = x;
		this.y = y;
	}
}

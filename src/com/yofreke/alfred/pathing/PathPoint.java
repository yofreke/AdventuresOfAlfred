package com.yofreke.alfred.pathing;

public class PathPoint {

	public int x, y;
	public int G;
	public int H;
	public int F;
	
	public PathPoint(int x, int y, int G, int H){
		this.x = x;
		this.y = y;
		this.G = G;
		this.H = H;
		this.F = G + H;
	}
	
}

package com.yofreke.alfred.pathing;

import java.awt.Point;

public class PathNode {
	
	public Point position;
	public int cost;
	public int pathCost;
	public PathNode next;
	public PathNode nextListElem;

	public PathNode(Point position, int cost, int pathCost, PathNode next)
	{
		this.position = position;
		this.cost = cost;
		this.pathCost = pathCost;
		this.next = next;
	}
	
	public String toString() {
		return "node "+position.x+","+position.y;
	}
}

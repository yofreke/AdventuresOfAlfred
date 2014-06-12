package com.yofreke.alfred.pathing;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.yofreke.alfred.PointF;

public class Path {

	private int pathLen;
	private PointF[] points;
	private int followIndex;
	private boolean isDone;
	
	public Path(PointF[] points){
		this.points = points;
		this.pathLen = points.length;
	}
	public Path(int pathLen){
		this.pathLen = pathLen;
		points = new PointF[pathLen];
	}
	
	public void setPoint(int index, PointF p){
		points[index] = p.copy();
	}
	
	public void increment(){
		followIndex++;
		if(followIndex >= points.length){
			followIndex = points.length - 1;
			isDone = true;
		}
	}
	
	public boolean isDone(){
		return isDone;
	}
	
	public int getPathLen(){
		return pathLen;
	}
	
	public void render(Graphics g){
		g.setColor(Color.orange);
		g.drawRect(points[0].x - 2, points[0].y - 2, 4, 4);
		g.setColor(Color.green);
		g.drawRect(points[points.length-1].x - 2, points[points.length-1].y - 2, 4, 4);
		Color c = new Color(0.8f, 0.8f, 0.65f);
		for(int i = 1; i < points.length; i++){
			float f = (float)i / points.length;
			c.r = 0.8f - f * 0.65f;
			c.b = 0.25f + f * 0.65f;
			g.setColor(c);
			g.drawLine(points[i-1].x, points[i-1].y, points[i].x, points[i].y);
		}
	}
	
	public PointF getCurrentPoint(){
		return points[followIndex];
	}
}

package com.yofreke.alfred;

public class PointF {

	public float x, y;
	
	public PointF(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public PointF copy(){
		return new PointF(x, y);
	}
	
	public String toString(){
		return "PF["+x+","+y+"]";
	}
}

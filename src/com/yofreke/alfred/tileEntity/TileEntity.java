package com.yofreke.alfred.tileEntity;

import org.newdawn.slick.Graphics;

import com.yofreke.alfred.level.Level;

public class TileEntity {

	protected Level level;
	protected int x, y;
	public boolean isDead;
	
	public TileEntity(Level level, int x, int y){
		this.level = level;
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	
	public void onRemove(){
		isDead = true;
	}
	
	public void update(){
		
	}
	
	public void render(Graphics g){
		
	}
}

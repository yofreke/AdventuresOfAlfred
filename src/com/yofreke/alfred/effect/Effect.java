package com.yofreke.alfred.effect;

import org.newdawn.slick.Graphics;

public class Effect {
	
	public float x, y;
	public float velX, velY;
	public boolean isDead;
	protected int age = 0;
	
	public Effect(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void update(){
		age++;
		x += velX;
		y += velY;
	}
	
	public void render(Graphics g){
		
	}
}

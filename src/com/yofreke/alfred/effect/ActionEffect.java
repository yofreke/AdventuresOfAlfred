package com.yofreke.alfred.effect;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class ActionEffect extends Effect {

	private static int MAX_AGE = 70;
	
	private Color actionColor;
	private float size;
	
	public ActionEffect(float x, float y, Color col) {
		super(x, y);
		this.actionColor = col;
		velY = -1.5f;
		size = 1.5f;
	}

	public void update(){
		velY *= 0.915f;
		size *= 1.02f;
		super.update();
		if(age > MAX_AGE){
			isDead = true;
		}
	}
	
	public void render(Graphics g){
		actionColor.a = 1 - ((float) age / MAX_AGE) * 0.25f;
		g.setColor(actionColor);
		g.fillRect(x - size * 0.5f, y - size * 0.5f, size, size);
		g.setColor(Color.white);
	}
}

package com.yofreke.alfred.entity;

import java.util.Random;

import org.newdawn.slick.Graphics;

import com.yofreke.alfred.level.Chunk;
import com.yofreke.alfred.level.Level;

public class Entity {
	
	protected static Random rand = new Random();

	public Level level;
	public float x, y;
	public float velX, velY;
	public boolean isDead;
	
	protected boolean spawnLocked;
	
	public Entity(Level level, float x, float y){
		this.level = level;
		this.x = x;
		this.y = y;
		
		updateSpawnLocked();
	}
	
	public void kill(){
		isDead = true;
	}
	
	public void update(){
		if(spawnLocked){
			updateSpawnLocked();
			x += velX;
			y += velY;
		} else {
			doMove();
		}
		
		velX *= 0.85f;
		velY *= 0.85f;
	}
	
	public int getTileX(){
		return (int) (x / Chunk.TILE_SIZE);
	}
	public int getTileY(){
		return (int) (y / Chunk.TILE_SIZE);
	}
	
	public void updateSpawnLocked(){
		spawnLocked = level.isBlocked(x, y);
	}
	public boolean getSpawnLocked(){
		return spawnLocked;
	}
	
	protected void doMove(){
		float newPos = x + velX;
		if(level.isBlocked(newPos, y)){
			velX = 0;
		}
		x += velX;
		
		newPos = y + velY;
		if(level.isBlocked(x, newPos)){
			velY = 0;
		}
		y += velY;
	}
	
	public void render(Graphics g){
		g.fillRect(x - 4, y - 8, 8, 8);
	}
	
	public void renderSelectionInfo(Graphics g){
		g.drawRect(x - 4, y - 8, 8, 8);
	}
}

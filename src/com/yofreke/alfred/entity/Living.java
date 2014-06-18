package com.yofreke.alfred.entity;

import java.awt.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.yofreke.alfred.Game;
import com.yofreke.alfred.PointF;
import com.yofreke.alfred.item.ItemStack;
import com.yofreke.alfred.level.Chunk;
import com.yofreke.alfred.level.Level;
import com.yofreke.alfred.level.tile.Tile;
import com.yofreke.alfred.pathing.Path;
import com.yofreke.alfred.pathing.PathFinder;
import com.yofreke.alfred.tileEntity.LivingHomeTE;

public class Living extends Entity {

	private static float WALK_ERROR = 1f;
	private static float WALK_ERROR_BLOCKED = 7f;
	
	private int health, maxHealth;
	protected int age = 0;
	protected int facing = 0;
	protected float moveSpeed = 1.0f;
	protected PointF targPoint;
	
	protected Path curPath;
	public PathFinder pathFinder;
	
	protected int actionCooldown = 20;
	protected int actionTimer = 0;
	
	protected LivingHomeTE home;
	protected PointF idleWaypoint;
	private ItemStack carrying;
	
	public Living(Level level, float x, float y){
		super(level, x, y);
		targPoint = new PointF(x, y);
		pathFinder = new PathFinder(level, this);
		idleWaypoint = new PointF(x, y);
	}
	
	public void walkTo(float worldX, float worldY){
		walkTo((int) (worldX / Chunk.TILE_SIZE), (int) (worldY / Chunk.TILE_SIZE));
	}
	public void walkTo(int tileX, int tileY){
		targPoint.x = x;
		targPoint.y = y;
		curPath = pathFinder.findPath(getTileX(), getTileY(), tileX, tileY);
	}
	
	public void setHome(LivingHomeTE newHome){
		if(this.home != null) {
			this.home.unregisterResident(this);
		}
		this.home = newHome;
		this.home.registerResident(this);
		setIdleWaypoint(newHome.getX() * Chunk.TILE_SIZE + 4f, newHome.getY() * Chunk.TILE_SIZE + 4f);
	}
	public void setIdleWaypoint(float tx, float ty){
		idleWaypoint.x = tx;
		idleWaypoint.y = ty;
	}
	
	public void heal(int amount){
		health = Math.min(health + amount, maxHealth);
	}
	public void hurt(int amount){
		health = Math.max(health - amount, 0);
		if(health == 0){
			die();
		}
	}
	
	public void setCarrying(ItemStack itemstack){
		carrying = itemstack;
		if(carrying != null){
			//System.out.println("walking home");
			walkTo(home.getX(), home.getY());
		}
	}
	public ItemStack getCarrying(){
		return carrying;
	}
	
	public void postDeposit(){
		walkTo(idleWaypoint.x, idleWaypoint.y);
	}
	
	public void setFacing(int i){
		facing = i;
	}
	public int getFacing(){
		return facing;
	}
	
	protected void die(){
		super.kill();
	}
	
	public boolean tryAction(){
		if(actionTimer > 0) return false;
		actionTimer = actionCooldown;
		doAction();
		return true;
	}
	
	public void doAction(){
	}
	
	public int getDamageAgainst(Tile t){
		return 1;
	}
	
	public Point getFacingTile(){
		Point p = new Point((int) (x / Chunk.TILE_SIZE), (int) (y / Chunk.TILE_SIZE));
		switch(facing){
		case 0:
			p.y -= 1;
			break;
		case 1:
			p.x += 1;
			break;
		case 3:
			p.x -= 1;
			break;
		default:
			p.y += 1;
			break;
		}
		if(p.x < 0) p.x = 0;
		if(p.y < 0) p.y = 0;
		return p;
	}
	
	public void updateLiving(){
		boolean ontarg = onTargCalc();
		
		if(ontarg){
			velX = 0;
			velY = 0;
			if(curPath != null){
				curPath.increment();
				targPoint = curPath.getCurrentPoint();
			}
		} else {
			if(x - targPoint.x < -WALK_ERROR){
				if(!ontarg) velX = moveSpeed;
				setFacing(1);
			} else if(x - targPoint.x > WALK_ERROR){
				if(!ontarg) velX = -moveSpeed;
				setFacing(3);
			} else {
				velX = 0;
			}
			if(y - targPoint.y < -WALK_ERROR){
				if(!ontarg) velY = moveSpeed;
				setFacing(2);
			} else if(y - targPoint.y > WALK_ERROR){
				if(!ontarg) velY = -moveSpeed;
				setFacing(0);
			} else {
				velY = 0;
			}
		}
		
		if(carrying != null){
			if(atTile(home.getX(), home.getY())){
				home.deposit(carrying);
				carrying = null;
				postDeposit();
			}
		} else {
			if(atPoint(idleWaypoint.x, idleWaypoint.y)){
				tryAction();
			}
		}
	}
	
	public boolean atTile(int tx, int ty){
		return Math.abs(getTileX() - tx) + Math.abs(getTileY() - ty) <= 2; 
	}
	public boolean atPoint(float worldX, float worldY){
		return Math.abs(x - worldX) + Math.abs(y - worldY) <= 2; 
	} 
	
	public boolean canPass(int tileId){
		if(spawnLocked) return true;
		return Tile.tiles[tileId].canPass();
	}
	
	public void setTargetPos(float newX, float newY){
		targPoint.x = newX;
		targPoint.y = newY;
	}
	public void setTargetPoint(PointF p){
		this.targPoint = p.copy();
	}
	
	public boolean onTargCalc(){
		float error = level.isBlocked(targPoint.x, targPoint.y) ? WALK_ERROR_BLOCKED : WALK_ERROR;
		return Math.abs(x - targPoint.x) <= error && Math.abs(y - targPoint.y) <= error; 
	}
	
	protected Point findClosestTile(int id){
		int radNorm = 1;
		
		int cx = (int) (x / Chunk.TILE_SIZE);
		int cy = (int) (y / Chunk.TILE_SIZE);
		int breakout = 0;
		
		int startX, startY;
		int endX, endY;
		int xAdd, yAdd;
		
		while(breakout++ < 16){
			if(rand.nextBoolean()){
				startX = -radNorm;
				endX = radNorm;
				xAdd = 1;
			} else {
				startX = radNorm;
				endX = -radNorm;
				xAdd = -1;
			}

			//System.out.println(startX+","+endX+" : "+(startX - endX));
			for (int x_ = startX; x_ - endX != 0; x_ += xAdd)
            {
				//System.out.println("  "+x_+" : "+(x_ - endX));
                int h = (int)(radNorm * Math.sin(Math.acos((double)x_ / (double)radNorm)));
                //System.out.println("  h="+h);
        		if(rand.nextBoolean()){
        			startY = -h;
        			endY = h;
        			yAdd = 1;
        		} else {
        			startY = h;
        			endY = -h;
        			yAdd = -1;
        		}
                for (int y_ = startY; y_ - endY != 0; y_ += yAdd){
                	if(level.getTile(x_ + cx, y_ + cy) == id){
                		Point p = new Point(x_ + cx, y_ + cy);
                		if(p.x != getTileX() && p.y != getTileY()){
                			return p;
                		}
                	}
                }
            }
			radNorm++;
			//System.out.println("search range extended "+radNorm);
		}
		
		return null;
	}
	
	public void update(){
		age += 1;
		updateLiving();
		if(actionTimer > 0){
			actionTimer--;
		}
		super.update();
	}
	
	public void render(Graphics g){
		//super.render(g);
		if(Game.DEBUG) {
			g.drawLine(x, y, targPoint.x, targPoint.y);
			g.setColor(Color.blue);
			g.drawLine(x, y, idleWaypoint.x, idleWaypoint.y);
			g.setColor(Color.white);
			if(curPath != null){
				g.setColor(curPath.isDone() ? Color.orange : Color.red);
				curPath.render(g);
			}
			if(carrying != null){
				g.setColor(Color.cyan);
				g.fillRect(x - 2, y - 3, 4, 3);
			}
			g.setColor(Color.white);
		}
	}
}

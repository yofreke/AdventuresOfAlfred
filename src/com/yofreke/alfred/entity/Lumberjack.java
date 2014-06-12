package com.yofreke.alfred.entity;

import java.awt.Point;

import org.newdawn.slick.Graphics;

import com.yofreke.alfred.Art;
import com.yofreke.alfred.item.Item;
import com.yofreke.alfred.item.ItemStack;
import com.yofreke.alfred.level.Level;
import com.yofreke.alfred.level.tile.Tile;

public class Lumberjack extends Living {
	
	
	public Lumberjack(Level level, float x, float y) {
		super(level, x, y);
	}
	
	public void doAction(){
		super.doAction();
		Point facing = getFacingTile();
		Tile t = Tile.tiles[level.getTile(facing.x, facing.y)];
		if(t.canDamage()){
			int damage = getDamageAgainst(t);
			if(damage > 0){
				level.damageTile(facing.x, facing.y, damage);
				if(level.getTile(facing.x, facing.y) != Tile.tree.id){
					setCarrying(new ItemStack(Item.wood.id));
				}
			}
		} else if(onTargCalc() && (curPath == null || curPath.isDone())){
			findMoreWood();
		}
	}
	
	public void findMoreWood(){
		//System.out.println(this + " moar woods");
		Point p = findClosestTile(Tile.tree.id);
		if(p != null){
			walkTo(p.x, p.y);
		}
	}
	
	public boolean canPass(int tileId){
		if(!super.canPass(tileId)){
			return getCarrying() == null && tileId == Tile.tree.id;
		}
		return true;
	}
	
	public int getDamageAgainst(Tile t){
		return t == Tile.tree ? 4 : 0;
	}
	
	public void update() {
		super.update();
		if(getCarrying() == null){
			tryAction();
		}
	}

	public void render(Graphics g){
		g.pushTransform();
		g.drawImage(Art.lumberjackSheet.getSprite((int) (/*(age * 0.1f) % 2*/1), 1 + facing), x - 8, y - 16);
		g.popTransform();
		super.render(g);
	}
}

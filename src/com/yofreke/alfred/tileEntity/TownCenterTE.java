package com.yofreke.alfred.tileEntity;

import org.newdawn.slick.Graphics;

import com.yofreke.alfred.item.ItemStack;
import com.yofreke.alfred.level.Level;
import com.yofreke.alfred.states.IngameState;

public class TownCenterTE extends TileEntity {

	public TownCenterTE(Level level, int x, int y) {
		super(level, x, y);
		
		/*for(int i = 0; i < 10; i++){
			Lumberjack l = new Lumberjack(level, x * 8.5f, y * 8.5f);
			l.setHome(this);
			level.addEntity(l);
		}*/
		//level.testEnt = l;
	}
	
	public void deposit(ItemStack itemstack){
		IngameState.wood++;
	}
	
	public void render(Graphics g){
		// TODO: Town boundaries
	}
}

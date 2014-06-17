package com.yofreke.alfred.gui;

import org.newdawn.slick.Graphics;

import com.yofreke.alfred.level.tile.Tile;
import com.yofreke.alfred.tileEntity.LivingHomeTE;

public class GuiBuildingMenu extends GuiElement {

	private float offsetX = 0;
	private float targOffsetX = 0;
	
	private LivingHomeTE target;
	private Tile targetTile = null;
	
	private String title = "";
	
	public GuiBuildingMenu(int x, int y, int w, int h) {
		super(x, y, w, h);
		setScale(2.5f);
		
//		addElement(new GuiButton(402, 600, 24, 24).setImage(this.image.getSubImage(120, 48, 24, 24)));
	}
	
	public void hide() {
		targOffsetX = this.width;
	}
	
	public void show(LivingHomeTE target) {
		this.target = target;
		this.targetTile = Tile.tiles[target.level.getTile(target.x, target.y)];
		targOffsetX = 0;
		
		title = targetTile.name;
	}
	
	public void draw(Graphics g){
		g.pushTransform();
		offsetX += (targOffsetX - offsetX) * 0.1;
		g.translate(offsetX, 0);
		
		super.draw(g);
		
		g.pushTransform();
		g.translate(x, y);
		g.scale(2, 2);
		FontRenderer.renderString(g, title, 10, 10);
		
		g.popTransform();
		g.popTransform();
	}
}

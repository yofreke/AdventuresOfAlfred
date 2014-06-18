package com.yofreke.alfred.gui;

import org.newdawn.slick.Graphics;

import com.yofreke.alfred.Game;
import com.yofreke.alfred.level.tile.Tile;
import com.yofreke.alfred.states.IngameState;
import com.yofreke.alfred.tileEntity.LivingHomeTE;
import com.yofreke.alfred.tileEntity.upgrades.HomeTEUpgrade;

public class GuiBuildingMenu extends GuiElement {

	private IngameState parent;
	
	private float offsetX = 0;
	private float targOffsetX = 0;
	
	private LivingHomeTE target;
	private Tile targetTile;
	
	private String title = "";
	
	private HomeTEUpgrade residentUpgrade;
	
	public GuiBuildingMenu(IngameState parent, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.parent = parent;
//		setScale(2.5f);
		
		// overwrite x to use width
		this.x = (int) (Game.WIDTH - w * scale - 4);
		
		addElement(
			new GuiHomeUpgradeButton(parent.purse, 10, 140, 24, 24, 1)
				.setUpgrade(residentUpgrade = new HomeTEUpgrade("Add Resident", new Object[]{ "wood", 10, "food", 20 }))
				.setImage(parent.guiImage.getSubImage(120, 48, 24, 24))
		);
		
//		targOffsetX = -800;
	}
	
	public void hide() {
		targOffsetX = width * scale + 4;
	}
	
	public void show(LivingHomeTE target) {
		this.target = target;
		this.targetTile = Tile.tiles[target.level.getTile(target.x, target.y)];
		targOffsetX = 0;
		
		title = targetTile.name;
	}
	
	public boolean onElementClicked(int id) {
		if(id == 1) {
			if(residentUpgrade.canPurchase()) {
				residentUpgrade.deductCosts();
				target.upgradeCapacity();
			}
			return true;
		}
		return false;
	}
	
	public void draw(Graphics g){
		g.pushTransform();
		offsetX += (targOffsetX - offsetX) * 0.1;
		g.translate(offsetX, 0);
		
		IngameState.draw9Tile(g, IngameState.solidBackground, x, y, (int) (width * scale), (int) (height * scale));
		super.draw(g);
		
		g.pushTransform();
		g.translate(x, y);
		g.scale(2, 2);
		FontRenderer.renderString(g, title, 10, 10);
		if(target != null) {
			FontRenderer.renderString(g, "Resident Count: " + target.getResidentCount(), 20, 22);
			
			String s = target.getCollectionType();
			if(s != null) {
				parent.purse.drawIcon(g, s, 20, 36);
				FontRenderer.renderString(g, "Collected: " + target.getCollectedAmount(), 40, 38);
			}
		}
		
		g.popTransform();
		g.popTransform();
	}
}

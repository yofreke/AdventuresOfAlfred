package com.yofreke.alfred.gui;

import org.newdawn.slick.Graphics;

import com.yofreke.alfred.tileEntity.upgrades.HomeTEUpgrade;

public class GuiHomeUpgradeButton extends GuiButton {

	private HomeTEUpgrade upgrade;
	
	public GuiHomeUpgradeButton(int x, int y, int w, int h, int id) {
		super(x, y, w, h, id);	
	}
	
	public GuiHomeUpgradeButton setUpgrade(HomeTEUpgrade upgrade) {
		this.upgrade = upgrade;
		return this;
	}
	
	public void draw(Graphics g) {
		super.draw(g);
		
		if(upgrade != null) {
			g.pushTransform();
			g.translate(x, y);
			g.scale(2, 2);
			FontRenderer.renderString(g, upgrade.name, 35, 10);
			g.popTransform();
		}
	}
}

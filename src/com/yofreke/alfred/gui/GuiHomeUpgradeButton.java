package com.yofreke.alfred.gui;

import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.yofreke.alfred.Purse;
import com.yofreke.alfred.tileEntity.upgrades.HomeTEUpgrade;

public class GuiHomeUpgradeButton extends GuiButton {

	private Purse purse;
	private HomeTEUpgrade upgrade;
	
	public GuiHomeUpgradeButton(Purse purse, int x, int y, int w, int h, int id) {
		super(x, y, w, h, id);	
		this.purse = purse;
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
			FontRenderer.renderString(g, upgrade.name, 35, 2);
			
			// TODO: abstract this into the purse so that it can be used for hovering on buildings in the building menu
			int drawX = 35;
			for (Map.Entry<String, Integer> entry : upgrade.costs.entrySet()) {
				String name = entry.getKey();
				int cost = entry.getValue();
				
				purse.drawIcon(g, name, drawX, 14);
				FontRenderer.renderString(g, "\u8353" + ((purse.balance(name) > cost) ? '2' : '1') + cost, drawX + 20, 17);
				drawX += 40;
			}
			
			g.popTransform();
		}
	}
}

package com.yofreke.alfred.gui;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import com.yofreke.alfred.Game;
import com.yofreke.alfred.Utils;
import com.yofreke.alfred.states.IngameState;

public class GuiResourcesBar extends GuiElement {
	
	private static Rectangle WOOD_RECT = new Rectangle(4, 6, 17, 13);
	
	private IngameState igs;
	
	private Rectangle woodRect;
	private ArrayList<String> woodHelptext;
	
	public GuiResourcesBar(IngameState igs, int x, int y) {
		super(x, y, 160, 24);
		this.igs = igs;
		scale = 1.8f;
		this.y = (int) (Game.HEIGHT - height * scale);
		
		woodRect = Utils.duplicateRect(WOOD_RECT);
		woodRect.scaleGrow(scale, scale);
		woodRect.setLocation(WOOD_RECT.getX() + this.x, WOOD_RECT.getY() + this.y);
		woodHelptext = new ArrayList<String>();
		woodHelptext.add(FontRenderer.COLOR_IDENT+"5Giggity.");
	}

	public void draw(Graphics g){
		g.pushTransform();
		g.translate(x, y);
		g.scale(scale, scale);
		g.drawImage(image.getSubImage(0, 0, width, height), 0, 0);
		g.drawImage(image.getSubImage(0, height+1, width, height), 0, 0);
		FontRenderer.renderString(g, ""+IngameState.wood, 23, 8);
		FontRenderer.renderString(g, ""+IngameState.rock, 70, 8);
		FontRenderer.renderString(g, ""+IngameState.food, 119, 8);
		g.popTransform();

		int mouseX = IngameState.getMouseX();
		int mouseY = IngameState.getMouseY();
		if(woodRect.contains(mouseX, mouseY)){
			IngameState.drawHelpBox(g, mouseX + 8, mouseY - 16, (int) (150), (int) ((woodHelptext.size() + 1.5f) * FontRenderer.FONT_HEIGHT), woodHelptext);
		}
	}
}

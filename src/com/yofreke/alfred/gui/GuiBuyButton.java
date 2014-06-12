package com.yofreke.alfred.gui;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import com.yofreke.alfred.level.tile.BuildingTile;
import com.yofreke.alfred.level.tile.Tile;
import com.yofreke.alfred.states.IngameState;

public class GuiBuyButton extends GuiButton {

	private int buyType;
	private BuildingTile buildingTile;
	
	public GuiBuyButton(int x, int y, int w, int h, int buyType) {
		super(x, y, w, h);
		this.buyType = buyType;
		Tile t = Tile.tiles[buyType];
		if(t instanceof BuildingTile){
			buildingTile = (BuildingTile) t;
		}
	}

	public void addHoverText(ArrayList<String> al){
		al.add("Buy: "+buildingTile.name);
		al.add(FontRenderer.COLOR_IDENT+"1  $400");
	}
	
	public void draw(Graphics g){
		super.draw(g);
		if(isHovering){
			int boxX = IngameState.getMouseX() + 8;
			int boxY = IngameState.getMouseY() - 16;
			ArrayList<String> costAl = new ArrayList<String>();
			addHoverText(costAl);
			IngameState.drawHelpBox(g, boxX, boxY, (int) (150), (int) ((costAl.size() + 1.5f) * FontRenderer.FONT_HEIGHT), costAl);
		}
	}
	
	public BuildingTile getBuildingTile(){
		return buildingTile;
	}
}

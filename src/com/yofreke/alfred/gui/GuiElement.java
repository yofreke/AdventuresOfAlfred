package com.yofreke.alfred.gui;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class GuiElement {

	protected GuiElement parent;
	protected ArrayList<GuiElement> elementList = new ArrayList<GuiElement>();
	public int id = 0;
	
	public float scale = 2.5f;
	public int x, y;
	public int oWidth, oHeight;
	public int width, height;
	protected Image image;
	
	public boolean isHovering;
	
	public GuiElement(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.oWidth = this.width = w;
		this.oHeight = this.height = h;
	}
	
	public GuiElement setID(int id) {
		this.id = id;
		return this;
	}
	
	public void setScale(float f){
		this.scale = f;
		width = (int) (scale * oWidth);
		height = (int) (scale * oHeight);
	}
	
	public GuiElement setImage(Image image){
		this.image = image;
		return this;
	}
	
	public void addElement(GuiElement element){
		elementList.add(element);
		element.parent = this;
	}
	
	protected void drawElements(Graphics g){
		for(GuiElement element : elementList){
			element.draw(g);
			// TODO: MOVE THIS OUT
			/*if(element.isHovering && element instanceof GuiBuyButton){
				int boxX = mouseX + 8;
				int boxY = mouseY - 16;
				ArrayList<String> costAl = new ArrayList<String>();
				((GuiBuyButton) element).addHoverText(costAl);
				drawHelpBox(g, boxX, boxY, (int) (150), (int) ((costAl.size() + 1.5f) * FontRenderer.FONT_HEIGHT), costAl);
			}*/
		}
	}
	
	public boolean onElementClicked(int id) {
		return false;
	}
	
	public GuiElement getClickedElement(int x, int y){
		for(GuiElement element : elementList){
			if(element.isOver(x, y)) {
				if(!this.onElementClicked(element.id))
					return element.getClickedElement(x, y);
			}
		}
		return this;
	}
	
	public boolean updateHovering(int x, int y){
		boolean flag = false;
		for(GuiElement element : elementList){
			element.isHovering = element.isOver(x, y);
			if(element.updateHovering(x, y) || element.isHovering){
				flag = true;
			}
		}
		return flag;
	}
	
	public float getDrawX() {
		if(this.parent != null)
			return this.parent.getDrawX() + this.x;
		return this.x;
	}
	public float getDrawY() {
		if(this.parent != null)
			return this.parent.getDrawY() + this.y;
		return this.y;
	}
	
	public void draw(Graphics g){
		g.pushTransform();
		g.translate(x, y);
		if(image != null) {
			g.pushTransform();
			g.scale(scale, scale);
			g.drawImage(image, 0, 0);
			g.popTransform();
		}
		drawElements(g);
		g.popTransform();
	}
	
	public boolean isOver(int mx, int my){
		float x = getDrawX();
		float y = getDrawY();
		return mx >= x && my >= y && mx <= x + width * scale && my <= y + height * scale;
	}
}

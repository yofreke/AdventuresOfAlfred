package com.yofreke.alfred.states;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.yofreke.alfred.Game;
import com.yofreke.alfred.Purse;
import com.yofreke.alfred.gui.FontRenderer;
import com.yofreke.alfred.gui.GuiBuildingMenu;
import com.yofreke.alfred.gui.GuiBuyBar;
import com.yofreke.alfred.gui.GuiBuyButton;
import com.yofreke.alfred.gui.GuiElement;
import com.yofreke.alfred.gui.GuiResourcesBar;
import com.yofreke.alfred.level.Chunk;
import com.yofreke.alfred.level.Level;
import com.yofreke.alfred.level.tile.BuildingTile;
import com.yofreke.alfred.level.tile.Tile;
import com.yofreke.alfred.tileEntity.LivingHomeTE;
import com.yofreke.alfred.tileEntity.TileEntity;

public class IngameState extends AlfredGameState {
	
	private static Color IGNORE_OVERLAY_COLOR = new Color(0.8f,0.8f,0.8f,0.6f);
	private static Color BLOCKED_OVERLAY_COLOR = new Color(0.85f,0.4f,0.4f,0.6f);
	
	public static Purse purse = new Purse();
	
	private static boolean LMB, RMB; 
	private static int mouseX = 0, mouseY = 0;
	private static int prevMouseX = 0, prevMouseY = 0;
	private static int mouseTileX = 0, mouseTileY = 0;
	
	public Level level;
	private BuildingTile heldTile;

	public static Image guiImage;
	public static Image helpTextBackground;
	public static Image solidBackground;
	
	private boolean isHovering = false;
	
	private GuiElement resourcesBar;
	private GuiBuildingMenu buildingMenu;
	
	private boolean showingTileInfo = false;
	private int tileInfoX, tileInfoY;
	private ArrayList<String> tileInfoStringList = new ArrayList<String>();
	
	public IngameState() {
		
	}

	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		Tile.init();
		FontRenderer.init();
		level = new Level();
		level.init(arg0, arg1);
		
		this.initGUI();
	}
	
	protected void initGUI() throws SlickException {
		// Set the static gui image.  This is sorta hacky, needs to be better.
		guiImage = new Image("res/gui.png", false, Image.FILTER_NEAREST);
		helpTextBackground = guiImage.getSubImage(0, 64, 16, 16);
		solidBackground = guiImage.getSubImage(0, 80, 16, 16);
		purse.initImages(guiImage);
		
		resourcesBar = new GuiResourcesBar(this, 0, 0).setImage(guiImage);
		addElement(resourcesBar);
		
		GuiElement buyBar = new GuiBuyBar(400, 590, 160, 24).setImage(guiImage.getSubImage(0, 0, 160, 24));
		buyBar.addElement(new GuiBuyButton(2, 10, 32, 16, Tile.lumberjack.id, -1).setImage(guiImage.getSubImage(0, 48, 32, 16)));
		addElement(buyBar);
		
		buildingMenu = (GuiBuildingMenu) new GuiBuildingMenu(this, Game.WIDTH - 280, 175, 120, 118);
		buildingMenu.hide();
		addElement(buildingMenu);
	}
	
	public void mouseMoved(int px, int py, int x, int y) {
		prevMouseX = px;
		prevMouseY = py;
		mouseX = x;
		mouseY = y;
		mouseTileX = level.toTileX(mouseX);
		mouseTileY = level.toTileY(mouseY);
		isHovering = updateHovering(x, y);
	}
	public void mouseClicked(int mb, int x, int y, int arg3) {
		GuiElement elem = getClickedElement(x, y);
		System.out.println("elem "+elem);
		if(elem != null){
			if(heldTile != null){
				heldTile = null;
			}
			if(elem instanceof GuiBuyButton){
				heldTile = ((GuiBuyButton) elem).getBuildingTile();
			}
		} else if(heldTile != null){
			if(canPlaceHeld(mouseTileX, mouseTileY)){
				level.setTile(mouseTileX, mouseTileY, heldTile.id);
				heldTile = null;
			}
		} else {
			if(showingTileInfo) {
				hideTileInfo();
			} else {
				showTileInfo(mouseTileX, mouseTileY);
			}
			
			level.mouseClicked(mb, x, y, arg3);
		}
	}
	public static int getMouseX() { return mouseX; }
	public static int getMouseY() { return mouseY; }
	public void mouseDragged(int px, int py, int x, int y) {
		level.mouseDragged(x, y, x - px, y - py);
	}
	public void mouseWheelMoved(int dir) {
		level.mouseWheelMoved(dir);
	}
	
	public void mousePressed(int mb, int x, int y) {
		if(mb == 0){
			LMB = true;
		} else if(mb == 1){
			RMB = true;
		}
		level.mousePressed(mb, x, y);
	}
	public void mouseReleased(int mb, int x, int y) {
		if(mb == 0){
			LMB = false;
		} else if(mb == 1){
			RMB = false;
		}
		level.mouseReleased(mb, x, y);
	}
	
	public static boolean isRMB() { return RMB; }
	public static boolean isLMB() { return LMB; }
	
	private boolean canPlaceHeld(int x, int y){
		if(level.isBlocked(x, y)) return false;
		int[] heldExtended = heldTile.getIgnore();
		if(heldExtended != null){
			for(int i = 0; i < heldExtended.length; i+=2){
				if(level.isBlocked(x + heldExtended[i], y + heldExtended[i+1])){
					return false;
				}
			}
		}
		return true;
	}
	
	private void showTileInfo(int x, int y) {
		showingTileInfo = true;
		tileInfoX = mouseTileX;
		tileInfoY = mouseTileY;
		
		int hoverTileId = this.level.getTile(mouseTileX, mouseTileY);
		Tile hoverTile = Tile.tiles[hoverTileId];
		
		tileInfoStringList.clear();
		
		tileInfoStringList.add("Tile Details");
		tileInfoStringList.add("  [" + hoverTileId + "] " + hoverTile.name);
		tileInfoStringList.add("  Tile Damage: " + level.getDamage(mouseTileX, mouseTileY) + " / " + hoverTile.getMaxDamage());
		if(hoverTile instanceof BuildingTile) {
			TileEntity te = level.getTileEntity(mouseTileX, mouseTileY);
			if(te != null) {
				LivingHomeTE homete = (LivingHomeTE) te;
				tileInfoStringList.add("  Residents: " + homete.getResidentCount());
				this.buildingMenu.show(homete);
			}
		}
	}
	private void hideTileInfo() {
		showingTileInfo = false;
		this.buildingMenu.hide();
	}
	
	public void update(GameContainer container, StateBasedGame arg1, int arg2) throws SlickException {	
		level.update(container, arg1, arg2);
		
		Input i = container.getInput();
		if(i.isKeyPressed(Input.KEY_1)) {
			Game.DEBUG = !Game.DEBUG;
		} else if(i.isKeyPressed(Input.KEY_F5)){
			this.level.reloadMap();
			this.clearElements();
			this.initGUI();
		}
		
		super.update(container, arg1, arg2);
	}
	
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		level.render(arg0, arg1, g, heldTile != null);
		g.pushTransform();
		float mockSize = Chunk.TILE_SIZE * level.curZoom;
		g.translate(
				(Game.WIDTH * 0.5f % mockSize + level.mapOffsetX * level.curZoom % mockSize),
				(Game.HEIGHT * 0.5f % mockSize + level.mapOffsetY * level.curZoom % mockSize)
			);
		if(heldTile != null && !isHovering){
			this.drawAvailableTiles(g, mockSize);
		}
		g.popTransform();
		
		if(this.showingTileInfo) {
			g.pushTransform();
			this.drawTileInfoOverlay(g);
			g.popTransform();
		}
		
		//TODO: drawElements(g);
		for(GuiElement element : elementList){
			element.draw(g);
		}
		this.drawMinimap(g);
		
		if(heldTile != null){
			g.pushTransform();
			g.translate(mouseX, mouseY);
			g.setColor(new Color(0.5f,1,1,0.5f));
			g.scale(level.curZoom, level.curZoom);
			heldTile.renderHeldTile(g);
			g.popTransform();
		}
		
		g.setColor(Color.magenta);
		Point p = level.stageToTile(mouseX, mouseY);
		g.drawString("global ("+mouseX+","+mouseY+")", 350, 4);
		if(level != null) g.drawString("local ("+(int)level.toLocalX(mouseX)+","+(int)level.toLocalY(mouseY)+")", 350, 24);
		g.drawString("("+p.x+","+p.y+") "+level.getTile(p.x, p.y), 350, 44);
		g.setColor(Color.white);
	}
	
	public void drawTileInfoOverlay(Graphics g) {
		g.pushTransform();
		g.setColor(IGNORE_OVERLAY_COLOR);
		this.level.setupLevelTransform(g);
		
		g.scale(Chunk.TILE_SIZE, Chunk.TILE_SIZE);
		g.fillRect(tileInfoX, tileInfoY, 1, 1);
		g.popTransform();
		
		IngameState.drawHelpBox(g, Game.WIDTH - 200, 475, (int) (150), (int) ((5 + 1.5f) * FontRenderer.FONT_HEIGHT), tileInfoStringList);
	}
	
	public void drawAvailableTiles(Graphics g, float tileSize) {
		// draw blocked and available tiles
		g.pushTransform();
		int drawRadius = 10;
		g.setColor(IGNORE_OVERLAY_COLOR);
		float translateX = mouseX - mouseX % tileSize;
		float translateY = mouseY - mouseY % tileSize;
		float localX = level.toLocalX(translateX);
		float localY = level.toLocalY(translateY);
		boolean canPlace = canPlaceHeld((int)(localX / Chunk.TILE_SIZE), (int)(localY / Chunk.TILE_SIZE));
		g.translate(translateX, translateY);
		g.scale(tileSize, tileSize);
		for (int x = -drawRadius; x < drawRadius; x++) {
            int h = (int)(drawRadius * 0.85f * Math.sin(Math.acos((double)x / (double)drawRadius)))/* - (x == 0 ? 1 : 0)*/;
            for (int y = -h; y < h; y++){
            	if(heldTile.isInIgnore(x, y) || (x == 0 && y == 0)){
            		if(canPlace){
						g.fillRect(x, y, 1, 1);
            		} else {
                		g.setColor(BLOCKED_OVERLAY_COLOR);
                		g.fillRect(x, y, 1, 1);
                		g.setColor(IGNORE_OVERLAY_COLOR);
            		}
            	} else {
            		if(level.isBlocked(localX + x * Chunk.TILE_SIZE, localY + y * Chunk.TILE_SIZE)){
            			g.fillRect(x, y, 1, 1);
            		} else {
						//g.drawRect(x * mockSize, y * mockSize, mockSize, mockSize);
					}
            	}
            }
        }
		g.popTransform();
	}
	
	public void drawMinimap(Graphics g) {
		float gScale = 4.5f;
		g.pushTransform();
		g.translate(Game.WIDTH - 32 * gScale, 0);
		g.scale(gScale, gScale);
		g.drawImage(guiImage.getSubImage(224, 0, 32, 32), 0, 0);
		g.popTransform();
	}
	
	public static void drawHelpBox(Graphics g, int x, int y, int width, int height, ArrayList<String> stringAl){
		// TODO: is this the right min values?
		float boxFontScale = 2f;
		width *= boxFontScale;
		height *= boxFontScale;
		width = Math.max(width, 24);
		height = Math.max(height, 24);
		int k = 4;
		if(x + width > Game.WIDTH - k){
			x = Game.WIDTH - width - k;
		} else if(x < k){
			x = k;
		}
		if(x < 288 && y + height > Game.HEIGHT - 44){
			y = Game.HEIGHT - height - 44;
		} else if(y + height > Game.HEIGHT - k){
			y = Game.HEIGHT - height - k;
		} else if(y < k){
			y = k;
		}
		
		draw9Tile(g, helpTextBackground, x, y, width, height);
		g.pushTransform();
		g.translate(x, y);
		g.scale(boxFontScale,boxFontScale);
		int fontDrawX = (int) ((8) / boxFontScale);
		for(int i = 0; i < stringAl.size(); i++){
			FontRenderer.renderString(g, stringAl.get(i), fontDrawX, (int) ((i * (FontRenderer.FONT_HEIGHT * boxFontScale + 4) + 8) / boxFontScale));
		}
		g.popTransform();
	}
	
	public static void draw9Tile(Graphics g, Image image, int x, int y, int width, int height) {
		g.pushTransform();
		g.translate(x, y);
		float scale = 3.0f;
		g.scale(scale, scale);
		int popupW = (int) (width / scale);
		int popupH = (int) (height / scale);
		// corners
		g.drawImage(image, 0, 0, 4, 4, 0, 0, 4, 4);
		g.drawImage(image, popupW - 4, 0, popupW, 4, 12, 0, 16, 4);
		g.drawImage(image, 0, popupH - 4, 4, popupH, 0, 12, 4, 16);
		g.drawImage(image, popupW - 4, popupH - 4, popupW, popupH, 12, 12, 16, 16);
		// sides
		g.drawImage(image, 0, 4, 4, popupH - 4, 0, 4, 4, 12);
		g.drawImage(image, 4, 0, popupW - 4, 4, 4, 0, 12, 4);
		g.drawImage(image, 4, popupH - 4, popupW - 4, popupH, 4, 12, 12, 16);
		g.drawImage(image, popupW - 4, 4, popupW, popupH - 4, 12, 4, 16, 12);
		// center
		g.drawImage(image, 4, 4, popupW - 4, popupH - 4, 4, 4, 12, 12);
		// text
		g.popTransform();
	}
	
	public int getID() {
		return Game.PREGAME_STATE;
	}
}

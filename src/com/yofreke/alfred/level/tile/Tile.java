package com.yofreke.alfred.level.tile;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.yofreke.alfred.Perlin;
import com.yofreke.alfred.level.Chunk;
import com.yofreke.alfred.level.Level;

public class Tile {
	
	protected Random rand = new Random();
	protected static SpriteSheet texSheet;
	public static void init() throws SlickException{
		texSheet = new SpriteSheet(new Image("res/terrainSmall.png", false, Image.FILTER_NEAREST), Chunk.TILE_SIZE, Chunk.TILE_SIZE);
	}
	public static Tile[] tiles = new Tile[128];
	
	public int id;
	public int texIndex;
	private Color debugColor;
	private boolean canPass = true;
	protected boolean canDamage = false;
	public String name;
	
	protected int texX, texY;
	protected int renderPass = 0;
	protected int[] ignorePositions;
	
	public Tile(int id, int texIndex){
		this.id = id;
		this.texIndex = texIndex;
		texX = (int) (texIndex % 16) * Chunk.TILE_SIZE;
		texY = (int) (texIndex / 16) * Chunk.TILE_SIZE;
		
		debugColor = new Color(0.8f, 0, 0);
		name = "Tile "+String.valueOf(id);
		
		tiles[id] = this;
	}
	
	public Tile setName(String name) {
		this.name = name;
		return this;
	}
	
	public Tile setColor(float r, float g, float b){
		this.debugColor.r = r;
		this.debugColor.g = g;
		this.debugColor.b = b;
		return this;
	}
	public Color getColor(){
		return debugColor;
	}
	
	public boolean canPass(){
		return canPass;
	}
	public Tile setCanPass(boolean flag){
		canPass = flag;
		return this;
	}
	
	public void onAdd(Level level, int x, int y){
		updateIgnorePositions(level, x, y, true);
	}
	public void onRemove(Level level, int x, int y){
		updateIgnorePositions(level, x, y, false);
	}
	public void updateIgnorePositions(Level level, int x, int y, boolean flag){
		if(ignorePositions != null){
			for(int i = 0; i < ignorePositions.length; i+=2){
				level.setIgnore(x+ignorePositions[i], y+ignorePositions[i+1], flag);
			}
		}
	}
	
	public boolean isInIgnore(int x, int y){
		if(ignorePositions == null) return false;
		for(int i = 0; i < ignorePositions.length; i+=2){
			if(ignorePositions[i] == x && ignorePositions[i+1] == y){
				return true;
			}
		}
		return false;
	}
	
	public int[] getIgnore(){
		return ignorePositions;
	}
	
	public boolean renderOnPass(int pass){
		return pass == renderPass;
	}
	
	public boolean canDamage(){
		return canDamage;
	}
	
	public int getMaxDamage(){
		return 10;
	}
	
	public void degrade(Level level, int x, int y){
		
	}
	
	public Image getImage(){
		return texSheet.getSprite(texIndex % 16, (int)(texIndex / 16));
	}
	
	public void renderAt(Graphics g, Level level, int x, int y, int dx, int dy){
		renderNormAt(g, x, y, dx, dy);
	}
	public void renderNormAt(Graphics g, int x, int y, int dx, int dy){
		g.drawImage(getImage(), dx, dy);
	}
	
	public void renderSpecs(Graphics g, int x, int y, int dx, int dy){
		float noise = Perlin.Noise(x, y);
		g.drawImage(texSheet.getSubImage(32 + (int)(noise * 8) + 8, 0, 8, 8), dx, dy, getColor());
	}
	
	public static Image getImageForId(int id){
		Tile t = tiles[id];
		if(t == null){
			return invalid.getImage();
		}
		return t.getImage();
	}
	
	public static Color getColorForId(int id){
		Tile t = tiles[id];
		if(t == null){
			return invalid.getColor();
		}
		return t.getColor();
	}
	
	public static boolean touching(Level level, int x, int y, Tile t){
		int id = t.id;
		if(level.getTile(x+1, y) == id) return true;
		if(level.getTile(x-1, y) == id) return true;
		if(level.getTile(x, y+1) == id) return true;
		return level.getTile(x, y-1) == id;
	}
	
	public static Tile invalid = new Tile(0, 0).setColor(0.8f, 0, 0).setCanPass(false);
	public static Tile water = new WaterTile(1, 1).setColor(0, 0, 0.8f).setCanPass(false).setName("Water");
	public static Tile dirt = new DirtTile(2, 16).setColor(0.75f, 0.75f, 0.55f).setName("Dirt");
	public static Tile grass = new GrassTile(3, 64).setColor(0.2f, 0.85f, 0.2f).setName("Grass");
	public static Tile sand = new SandTile(4, 112).setColor(0.8f, 0.7f, 0.3f).setName("Sand");
	public static Tile tree = new TreeTile(15, 16).setColor(0.1f, 0.65f, 0.1f).setCanPass(false).setName("Tree");
	public static Tile stump = new TreeStumpTile(16, 16).setColor(0.55f, 0.65f, 0.35f).setName("Tree Stump");
	public static Tile flag = new FlagTile(17, 69).setColor(0.55f, 0.7f, 0.67f).setCanPass(false).setName("Flag");
	public static Tile gravel = new GravelTile(18, 208).setColor(0.55f, 0.4f, 0.3f).setName("Gravel");
	public static Tile stone = new StoneTile(20, 160).setColor(0.3f, 0.3f, 0.3f).setCanPass(false).setName("Stone");
	

	public static Tile lumberjack = new LumberjackTile(60, 37).setColor(0.7f, 0.3f, 0.3f).setCanPass(false);
}

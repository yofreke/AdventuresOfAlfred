package com.yofreke.alfred.level;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.davedes.FilterableImage;
import com.yofreke.alfred.Game;
import com.yofreke.alfred.Perlin;
import com.yofreke.alfred.level.tile.ITileEntityPair;
import com.yofreke.alfred.level.tile.Tile;
import com.yofreke.alfred.tileEntity.TileEntity;

public class Chunk {
	private static Perlin perlin = new Perlin();
	private static int minTVal = 1;
	private static Color BLOCKED_DRAW_COLOR = new Color(1,1,1,0.35f);
	//private static int maxTVal = 5;
	
	public static int CHUNK_SIZE = 32;
	public static int TILE_SIZE = 8;
	public static int IMG_BUFF = 32;
	
	private Level level;
	
	private int chunkX, chunkY;
	public int tOffsetX, tOffsetY;
	private int[] tileArray;
	private boolean[] ignoreArray;
	private int[] dataArray;
	private int[] damageArray;
	
	private HashMap<Integer, TileEntity> tileEntities = new HashMap<Integer, TileEntity>();
	
	private FilterableImage mapImage;
	//private FilterableImage mapImageBlocked;
	public boolean redraw = true;
	
	public Chunk(Level igs, int x, int y){
		this.level = igs;
		this.chunkX = x;
		this.chunkY = y;
		
		this.tOffsetX = chunkX * CHUNK_SIZE;
		this.tOffsetY = chunkY * CHUNK_SIZE;
		
		tileArray = new int[CHUNK_SIZE * CHUNK_SIZE];
		dataArray = new int[tileArray.length];
		damageArray = new int[dataArray.length];
		ignoreArray = new boolean[tileArray.length];
	}
	
	public void genChunk(Random rand, boolean initImage) throws SlickException {
		if(initImage && mapImage == null) {
			checkMapImage();
		}
		
		perlin = new Perlin();
		int x, y, i, j, k;
		float pscale = 0.005f;
		for(y = 0; y < CHUNK_SIZE; y++){
			float dist = Math.abs(Level.T_TALL * 0.5f - (y + tOffsetY));
			float fmod = dist * dist * 0.00015f;
			for(x = 0; x < CHUNK_SIZE; x++){
				i = y * CHUNK_SIZE + x;
				float f = perlin.PerlinNoise_2D((tOffsetX  + x) * pscale, (tOffsetY + y) * pscale) * 1F;
				f += fmod;
				k = 0;
				if(f < minTVal){
					j = Tile.water.id;
				}
				else if(f < 2){
					j = Tile.sand.id;
				}
				else if(f < 4.5){
					j = Tile.grass.id;
				}
				else if(f < 7){
					j = Tile.tree.id;
				}
				else {
					j = Tile.tree.id;
					k = 1;
					//j = Tile.dirt.id;
					//j = Tile.invalid.id;
				}
				tileArray[i] = j;
				dataArray[i] = k;
			}
		}
		
		genRocks(rand);
		//updateTouching();
		redraw = true;
	}
	
	private void checkMapImage() throws SlickException{
		if(mapImage != null) return;
		
		mapImage = new FilterableImage(CHUNK_SIZE * TILE_SIZE + IMG_BUFF * 2, CHUNK_SIZE * TILE_SIZE + IMG_BUFF * 2, Image.FILTER_NEAREST);
		//mapImageBlocked = new FilterableImage(CHUNK_SIZE * TILE_SIZE + IMG_BUFF * 2, CHUNK_SIZE * TILE_SIZE + IMG_BUFF * 2, Image.FILTER_NEAREST);
		Graphics g = mapImage.getGraphics();
		g.flush();
		//mapImageBlocked.getGraphics().flush();
	}
	
	public void genRocks(Random rand){
		int i;
		RockGenerator rockGen = new RockGenerator();
		for(i = 0; i < (level.debugView ? 50 : 3); i++){
			int x = rand.nextInt(CHUNK_SIZE);
			int y = rand.nextInt(CHUNK_SIZE);
			//System.out.println(tOffsetX+","+tOffsetY+":"+x+","+y);
			rockGen.genAt(level, rand, x + tOffsetX, y + tOffsetY);
		}
	}
	
	public int getTile(int x, int y){
		return tileArray[y * CHUNK_SIZE + x];
	}
	public void setTileAndData(int x, int y, int id, int data){
		int i = y * CHUNK_SIZE + x;
		Tile t = Tile.tiles[tileArray[i]];
		// remove old tile
		if(t instanceof ITileEntityPair){
			tileEntities.get(i).onRemove();
		}
		t.onRemove(level, x + tOffsetX, y + tOffsetY);
		//
		
		tileArray[i] = id;
		dataArray[i] = data;
		damageArray[i] = 0;
		
		t = Tile.tiles[id];
		if(t instanceof ITileEntityPair){
			tileEntities.put(i, ((ITileEntityPair)t).getEntity(level, x + tOffsetX, y + tOffsetY));
		}
		t.onAdd(level, x + tOffsetX, y + tOffsetY);
		redraw = true;
	}
	public TileEntity getTileEntity(int x, int y) {
		int i = y * CHUNK_SIZE + x;
		if(tileEntities.containsKey(i))
			return tileEntities.get(i);
		return null;
	}
	public boolean getIgnore(int localX, int localY){
		return ignoreArray[localY * CHUNK_SIZE + localX];
	}
	public void setIgnore(int localX, int localY, boolean flag){
		ignoreArray[localY * CHUNK_SIZE + localX] = flag;
		redraw = true;
	}
	public int getData(int x, int y){
		return dataArray[y * CHUNK_SIZE + x];
	}
	public void setData(int x, int y, int data){
		dataArray[y * CHUNK_SIZE + x] = data;
		redraw = true;
	}
	public int getDamage(int x, int y){
		return damageArray[y * CHUNK_SIZE + x];
	}
	/**
	 * returns tile health (0.0f - 1.0f)
	 */
	public float damageTile(int x, int y, int i){
		int index = y * CHUNK_SIZE + x;
		damageArray[index] += i;
		//redraw = true;
		Tile t = Tile.tiles[tileArray[index]];
		float tileHealth = 1 - (float) damageArray[index] / t.getMaxDamage();
		if(tileHealth <= 0){
			t.degrade(level, x + tOffsetX, y + tOffsetY);
			return 0f;
		}
		return tileHealth;
	}
	
	public boolean isBlocked(int localX, int localY){
		if(!Tile.tiles[getTile(localX, localY)].canPass()) return true;
		if(ignoreArray[localY * CHUNK_SIZE + localX]) return true;
		return false;
	}
	
	public void updateMapImage() throws SlickException{
		redraw = false;
		checkMapImage();
		Graphics g = mapImage.getGraphics();
		g.clear();
		//Graphics blockedG = mapImageBlocked.getGraphics();
		//blockedG.clear();
		// draw normal chunk
		g.translate(IMG_BUFF, IMG_BUFF);
		
		if(tileArray == null){
			g.setColor(Color.lightGray);
			int pixelSize = CHUNK_SIZE * TILE_SIZE;
			g.fillRect(0, 0, pixelSize, pixelSize);
			return;
		}
		
		//blockedG.setColor(Color.black);
		int x, y, i;
		boolean useImages = !level.debugView;
		for(int pass = 0; pass < 2; pass++){
			for(y = 0; y < CHUNK_SIZE; y++){
				for(x = 0; x < CHUNK_SIZE; x++){
					i = y * CHUNK_SIZE + x;
					
					if(useImages){
						//img = Tile.getImageForId(tileArray[i]);
						//g.drawImage(img, x * TILE_SIZE, y * TILE_SIZE);
						Tile t = Tile.tiles[tileArray[i]];
						if(t.renderOnPass(pass)){
							t.renderAt(g, level, x + tOffsetX, y + tOffsetY, x * TILE_SIZE, y * TILE_SIZE);
						}
					} else {
						g.setColor(Tile.getColorForId(tileArray[i]));
						g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					}
				}
			}
		}
		g.flush();
	}
	
	public void update(){
		Iterator<Entry<Integer, TileEntity>> it = tileEntities.entrySet().iterator();
	    while (it.hasNext()) {
	        TileEntity te = it.next().getValue();
	        te.update();
	        if(te.isDead){
	        	it.remove();
	        }
	    }

	}
	
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g, boolean showBlocked) throws SlickException {
		if(redraw){
			updateMapImage();
			//System.out.println("draw "+chunkX+" "+chunkY);
		}
		g.pushTransform();
		g.translate(tOffsetX * TILE_SIZE - IMG_BUFF, tOffsetY * TILE_SIZE - IMG_BUFF);
		g.drawImage(mapImage, 0, 0);
		// Debugging
		if(Game.DEBUG) {
			g.setColor(Color.cyan);
			g.drawRect(IMG_BUFF, IMG_BUFF, mapImage.getWidth() - 2 * IMG_BUFF, mapImage.getHeight() - 2 * IMG_BUFF);
		}
		
		if(showBlocked){
			//g.drawImage(mapImageBlocked, 0, 0, BLOCKED_DRAW_COLOR);
		}
		g.popTransform();
		for(TileEntity te : tileEntities.values()){
			te.render(g);
		}
	}
}

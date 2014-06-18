package com.yofreke.alfred.level;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import com.yofreke.alfred.Game;
import com.yofreke.alfred.effect.ActionEffect;
import com.yofreke.alfred.effect.Effect;
import com.yofreke.alfred.entity.Entity;
import com.yofreke.alfred.entity.Living;
import com.yofreke.alfred.entity.Lumberjack;
import com.yofreke.alfred.level.tile.Tile;
import com.yofreke.alfred.states.IngameState;
import com.yofreke.alfred.tileEntity.TileEntity;

public class Level {
	public Random rand = new Random();
	public static int T_WIDE = 1024;
	public static int T_TALL = 512;

	private int cWide;
	private int cTall;
	
	public float mapOffsetX = -150, mapOffsetY = -150;
	public float targMapX = mapOffsetX, targMapY = mapOffsetY;
	public float curZoom = 2f;
	public float targZoom = curZoom;
	
	public int playerPosX, playerPosY;
	public int lastPlayerPosX, lastPlayerPosY;
	
	private long lastTick = 0;
	private float partialTick = 0f;
	
	public Chunk[] chunks;
	public boolean debugView = false;
	
	private boolean isLoading;
	private boolean doImageLoad;
	private int partialLoadI;
	private Random generatorRandom;
	
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Effect> effects = new ArrayList<Effect>();
	
	public Lumberjack testEnt;
	private Rectangle selectionRect;
	private ArrayList<Living> selectedEntities = new ArrayList<Living>();
	
	public Level() {
		lastTick = System.currentTimeMillis();
	}

	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		loadMap();
	}
	
	private void loadMap() throws SlickException {
		cWide = (int) (T_WIDE / Chunk.CHUNK_SIZE);
		cTall = (int) (T_TALL / Chunk.CHUNK_SIZE);
		chunks = new Chunk[cWide * cTall];
		generatorRandom = new Random(51654654);
		
		int i;
		for(i = 0; i < chunks.length; i++){
			chunks[i] = new Chunk(this, (int) (i % cWide), (int) (i / cWide));
		}
		
		partialLoadI = 0;
		isLoading = true;
	}
	
	private void partialLoad() throws SlickException{
		long startTime = System.currentTimeMillis();
		while(partialLoadI < chunks.length){
			chunks[partialLoadI].genChunk(generatorRandom, doImageLoad);
			
			partialLoadI++;
			if(System.currentTimeMillis() - startTime > 25) {
				return;
			}
		}
		isLoading = doImageLoad = false;
		// done loading
		

		LevelUtils.fillCircle(this, 15, 15, 8, Tile.gravel.id);
		setTile(15, 15, Tile.flag.id);
		setTile(17, 17, Tile.lumberjack.id);
	}
	
	public void clearMap(){
		entities.clear();
		effects.clear();
	}
	
	public int getChunkIndex(int chunkX, int chunkY){
		return (int) (chunkY / Chunk.CHUNK_SIZE) * cWide + (int) (chunkX / Chunk.CHUNK_SIZE);
	}
	public Chunk getChunkAt(int x, int y){
		Chunk c = chunks[getChunkIndex(x, y)];
		return c;
	}
	
	public int getTile(int x, int y){
		if(x < 0 || y < 0 || x >= T_WIDE || y >= T_TALL) return 0;
		Chunk c = getChunkAt(x, y); 
		return c.getTile(x - c.tOffsetX, y - c.tOffsetY);
	}
	public TileEntity getTileEntity(int x, int y) {
		if(x < 0 || y < 0 || x >= T_WIDE || y >= T_TALL) return null;
		Chunk c = getChunkAt(x, y); 
		return c.getTileEntity(x - c.tOffsetX, y - c.tOffsetY);
	}
	public void setTile(int x, int y, int id){
		if(x < 0 || y < 0 || x >= T_WIDE || y >= T_TALL) return;
		Chunk c = getChunkAt(x, y); 
		c.setTileAndData(x - c.tOffsetX, y - c.tOffsetY, id, 0);
	}
	public void setTileAndData(int x, int y, int id, int data){
		if(x < 0 || y < 0 || x >= T_WIDE || y >= T_TALL) return;
		Chunk c = getChunkAt(x, y); 
		c.setTileAndData(x - c.tOffsetX, y - c.tOffsetY, id, data);
	}
	
	public boolean getIgnore(int x, int y){
		if(x < 0 || y < 0 || x >= T_WIDE || y >= T_TALL) return false;
		Chunk c = getChunkAt(x, y); 
		return c.getIgnore(x - c.tOffsetX, y - c.tOffsetY);
	}
	public void setIgnore(int x, int y, boolean flag){
		if(x < 0 || y < 0 || x >= T_WIDE || y >= T_TALL) return;
		Chunk c = getChunkAt(x, y); 
		c.setIgnore(x - c.tOffsetX, y - c.tOffsetY, flag);
	}
	
	public int getData(int x, int y){
		if(x < 0 || y < 0 || x >= T_WIDE || y >= T_TALL) return 0;
		Chunk c = getChunkAt(x, y); 
		return c.getData(x - c.tOffsetX, y - c.tOffsetY);
	}
	public void setData(int x, int y, int data){
		if(x < 0 || y < 0 || x >= T_WIDE || y >= T_TALL) return;
		Chunk c = getChunkAt(x, y); 
		c.setData(x - c.tOffsetX, y - c.tOffsetY, data);
	}
	
	public int getDamage(int x, int y){
		if(x < 0 || y < 0 || x >= T_WIDE || y >= T_TALL) return 0;
		Chunk c = getChunkAt(x, y); 
		return c.getDamage(x - c.tOffsetX, y - c.tOffsetY);
	}
	public void damageTile(int x, int y, int i){
		if(x < 0 || y < 0 || x >= T_WIDE || y >= T_TALL) return;
		Chunk c = getChunkAt(x, y); 
		float tileHealth = c.damageTile(x - c.tOffsetX, y - c.tOffsetY, i);
		addEffect(new ActionEffect((x * Chunk.TILE_SIZE) + 4 + (0.5f - rand.nextFloat()) * 5, (y * Chunk.TILE_SIZE) + 4, new Color(0.9f - tileHealth * 0.8f, 0.1f + tileHealth * 0.9f, 0.1f)));
	}
	
	public void addEntity(Entity e){
		entities.add(e);
	}
	public void removeEntity(Entity e){
		entities.remove(e);
	}
	
	public void addEffect(Effect e){
		effects.add(e);
	}
	public void removeEffect(Effect e){
		effects.remove(e);
	}
	
	public boolean isValidTile(int x, int y){
		if(x < 0 || y < 0 || x >= T_WIDE || y >= T_TALL) return false;
		return true;
	}
	
	public boolean isBlocked(float x, float y){
		return isBlocked((int)(x / Chunk.TILE_SIZE), (int)(y / Chunk.TILE_SIZE));
	}
	public boolean isBlocked(int x, int y)
	{ return isBlocked(x, y, false); }
	public boolean isBlocked(int x, int y, boolean checkEnts){
		if(x < 0 || y < 0 || x >= T_WIDE || y >= T_TALL) return true;
		// check blocks
		Chunk c = getChunkAt(x, y); 
		if(c.isBlocked(x - c.tOffsetX, y - c.tOffsetY)) return true;
		//if(!Tile.tiles[c.getTile(x - c.tOffsetX, y - c.tOffsetY)].canPass()) return true;
		// check entities
		if(checkEnts){
			ArrayList<Entity> ents = getEntitiesWithin(x * Chunk.TILE_SIZE, y * Chunk.TILE_SIZE, Chunk.TILE_SIZE, Chunk.TILE_SIZE);
			// TODO: ent specific collision?
			if(ents.size() > 0){
				return true;
			}
		}
		return false;
	}
	
	public ArrayList getEntitiesWithin(float x, float y, float w, float h){
		ArrayList<Entity> list = new ArrayList<Entity>();
		for(Entity ent : entities){
			if(ent.x >= x && ent.x <= x + w && ent.y >= y && ent.y <= y + h){
				list.add(ent);
			}
		}
		return list;
	}
	
	public boolean hasSelection(){
		return selectedEntities.size() > 0;
	}
	public void moveSelectionTo(int tileX, int tileY){
		for(Living e : selectedEntities){
			e.walkTo(tileX, tileY);
			e.setIdleWaypoint(tileX * Chunk.TILE_SIZE + 4f, tileY * Chunk.TILE_SIZE + 4f);
		}
	}
	
	public void setDebugView(boolean flag){
		float mapF;
		if(debugView = flag){
			Chunk.TILE_SIZE = 1;
			Chunk.CHUNK_SIZE = 256;
			mapF = 0.25f;
		} else {
			Chunk.TILE_SIZE = 8;
			Chunk.CHUNK_SIZE = 32;
			mapF = 4f;
		}
		targMapX *= mapF;
		targMapY *= mapF;
	}
	
	public void mouseClicked(int mb, int x, int y, int arg3) {
		if(mb == 0){
			Point p = stageToTile(x, y);
			//System.out.println("clicked tile "+p.x+","+p.y);
			for(int i = 0; i < 1; i++){
				//addEntity((new Lumberjack(this, tx * 8.5f, ty * 8.5f)));
			}
			//testEnt.walkTo(p.x, p.y);
		} else if(mb == 1){
			if(hasSelection()){
				Point p = stageToTile(x, y);
				moveSelectionTo(p.x, p.y);
			}
		}
	}
	
	public void mouseDragged(int x, int y, int deltaX, int deltaY){
		if(IngameState.isRMB()){
			targMapX += deltaX / curZoom * 1f;
			targMapY += deltaY / curZoom * 1f;
		} else if(selectionRect != null){
			selectionRect.setWidth(x - selectionRect.getX());
			selectionRect.setHeight(y - selectionRect.getY());
		}
	}
	public void mouseWheelMoved(int dir) {
		if(dir < 0){
			targZoom *= 0.75f;
		} else {
			targZoom *= 1.25f;
		}
	}
	
	public void mousePressed(int mb, int x, int y) {
		if(mb == 0){
			selectionRect = new Rectangle(x, y, 0, 0);
			selectedEntities.clear();
		}
	}
	public void mouseReleased(int mb, int x, int y) {
		if(mb == 0){
			selectedEntities = getEntitiesWithin(toLocalX(selectionRect.getX()), toLocalY(selectionRect.getY()), selectionRect.getWidth() / curZoom, selectionRect.getHeight() / curZoom);
			selectionRect = null;// TODO: new Rectangle(x, y, 0, 0);
		}
	}
	
	public Point stageToTile(int stageX, int stageY){
		return new Point((int) ((stageX - (int)(mapOffsetX * curZoom + Game.WIDTH * 0.5f)) / (Chunk.TILE_SIZE * curZoom)), 
				(int) ((stageY - (int)(mapOffsetY * curZoom + Game.HEIGHT * 0.5f)) / (Chunk.TILE_SIZE * curZoom)));
	}
	public int toTileX(int stageX) {
		return (int) ((stageX - (int)(mapOffsetX * curZoom + Game.WIDTH * 0.5f)) / (Chunk.TILE_SIZE * curZoom));
	}
	public int toTileY(int stageY) {
		return (int) ((stageY - (int)(mapOffsetY * curZoom + Game.HEIGHT * 0.5f)) / (Chunk.TILE_SIZE * curZoom));
	}
	// TODO: Something still messed up with this
	public float toLocalX(float stageX){
		return (stageX - (mapOffsetX * curZoom + Game.WIDTH * 0.5f)) / curZoom;
	}
	public float toLocalY(float stageY){
		return (stageY - (mapOffsetY * curZoom + Game.HEIGHT * 0.5f)) / curZoom;
	}
	
	public Collision raytrace(int sx, int sy, int ex, int ey, boolean checkEnd)
	{
	    int dx = Math.abs(ex - sx);
	    int dy = Math.abs(ey - sy);
	    int x = sx;
	    int y = sy;
	    int n = 1 + dx + dy;
	    int x_inc = (ex > sx) ? 1 : -1;
	    int y_inc = (ey > sy) ? 1 : -1;
	    int error = dx - dy;
	    dx *= 2;
	    dy *= 2;

	    int id;
	    Tile t;
	    for (; n > 0; --n)
	    {
	        //
	    	if(checkEnd || (x != ex && y != ey)){
		        id = getTile(x, y);
		        if(id > 0){
		        	t = Tile.tiles[id];
		        	if(!t.canPass()){
		        		return new Collision(t, x, y);
		        	}
		        }
	    	}
	        //
	        if (error > 0)
	        {
	            x += x_inc;
	            error -= dy;
	        }
	        else
	        {
	            y += y_inc;
	            error += dx;
	        }
	    }
	    return null;
	}
	
	public void reloadMap() throws SlickException {
		clearMap();
		doImageLoad = false;
		loadMap();
	}
	
	public void update(GameContainer container, StateBasedGame arg1, int arg2) throws SlickException {
		if(testEnt == null){
			//addEntity((testEnt = new Lumberjack(this, 10, 10)));
			//testEnt.setTargetPos(50, 10);
		}
		
		long curTime = System.currentTimeMillis();
		long tickTime = curTime - lastTick;
		lastTick = curTime;
		
		Input i = container.getInput();
		if(i.isKeyPressed(Input.KEY_F2)){
			setDebugView(!debugView);
			loadMap();
		}
		else if(i.isKeyPressed(Input.KEY_F3)){
			/*long luStart = System.currentTimeMillis();
			long luStop;
			for(int i = 0; i < chunks.length; i++){
				chunks[i].genChunk(false);
			}
			luStop = System.currentTimeMillis();
			System.out.println("GEN TIME: "+(luStop - luStart)+" ms");*/
		}
		else if(i.isKeyPressed(Input.KEY_F4)){
			doImageLoad = true;
			loadMap();
		}
		if(i.isKeyPressed(Input.KEY_MINUS)){
			targZoom *= 0.75f;
		} else if(container.getInput().isKeyPressed(Input.KEY_EQUALS)){
			targZoom *= 1.25f;
		}
		float moveStep = Chunk.CHUNK_SIZE * Chunk.TILE_SIZE * 0.1f;
		if (i.isKeyDown(Input.KEY_UP)) {
			targMapY += moveStep;
		} else if (i.isKeyDown(Input.KEY_DOWN)) {
			targMapY -= moveStep;
		} else if (i.isKeyDown(Input.KEY_LEFT)) {
			targMapX += moveStep;
		} else if (i.isKeyDown(Input.KEY_RIGHT)) {
			targMapX -= moveStep;
		}
		
		float testSpeed = 1.0f;
		if (i.isKeyDown(Input.KEY_W)) {
			testEnt.velY = -testSpeed;
			testEnt.setFacing(0);
		} else if (i.isKeyDown(Input.KEY_S)) {
			testEnt.velY = testSpeed;
			testEnt.setFacing(2);
		} else if (i.isKeyDown(Input.KEY_A)) {
			testEnt.velX = -testSpeed;
			testEnt.setFacing(3);
		} else if (i.isKeyDown(Input.KEY_D)) {
			testEnt.velX = testSpeed;
			testEnt.setFacing(1);
		} else {
			//testEnt.setFacing(-1);
		}
		if (i.isKeyDown(Input.KEY_SPACE)) {
			testEnt.tryAction();
		}
		
		if(isLoading){
			partialLoad();
		}
		
		partialTick += tickTime * 0.06f;
		while(partialTick > 1){
			partialTick -= 1.0f;
			
			for(Chunk c : chunks){
				c.update();
			}
			
			Iterator<Entity> itr = entities.iterator();
			Entity ent;
			while(itr.hasNext()){
				ent = itr.next();
				ent.update();
				if(ent.isDead){
					itr.remove();
				}
			}
			
			Iterator<Effect> effItr = effects.iterator();
			Effect eff;
			while(effItr.hasNext()){
				eff = effItr.next();
				eff.update();
				if(eff.isDead){
					effItr.remove();
				}
			}
			
			mapOffsetX += (targMapX - mapOffsetX) * 0.3f;
			mapOffsetY += (targMapY - mapOffsetY) * 0.3f;
			curZoom += (targZoom - curZoom) * 0.4f;
		}
	}
	
	public void setupLevelTransform(Graphics g) {
		g.translate(Game.WIDTH * 0.5f, Game.HEIGHT * 0.5f);
		g.scale(curZoom, curZoom);
		g.translate(mapOffsetX, mapOffsetY);
	}
	
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g, boolean showBlocked) throws SlickException {
		g.setClip(0, 0, Game.WIDTH, Game.HEIGHT);
		// draw map
		g.drawRect(0, 0, Game.WIDTH * 0.5f, Game.HEIGHT * 0.5f); // Guideline
		g.pushTransform();
		setupLevelTransform(g);
		float cPixelSize = Chunk.CHUNK_SIZE * Chunk.TILE_SIZE;
		int viewRad = (int)(Game.WIDTH * 0.5f * (1 / curZoom));//(int) (cPixelSize * 2);
		int left = (int) Math.max((-mapOffsetX - viewRad) / cPixelSize, 0);
		int top = (int) Math.max((-mapOffsetY - viewRad) / cPixelSize, 0);
		int right = (int) Math.min((-mapOffsetX + viewRad) / cPixelSize, cWide - 1);
		int bottom = (int) Math.min((-mapOffsetY + viewRad) / cPixelSize, cTall - 1);
		int x, y;
		for(y = top; y <= bottom; y++){
			for(x = left; x <= right; x++){
				chunks[y * cWide + x].render(arg0, arg1, g, showBlocked);
			}
		}
		// draw ents
		int len = entities.size();
		for(int i = 0; i < len; i++){
			entities.get(i).render(g);
		}
		len = effects.size();
		for(int i = 0; i < len; i++){
			effects.get(i).render(g);
		}
		if(selectedEntities.size() > 0){
			g.setColor(Color.green);
			len = selectedEntities.size();
			for(int i = 0; i < len; i++){
				selectedEntities.get(i).renderSelectionInfo(g);
			}
		}
		g.popTransform();
		// draw selection rect
		if(selectionRect != null){
			g.setColor(Color.green);
			g.setLineWidth(2.5f);
			g.draw(selectionRect);
			g.setLineWidth(1f);
		}
		g.setColor(Color.white);
		// draw texts
		g.drawString((int) (mapOffsetX)+","+(int) (mapOffsetY), 8, 28);
		g.drawString(left+"->"+right+"  "+top+"->"+bottom, 150, 42);
		g.drawString("Zoom "+(Math.floor(curZoom * 100))/100, 8, 42);
		g.drawString("Selected "+selectedEntities.size(), 8, 56);
		g.drawString("Load "+partialLoadI+" / "+chunks.length, 8, 72);
		g.drawString("E "+entities.size()+" / EF "+effects.size(), 8, 86);

		if(testEnt != null){
			g.drawString("Player "+(int)(testEnt.x/Chunk.TILE_SIZE)+", "+(int)(testEnt.y / Chunk.TILE_SIZE), 8, 120);
			Point facing = testEnt.getFacingTile();
			g.drawString("Facing ["+testEnt.getFacing()+"] "+facing.x+", "+facing.y, 8, 134);
			g.drawString("onTarg "+testEnt.onTargCalc(), 8, 148);
		}
		
		if(doImageLoad){
			g.pushTransform();
			g.translate(100, 250);
			g.scale(4, 4);
			g.setColor(Color.magenta);
			g.drawString("Please wait,", 0, 0);
			g.drawString("loading full map", 8, 18);
			g.setColor(Color.white);
			g.popTransform();
		}
	}
}

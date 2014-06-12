package com.yofreke.alfred;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.yofreke.alfred.states.IngameState;

public class Game extends StateBasedGame {
	
	public static int WIDTH = 800;
	public static int HEIGHT = 650;
	public static float WIDTHF = 25F;
	public static float HEIGHTF = 15.6F;
	public static Game instance;
	
	public static int PREGAME_STATE = 1;
	public static int MENU_STATE = 2;
	public static int INGAME_STATE = 3;
	
	public Game(){
		super("Adventures of Alfred");
		instance = this;
		//Log.setLogSystem(new SilentLogSystem());
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		Art.init();
		
		//addState(new PregameState());
		//addState(new MenuState());
		addState(new IngameState());
	}
	
	//// //// //// ////
	public static void main(String[] args) {
		// ME.debugEnabled = true;

		try
		{
			// Log.setVerbose(false);
			AppGameContainer container = new AppGameContainer(new Game());
			container.setDisplayMode(WIDTH, HEIGHT, false);
			container.setShowFPS(true);
			container.setTargetFrameRate(60);
			container.setAlwaysRender(true);
			//container.setVSync(true);
			// switch off mouse cursor
			container.start();
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}

}

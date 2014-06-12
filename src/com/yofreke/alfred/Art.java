package com.yofreke.alfred;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Art {

	public static SpriteSheet lumberjackSheet;
	
	public static void init() throws SlickException {
		lumberjackSheet = new SpriteSheet(new Image("res/lumberjack.png", false, Image.FILTER_NEAREST), 16, 16);
	}
}

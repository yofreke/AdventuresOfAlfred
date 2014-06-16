package com.yofreke.alfred.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class FontRenderer {

	public static char COLOR_IDENT = '\u8353';
	public static int FONT_WIDTH = 6;
	public static int FONT_HEIGHT = 8;
	
	public static Image texSheet;
	public static void init() throws SlickException{
		texSheet = new Image("res/gui.png", false, Image.FILTER_NEAREST);
	}
	
	public static char[] chars = new char[]{
		'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',' ',' ',' ',' ',' ',' ',
		'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',':','$','[',']','/',' ',
		'1','2','3','4','5','6','7','8','9','0','.'  
	};
	
	private static int charTexX, charTexY;
	private static boolean findingColor;
	private static Color drawColor = Color.white;
	
	private static boolean setCharCoord(char c){
		if(c == COLOR_IDENT){
			findingColor = true;
			return false;
		} else if(findingColor){
			drawColor = getColor(String.valueOf(c));
			findingColor = false;
			return false;
		}
		int i;
		for(i = 0; i < chars.length; i++){
			if(c == chars[i]){
				break;
			}
		}
		charTexX = (int) (i % 32) * 8;
		charTexY = (int) (i / 32) * 8 + 232;
		return true;
	}
	
	public static Color getColor(String s){
		try {
			switch(Integer.parseInt(s, 16)){
			case 1:
				return Color.red;
			case 2:
				return Color.green;
			case 3:
				return Color.blue;
			case 4:
				return Color.black;
			case 5:
				return Color.magenta;
			default:
				return Color.white;
			}
		} catch (NumberFormatException e){
			return Color.white;
		}
	}
	
	public static void renderString(Graphics g, String s, int x, int y){
		drawColor = Color.white;
		for(int i = 0; i < s.length(); i++){
			if(setCharCoord(s.charAt(i))){
			//g.drawImage(texSheet.getSubImage(charTexX, charTexY, 4, 8), x, y);
				g.drawImage(texSheet, x, y, x + FONT_WIDTH, y + FONT_HEIGHT, charTexX, charTexY, charTexX + FONT_WIDTH, charTexY + FONT_HEIGHT, drawColor);
				x += FONT_WIDTH;
			}
		}
	}
}

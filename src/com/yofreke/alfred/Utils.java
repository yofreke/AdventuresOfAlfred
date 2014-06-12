package com.yofreke.alfred;

import org.newdawn.slick.geom.Rectangle;

public class Utils {
	
	
	public static Rectangle duplicateRect(Rectangle rect){
		return new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}
}

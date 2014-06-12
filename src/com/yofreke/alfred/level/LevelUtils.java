package com.yofreke.alfred.level;



public class LevelUtils {

	public static void fillCircle(Level level, int x, int y, int rad, int id){
		for (int x_ = -rad; x_ < rad; x_++)
        {
			int h = (int)(rad * Math.sin(Math.acos((double)x_ / (double)rad)));
            for (int y_ = -h; y_ < h; y_++){
            	level.setTile(x_ + x, y_ + y, id);
            }
        }
	}
}

package com.yofreke.alfred.level;

import java.util.Random;


public abstract class Generator {
	public abstract void genAt(Level level, Random rand, int x, int y);
}

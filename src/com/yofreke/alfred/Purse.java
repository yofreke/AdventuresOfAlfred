package com.yofreke.alfred;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Purse {

	private Map<String, Integer> map = new HashMap<String, Integer>();
	private Map<String, Image> iconMap = new HashMap<String, Image>();
	
	public Purse() {
		this.addResource("wood");
		this.addResource("rock");
		this.addResource("food");
		
		this.transact("food", 100);
	}
	
	public void initImages(Image i) {
		iconMap.put("wood", i.getSubImage(4, 30, 18, 13));
		iconMap.put("rock", i.getSubImage(52, 29, 15, 15));
		iconMap.put("food", i.getSubImage(100, 29, 16, 15));
	}
	
	public void drawIcon(Graphics g, String name, float x, float y) {
		g.drawImage(iconMap.get(name), x, y);
	}
	
	private void addResource(String name) {
		map.put(name, 0);
	}
	
	public void transact(String name, int amount) {
		if(!map.containsKey(name)) {
			System.err.println("Unknown purse value - " + name);
			return;
		}
		map.put(name, map.get(name) + amount);
	}
	
	public int balance(String name) {
		if(!map.containsKey(name)) {
			System.err.println("Unknown purse value - " + name);
			return 0;
		}
		return map.get(name);
	}
}

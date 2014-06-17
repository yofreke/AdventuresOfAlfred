package com.yofreke.alfred;

import java.util.HashMap;
import java.util.Map;

public class Purse {

	private Map<String, Integer> map = new HashMap<String, Integer>();
	
	public Purse() {
		this.addResource("wood");
		this.addResource("rock");
		this.addResource("food");
		
		this.transact("food", 100);
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

package com.yofreke.alfred.item;

public class Item {

	public static Item[] items = new Item[64];
	
	public int id;
	public String name;
	
	public Item(int id){
		this.id = id;
		this.name = "Item " + id;
		
		items[id] = this;
	}
	
	public Item setName(String s) {
		this.name = s;
		return this;
	}
	
	public static Item wood = new Item(1).setName("wood");
	public static Item rock = new Item(2).setName("rock");
	public static Item food = new Item(3).setName("food");
}

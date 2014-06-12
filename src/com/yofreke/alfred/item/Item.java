package com.yofreke.alfred.item;

public class Item {

	public static Item[] items = new Item[64];
	
	public int id;
	public Item(int id){
		this.id = id;
		
		items[id] = this;
	}
	
	public static Item wood = new Item(1);
	public static Item rock = new Item(2);
	public static Item food = new Item(3);
}

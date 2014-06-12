package com.yofreke.alfred.item;

public class ItemStack {
	
	private int itemId;
	private int quantity;
	
	public ItemStack(int itemId){
		this(itemId, 1);
	}
	public ItemStack(int itemId, int quantity){
		this.itemId = itemId;
		this.quantity = quantity;
	}
	
	public int getId(){
		return itemId;
	}
	
	public int getQuality(){
		return quantity;
	}
}

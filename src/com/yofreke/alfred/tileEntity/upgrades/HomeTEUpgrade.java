package com.yofreke.alfred.tileEntity.upgrades;

import com.yofreke.alfred.states.IngameState;

public class HomeTEUpgrade {
	
	public String name;
	private Object[] costs;
	
	/** Give costs as array of string, int, string, int */
	public HomeTEUpgrade(String name, Object[] costs) {
		this.name = name;
		this.costs = costs;	
	}
	
	public void deductCosts() {
		for(int i = 0; i < costs.length; i += 2) {
			IngameState.purse.transact((String) costs[i], -(Integer) costs[i + 1]);
		}
	}
	
	public boolean canPurchase() {
		for(int i = 0; i < costs.length; i += 2) {
			if(IngameState.purse.balance((String) costs[i]) < (Integer) costs[i + 1]) return false;
		}
		return true;
	}
}

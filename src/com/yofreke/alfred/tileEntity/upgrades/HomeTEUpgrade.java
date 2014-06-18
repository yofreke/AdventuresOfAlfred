package com.yofreke.alfred.tileEntity.upgrades;

import java.util.HashMap;
import java.util.Map;

import com.yofreke.alfred.states.IngameState;

public class HomeTEUpgrade {
	
	public String name;
	public Map<String, Integer> costs;
	
	/** Give costs as array of string, int, string, int */
	public HomeTEUpgrade(String name, Object[] costs) {
		this.name = name;
		
		this.costs = new HashMap<String, Integer>();
		for(int i = 0; i < costs.length; i += 2) {
			this.costs.put((String) costs[i], (Integer) costs[i + 1]);
		}
	}
	
	public void deductCosts() {
		for (Map.Entry<String, Integer> entry : costs.entrySet()) {
			IngameState.purse.transact(entry.getKey(), -entry.getValue());
		}
	}
	
	public boolean canPurchase() {
		for (Map.Entry<String, Integer> entry : costs.entrySet()) {
			if(IngameState.purse.balance(entry.getKey()) < entry.getValue()) return false;
		}
		return true;
	}
}

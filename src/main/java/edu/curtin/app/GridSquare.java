package edu.curtin.app;

import java.util.*;

public class GridSquare {

	private final String terrain;
	private final List<ZoningRule> rules = new ArrayList<>();

	public GridSquare(String line) {
		String[] parts = line.split(",");
		terrain = parts[0].trim().toUpperCase();

		for (int i = 1; i < parts.length; i++) { //parse and add zoning rules from input
			ZoningRule rule = ZoningRule.create(parts[i]);
				if (rule != null) {
				rules.add(rule);
				}
		}
	}

	public boolean canBuild(Structure s) {
		if (terrain.equals(Constants.SWAMPY) && 
			(s.getFoundation().equals(Constants.SLAB) || s.getMaterial().equals(Constants.WOOD))) {
			return false;
		}

		for (ZoningRule r : rules) {
			if (!r.allows(s)) {
			return false;
	    		}
		}
		return true;
	}

	public double calculateCost(Structure s) {
		double cost = s.baseCost();
			if (terrain.equals(Constants.SWAMPY)) {
				cost += 20000 * s.getFloors(); //for swamp +20000 per floor
			}
			if (terrain.equals(Constants.ROCKY)) {
				cost += 50000;  //for rocky +50000
			}
			for (ZoningRule r : rules) {
				cost = r.applyExtraCost(cost, s);
			}
			return cost;
			}

		public String getTerrain() {
			return terrain;
	}

	
	public String getRulesSummary() { //display the floors
		StringBuilder summary = new StringBuilder();

		for (ZoningRule rule : rules) {
			String displayText = rule.getDisplayText();
			if (!displayText.isEmpty()) {
				summary.append(displayText).append(" ");
			}
		}

		return summary.toString().trim();
	}
}


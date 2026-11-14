package edu.curtin.app;

public interface ZoningRule {
	boolean allows(Structure s);
	double applyExtraCost(double cost, Structure s);


	String getDisplayText();

	static ZoningRule create(String s) {
		if (s.startsWith("heritage=")) {
			return new HeritageRule(s.split("=")[1]);
		} else if (s.startsWith("height-limit=")) {
			return new HeightLimitRule(Integer.parseInt(s.split("=")[1]));
		} else if (s.startsWith("flood-risk=")) {
			return new FloodRiskRule(Double.parseDouble(s.split("=")[1]));
		} else if (s.equals("contamination")) {
			return new ContaminationRule();
		}
		return null;
	}
}

class HeritageRule implements ZoningRule {
	private final String required;

	public HeritageRule(String required) {
		this.required = required.toUpperCase();
	}

	@Override
	public boolean allows(Structure s) {
		return s.getMaterial().equals(required);
	}

	@Override
	public double applyExtraCost(double cost, Structure s) {
		return cost; 
	}

	@Override
	public String getDisplayText() {
		return "H" + required.charAt(0);
	}
}

class HeightLimitRule implements ZoningRule {
	private final int limit;

	public HeightLimitRule(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean allows(Structure s) {
		return s.getFloors() <= limit;
	}

	@Override
	public double applyExtraCost(double cost, Structure s) {
		return cost;
	}

	@Override
	public String getDisplayText() {
		return "L" + limit;
	}
}

class FloodRiskRule implements ZoningRule {
	private final double risk;

	public FloodRiskRule(double risk) {
		this.risk = risk;
	}

	@Override
	public boolean allows(Structure s) {
		return s.getFloors() >= 2; //must have at least 2 floors
	}

	@Override
	public double applyExtraCost(double cost, Structure s) {
		return cost * (1 + (risk / 50)); //cost factor based on flood risk
	}

	@Override
	public String getDisplayText() {
		return "F" + Math.round(risk);
	}
}

class ContaminationRule implements ZoningRule {
	@Override
	public boolean allows(Structure s) {
		return true; 
	}

	@Override
	public double applyExtraCost(double cost, Structure s) {
		return cost * 1.5; //increase cost by 1.5 - contamination
	}

	@Override
	public String getDisplayText() {
		return "C";
	}
}

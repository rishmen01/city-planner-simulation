	package edu.curtin.app;

import java.util.Scanner;

public class Structure {
	private final int floors;
	private final String foundation;
	private final String material;

	public Structure(int floors, String foundation, String material) {
		this.floors = floors;
		this.foundation = foundation;
		this.material = material;
	}

	public int getFloors() {
		return floors;
	}

	public String getFoundation() {
		return foundation;
	}

	public String getMaterial() {
		return material;
	}

	public static Structure getInput(Scanner sc) {
		int f;
		String fo;
		String m;

		//get and validate floors
		while (true) {
			try {
				System.out.print("Floors: ");
				String floorsInput = sc.nextLine();
				f = Integer.parseInt(floorsInput);
				if (f <= 0) {
					System.out.println("Floors must be a positive number");
					continue;
				}
				break;
			} catch (NumberFormatException e) {
				System.out.println("enter a valid number");
			}
		}

	//get and validate foundation
	while (true) {
		System.out.print("Foundation (SLAB/STILTS): ");
		fo = sc.nextLine().toUpperCase();
		if (fo.equals(Constants.SLAB) || fo.equals(Constants.STILTS)) {
			break;
		} else {
			System.out.println("Foundation must be either SLAB or STILTS");
		}
	}

	//get and validate material
	while (true) {
		System.out.print("Material (WOOD/STONE/BRICK/CONCRETE): ");
		m = sc.nextLine().toUpperCase();
		if (m.equals(Constants.WOOD) || m.equals(Constants.STONE) || 
			m.equals(Constants.BRICK) || m.equals(Constants.CONCRETE)) {
			break;
		} else {
			System.out.println("Material must be WOOD, STONE, BRICK or CONCRETE");
		}
	}

	return new Structure(f, fo, m);
	}

	public double baseCost() {
		return switch (material) {
			case Constants.WOOD -> 10000;
			case Constants.STONE -> 50000;
			case Constants.BRICK -> 30000;
			case Constants.CONCRETE -> 20000;
			default -> 0;
		} * floors;
	}
}


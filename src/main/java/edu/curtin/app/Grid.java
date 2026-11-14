package edu.curtin.app;

import java.io.*;
import java.util.*;

public class Grid {

	private final int height;
	private final int width;
	private final GridSquare[][] grid;

	public Grid(String filename) throws IOException {
		File file = new File(filename);

		if (!file.exists()) {
			throw new IOException("File " + filename + " was not found");
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String[] size = reader.readLine().split(",");
			if (size.length != 2) {
				throw new IOException("Invalid grid size");
			}
			//parse grid dimention
			height = Integer.parseInt(size[0].trim());
			width = Integer.parseInt(size[1].trim());
			grid = new GridSquare[height][width];
			
			//read data for squares
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					String line = reader.readLine();
					if (line == null) {
						throw new IOException("Error");
					}
					grid[i][j] = new GridSquare(line);
				}
			}
		}
	}

	public void displayGrid() { //displying the graph
		StringBuilder gridDisplay = new StringBuilder();

		for (int j = 0; j < width; j++) {
			gridDisplay.append("+-----------------");
		}
		gridDisplay.append("+\n");

		for (int i = 0; i < height; i++) {
			StringBuilder terrainLine = new StringBuilder("| ");
			StringBuilder rulesLine = new StringBuilder("| ");

		for (int j = 0; j < width; j++) {
			GridSquare square = grid[i][j];
			String terrain = square.getTerrain().toLowerCase();
			String rules = square.getRulesSummary();

			terrainLine.append(String.format("%-15s | ", terrain)); //terrain with a fixed length

			rulesLine.append(String.format("%-15s | ", rules)); //rules with a fixed length
		}

		gridDisplay.append(terrainLine).append("\n");  //add to display
		gridDisplay.append(rulesLine).append("\n");

		for (int j = 0; j < width; j++) {
			gridDisplay.append("+-----------------");
		}
		gridDisplay.append("+\n");
		}

		System.out.println(gridDisplay.toString());
	}

	public void buildStructure(Scanner sc) {
		while (true) {
			try {
				//row 
				System.out.print("Row: ");
				String rowInput = sc.nextLine();
				int r = Integer.parseInt(rowInput);

				if (r < 0 || r >= height) {
					System.out.println("Row must be between 0 and " + (height - 1));
					continue;
				}

				//colomn
				System.out.print("Col: ");
				String colInput = sc.nextLine();
				int c = Integer.parseInt(colInput);

				if (c < 0 || c >= width) {
					System.out.println("Error: Column must be between 0 and " + (width - 1));
					continue;
				}

				//get structure details
				Structure s = Structure.getInput(sc);

				if (grid[r][c].canBuild(s)) {
					System.out.println("Cost: $" + grid[r][c].calculateCost(s));
				} else {
					System.out.println("Cannot build here");
				}

				//exit the loop
				break;
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number");
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Grid coordinates are wrong");
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			} catch (InputMismatchException e) {
				System.out.println("Invalid input");
			}
		}
	}

	public BuildStrategy selectStrategy(Scanner sc) { //selecting the stratagy
		System.out.println("Choose: \n1. Uniform \n2. Random \n3. Central");
		String opt = sc.nextLine();

		if (opt.equals("1")) {
			return new UniformBuild(Structure.getInput(sc));
		} else if (opt.equals("2")) {
			return new RandomBuild();
		}

		return new CentralBuild();
	}

	public interface BuildStrategy {
		void buildCity();
	}

	public abstract class AbstractBuild implements BuildStrategy {
		@Override
		public final void buildCity() {
			int count = 0;
			double cost = 0;
			StringBuilder visual = new StringBuilder(); //repreenation of city

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					Structure s = decideStructure(i, j);
					if (grid[i][j].canBuild(s)) {
						count++;
						cost += grid[i][j].calculateCost(s);
						visual.append("X"); //mark built location
					} else {
						visual.append(" ");
					}
				}
				visual.append("\n");
			}
			System.out.println("Built: " + count + " Structures");
			System.out.println("Total Cost: $" + cost);
			System.out.println("City Grid:\n" + visual);
		}
		protected abstract Structure decideStructure(int i, int j);
	}

	public class UniformBuild extends AbstractBuild { //build the same structure everywhere
		private final Structure s;

		public UniformBuild(Structure s) {
			this.s = s;
		}

		@Override
		protected Structure decideStructure(int i, int j) {
			return s;
		}
	}

	public class RandomBuild extends AbstractBuild { //randomly builds
		private final Random r = new Random();

		@Override
		protected Structure decideStructure(int i, int j) {
			String mat = r.nextBoolean() ? Constants.WOOD : Constants.BRICK;
			String fnd = r.nextBoolean() ? Constants.SLAB : Constants.STILTS;
			return new Structure(1 + r.nextInt(5), fnd, mat);
		}
	}

	public class CentralBuild extends AbstractBuild { //build based on distance 
		@Override
		protected Structure decideStructure(int i, int j) {
			//calculate distance 
			double distance = Math.sqrt(Math.pow(i - ((height - 1) / 2.0), 2) + Math.pow(j - ((width - 1) / 2.0), 2));

			String mat;
			if (distance <= 2) {
				mat = Constants.CONCRETE;
			} else if (distance <= 4) {
				mat = Constants.BRICK;
			} else if (distance <= 6) {
				mat = Constants.STONE;
			} else {
				mat = Constants.WOOD;
			}

			int floors = Math.round((float)(1 + 20 / (distance + 1))); //calculate floors 

			return new Structure(floors, Constants.SLAB, mat);
		}
	}
}


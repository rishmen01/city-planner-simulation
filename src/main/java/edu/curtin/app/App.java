package edu.curtin.app;

import java.util.Scanner;
import java.util.logging.*;
import java.io.IOException;

public class App {

	private static final Logger logger = Logger.getLogger(App.class.getName());

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: ./gradlew run --args=\"grid.txt\"");
			return;
		}

		String filename = args[0];
		Grid grid;

		try { //loading the gridfie
			grid = new Grid(filename);
		} catch (IOException e) {
			logger.log(Level.SEVERE, () -> "Error loading grid: " + e.getMessage());
			return;
		}

		try (Scanner sc = new Scanner(System.in)) {
			Grid.BuildStrategy strategy = grid.new RandomBuild(); //random is default
			boolean exit = false;

			while (!exit) {
				printMenu();
				String choice = sc.nextLine();

				switch (choice) {
					case "1" -> grid.buildStructure(sc);
					case "2" -> strategy.buildCity();
					case "3" -> strategy = grid.selectStrategy(sc);
					case "4" -> grid.displayGrid();
					case "5" -> exit = true;
					default -> System.out.println("Invalid Option");
				}
			}
		}
	}

	private static void printMenu() { //display menu
		System.out.println("\nMenu");
		System.out.println("1. Build Structure");
		System.out.println("2. Build City");
		System.out.println("3. Configure");
		System.out.println("4. Display Grid");
		System.out.println("5. Quit");
		System.out.print("Choice: ");
	}
}

package ie.atu.sw;

import java.util.Scanner;

public class Menu {

	/*
	 * A class to display a menu with options to the user The user is prompted to
	 * input an integer to choose an option. If the option is invalid, an error is
	 * shown, the menu refreshes and the user is prompted for a choice once again.
	 */

	private String inputDir = System.getProperty("user.dir"); // sets the current working directory as the default
																// directory
	private String outputFile = System.getProperty("user.dir") + "\\Output.csv"; // sets the current output file as a
																					// default in the working directory
	private int nGramSize = 1; // sets default nGram size to 1
	private Scanner sc = new Scanner(System.in);

	private Parser myParser;

	public Menu() {

		/*
		 * Constructor for the Menu class Creates a new Parser object for this menu
		 * instance and sets the default parameters
		 */

		myParser = new Parser();
		myParser.setOutputFile(outputFile);
		myParser.setInputDir(inputDir);
		myParser.setNGramSize(nGramSize);
	}

	public void show() {

		/*
		 * This show function will display the menu and prompt the user for a choice
		 * (int) The menu will also display the current settings for each choice These
		 * settings can be updated by the user through the menu options
		 */

		int choice = 0; // sets default choice to nothing
		clearScreen(); // clears the screen

		while (choice != 5) { // if 5 is selected, the method will terminate
			System.out.println("************************************************************");
			System.out.println("*      ATU - Dept. Computer Science & Applied Physics      *");
			System.out.println("*                                                          *");
			System.out.println("*                  N-Gram Frequency Builder                *");
			System.out.println("*                                                          *");
			System.out.println("************************************************************");
			System.out.println("(1) Specify Text File Directory - Currently: " + myParser.getInputDir());
			System.out.println("(2) Specify n-Gram Size - Currently: " + myParser.getNGramSize());
			System.out.println("(3) Specify Output File - Currently: " + myParser.getOutputFile());
			System.out.println("(4) Build n-Grams ");
			System.out.println("(5) Quit");
			System.out.print("-> ");

			choice = sc.nextInt(); // Prompts the user for an integer stating their choice

			switch (choice) { // calls the relevant method for the choice selected
			case 1 -> setDirectory();
			case 2 -> setNGramSize();
			case 3 -> setOutputFile();
			case 4 -> buildNGrams();
			case 5 -> System.out.println("Quitting.");
			default -> System.out.println("Invalid Selection!");
			}
		}
	}

	public void setDirectory() {
		// sets the directory containing the text files to be parsed
		System.out.print("Input File/Directory: ");
		myParser.setInputDir(sc.next().trim());
	}

	public void setNGramSize() {
		// sets the size of the nGram to use while parsing the text files

		System.out.print("n-Gram Size [1-5]: ");

		int i = sc.nextInt();

		switch (i) {
		case 1, 2, 3, 4, 5 -> myParser.setNGramSize(i);
		default -> System.out.print("Invalid selection!");
		}

	}

	public void setOutputFile() {
		// sets the preferred output file name for the user
		// The directory can also be specified if required.

		System.out.print("Output File: ");
		myParser.setOutputFile(sc.next().trim() + ".csv");
	}

	public void buildNGrams() {

		// function to call the parse method of the myParser object

		System.out.println("Building N-Grams...");

		try {
			myParser.parse();
		} catch (Exception e) {

			// If an error occurred, specify the error message to the user
			System.out.println("Error: " + e.getLocalizedMessage());
		}

	}

	public static void clearScreen() {

		// Clears the screen for the user
		System.out.println("\033[H\033[2J");
		System.out.flush();
	}
}

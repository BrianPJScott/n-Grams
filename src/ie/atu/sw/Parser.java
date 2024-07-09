package ie.atu.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Parser {

	private Object[][] table;
	private int nSize;
	private String inputDir;
	private String outputFile;

	public Parser() {

		// constructor for the Parser class
		// Sets variables to default values.

		setOutputFile(null);
		setInputDir(null);
		setNGramSize(1);

	}

	// The following methods are getters and setters for the variables controlled by
	// this class

	public void setInputDir(String dir) {
		inputDir = dir;
	}

	public String getInputDir() {

		return inputDir;
	}

	public void setNGramSize(int size) {
		nSize = size;
	}

	public int getNGramSize() {
		return nSize;
	}

	public void setOutputFile(String output) {
		outputFile = output;
	}

	public String getOutputFile() {
		return outputFile;
	}

	// This function adds an NGram to the table.
	// If the nGram already existed, it will increment it's count by 1
	// If not,the nGram will be added to the end of the table and its count set to 1
	private void addNGram(String nGram) {

		int i = 0;
		boolean found = false;

		while (i <= table.length && !found) { // Iterate through the table until either the nGram has been found, or,
												// for some reason, we've reached the end of the table
			if (table[i][0].toString().equals(nGram)) { // If this table entry matches the n-gram to be added..
				table[i][1] = (long) table[i][1] + 1; // Increment it's count by 1
				found = true; // The nGram was found, we can stop looking for it now
			} else if (table[i][0].toString().equals("")) { // Otherwise, if this table entry is empty (i.e. we've gone
															// through all entries that have already been added...
				table[i][0] = nGram; // Make a new entry matching this new nGram
				table[i][1] = 1l; // and set it's count to 1
				found = true; // we're done now and can stop looking
			} else {
				i++; // otherwise, we still haven't found a proper entry.. continue looking
			}
		}

	}

	// This function begins the parsing process
	public void parse() {

		table = new Object[(int) Math.pow(26, nSize)][2]; // The table size is set to the maximum possible entries that
															// can be found for this nGram Size - It's a waste of
															// memory, but limitations are set by the use of the Array
															// data structure

		long lineCount;
		long progress;
		String newProgress = "";
		String lastProgress = "";

		for (int i = 0; i < table.length; i++) { // initialize the entire table to Empty Strings and longs of value 0
			table[i][0] = new String("");
			table[i][1] = 0l;
		}

		File f = new File(inputDir); // Set file f to the input directory specified by the user in Menu

		File[] files = f.listFiles(); // Fill the array of files with a list of the files contained in the directory

		if (files != null) { // if files were found in the specified directory.. continue. Otherwise, specify
								// to the user that there were no files.

			for (File file : files) { // For each of these files...
				System.out.println();
				if (!file.isDirectory()) { // If it is a directory, skip it...
					System.out.println("Currently working file: " + file.toString()); // Echo the currently worked file
																						// to the user

					String line = null;
					lineCount = 0;
					progress = 0;

					try (BufferedReader br = new BufferedReader(
							new InputStreamReader(new FileInputStream(file.getAbsoluteFile())))) { // try to open the
																									// current file for
																									// reading and
																									// parsing

						while ((line = br.readLine()) != null) { // Count the amount of lines in the file for the sake
																	// of the progress meter
							lineCount++;
						}

						br.close(); // close the file reader
					} catch (Exception e) {

						// If an error occurred, specify the error message to the user
						System.out.println("Error: " + e.getLocalizedMessage());
					}

					try (BufferedReader br2 = new BufferedReader(
							new InputStreamReader(new FileInputStream(file.getAbsoluteFile())))) { // try to open the
																									// current file for
																									// reading and
																									// parsing

						while ((line = br2.readLine()) != null) { // While the file has not been completely read

							// increment progress and check if the progress has been updated since the last
							// prompt to the user. If not, don't print it.
							progress++;
							newProgress = getProgress(progress, lineCount);
							if (!newProgress.equals(lastProgress)) {
								System.out.print(newProgress);
								lastProgress = newProgress;
							}

							String[] words = line.split("\\s+"); // break the current line into an array of strings
																	// delimited by spaces (i.e. words)

							for (String word : words) { // for each word..

								int i = 0;

								word = word.replaceAll("[^A-Za-z]", "").toLowerCase(); // make all letters lower-case
																						// and
																						// remove any non-alphabetical
																						// characters

								while (word.length() >= i + nSize) { // move through the word one character at a time
																		// and read a string of the nGram size specified
																		// from it
									String newGram = word.substring(i, i + nSize);
									addNGram(newGram); // add the newly found nGram to the table
									i++; // move one letter ahead in the word
									// Note: if the word has fewer letters then the specified nGram size, it will be
									// skipped
								}
							}
						}
						br2.close(); // close the file reader
					} catch (Exception e) {

						// If an error occurred, specify the error message to the user
						System.out.println("Error: " + e.getLocalizedMessage());
					}
				} else {
					System.out.println(file.toString() + " - Skipping directory"); // if this was a directory, skip it
																					// and prompt that to the user
				}
			}
			System.out.println();
			SortTable(); // call the function to sort the table
			WriteNGrams(); // write the table to the specified file
		} else {
			System.out.println("No files found in directory: " + inputDir);
		}

	}

	// This function will sort the table contained in this object by the number of
	// nGrams from highest to lowest
	private void SortTable() {

		int i = 0;
		Object[] entry = new Object[2];

		boolean sorted = false;

		System.out.println("Sorting Table...");

		while (!sorted) {
			sorted = true; // if we never have to swap an entry through this pass, the table is sorted and
							// we can stop processing
			i = 0;
			while ((i < table.length - 1) && !table[i][0].toString().equals("")) { // while we haven't gotten to the end
																					// of the table..
				if ((long) table[i + 1][1] > (long) table[i][1]) { // if the next entry is greater than the current
																	// entry..
					entry = table[i]; // swap the entries.
					table[i] = table[i + 1];
					table[i + 1] = entry;
					sorted = false; // if we had to swap the entries, the table was not yet sorted, so we should go
									// again
				}
				i++;
			}
		}

	}

	// Method to write the current table of nGrams to the file specified by the user
	private void WriteNGrams() {

		int i = 0;

		System.out.println("Writing to file: " + outputFile + "..."); // Prompt this step to the user

		try {

			File f = new File(outputFile);
			if (f.createNewFile()) {
				System.out.println("File created: " + f.getName()); // if the file needed to be created, prompt this to
																	// the user
			} else {
				System.out.println(f.getName() + " exists; overwriting"); // if the file already existed, overwrite and
																			// notify user
			}

			FileWriter fr = new FileWriter(outputFile);
			while (i < table.length && !table[i][0].toString().equals("")) {
				fr.write(table[i][0] + "," + table[i][1] + System.lineSeparator()); // while we're not at the end of the
																					// table, write his entry to the
																					// file, deliminated by ','
				i++;
			}
			fr.close();

		} catch (IOException e) {
			System.out.println("Error: " + e.getLocalizedMessage()); // if there was an error in the file write process,
																		// output the error to the user
		}

	}

	private String getProgress(long prog, long total) {	// Returns a string showing progress given the progress (prog) of total (total)

		StringBuilder sb = new StringBuilder();
		int blocks = 0;
		int percent = 0;
		double current = (double) prog;
		float tenpercent = 0;

		sb.append("Parse Progress: [");		// start building the return string

		tenpercent = (float) (total / 10d);	// this progress meter works in 10% blocks to reduce prompts to the console

		while (current >= tenpercent) {		// while current progress is above 10% / 20%.. etc., add '*' to the bar
			current -= tenpercent;
			sb.append("*");
			blocks++;
		}

		percent = blocks * 10;

		while (blocks < 10) {				// fill out the remaining empty space in the progress bar wth '-'
			sb.append("-");
			blocks++;
		}

		return ("\r" + sb + "] " + percent + "%"); // complete the string and return it

	}

	// This was a function that can be called to print the table to the screen for
	// the user
	// although it was used in the debugging process, it is not used in the deployed
	// release
	/*
	 * private void PrintNGrams() {
	 * 
	 * int i = 0; System.out.println("*****************************"); while ((i <
	 * table.length) && !(table[i][0].toString().equals(""))) { System.out.println(i
	 * + ": " + table[i][0] + " - " + table[i][1]); i++; }
	 * System.out.println("*****************************"); }
	 */
}

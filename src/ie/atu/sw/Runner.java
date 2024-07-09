package ie.atu.sw;

/*N-Gram Frequency Builder
H.Dip. in Science (Software Development)
Object-Oriented Software Development (2022)
Brian Scott - G00411391@gmit.ie

This is a menu driven application which parses through text files and maps the frequency of individual strings of characters.

The menu options behave as follows:
(1) Specify Text File Directory -> Allows the user to specify the directory which contains the files to be parsed.
	- Default directory is the current working directory.
	-- Any sub directories will be skipped
	-- Any un-accessible files will be skipped (e.g. locked by Operating System)

(2) Specify n-Gram Size -> allows the user to state the length of the N-Gram to use in the parsing process.
	- Default value is 1
	- Maximum value is 5 (Any additional N-Gram length slows the process dramatically due to the array data structure being used and its size (26 * (N-GramSize))

(3) Specify Output File -> Allows the user to specify the location and name of the output file. 
	- The absolute path and name of the file can be entered (e.g. c:\outdir\outputfile).
	- The extension of the .csv file is automatically added and does not need to be specified by the user

(4) Build n-Grams -> Begins the parsing process
	- Each file in the specified directory will be opened and parsed through in turn.
	- During the process:
		- Each file is read line by line
		- Any non-alphabetic characters are removed from the string of text
		- The string of text is converted to lowercase
		- the line is broken into words (as delimited by the space character)
		- Each word is panned through and N-Grams of the specified size are pulled from them (If the words length is shorter than the n-gram length, it is ignored)
		- The table of found n-grams is searched for the new n-gram
			-- if it exists, it's count is incremented
			-- if it does not exist, it is added to the table
	- This process continues until all files in the directory have been parsed.
	- Once complete, the table is sorted from the n-gram of highest frequency to the lowest
	- The result is then written to the output file specified.

5) Quit - Terminates the Application


Notes:
An array list could be used to dynamically control the size of the table at run time, which would speed up the process, lower memory requirements and allow for longer N-Gram parsing.

Although console colours were suggested in the spec. for the project, they did not work in the console in the IDE. The output from these commands in the IDE made the experience messy, so I removed them.

A more advanced sorting algorithm could me used to speed up the sorting process (bubble/shell sort) however for the scope of this project, a basic sorting method seemed effective.

References: https://www.drupal.org/docs/develop/managing-a-drupalorg-theme-module-or-distribution-project/documenting-your-project/readme-template

https://www.tutorialspoint.com/java/java_files_io.htm#:~:text=sequence%20of%20data.-,The%20InputStream%20is%20used%20to%20read%20data%20from%20a%20source,be%20discussed%20in%20this%20tutorial.

https://www.geeksforgeeks.org/switch-statement-in-java/#:~:text=The%20switch%20statement%20is%20a,the%20value%20of%20the%20expression.

https://stackoverflow.com/questions/2979383/how-to-clear-the-console

https://www.baeldung.com/java-string-newline#:~:text=In%20Windows%2C%20a%20new%20line,the%20end%20of%20our%20string.


*/

public class Runner {

	public static void main(String[] args) throws Exception {

		// Creates a new instance of the Menu class and implements it's show() function

		Menu myMenu = new Menu();
		myMenu.show();

	}
}

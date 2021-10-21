package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is associated with the audio functionality of festival.
 * It is responsible for creating and writing to a scheme file to use 
 * the Māori festival voice. It will also return the path of the scheme file.
 * @author Group 22
 *
 */
public class Speak {
	
	/**
	 * Function to create and write to a scheme file to call the Māori voice of festival
	 * @param word
	 * @param speed
	 * @param schemeFile
	 */
	public static void createSchemeFile(String word, double speed, File schemeFile) {
		//open a scheme file and write to it 
		BufferedWriter scheme = null;
		try {
			if (word.contains("-")) {
				word = word.replaceAll("-", " ");
			}
			scheme = new BufferedWriter(new FileWriter(schemeFile));
			scheme.write("(voice_akl_mi_pk06_cg)"); // set up Māori voice
			scheme.write("(Parameter.set 'Duration_Stretch " + speed + ")");
			scheme.write("(SayText \"" + word + "\")");
			scheme.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to get and return the path for the scheme file
	 * @return
	 */
	public static File getPath() {
		Path path = Paths.get(".speak.scm"); 
		File schemeFile = new File(path.toAbsolutePath().toString());
		return schemeFile;
	}
}

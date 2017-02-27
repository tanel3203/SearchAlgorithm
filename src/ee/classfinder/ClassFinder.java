package ee.classfinder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

public class ClassFinder {

	public static void main(String[] args) throws Exception {

		TreeMap<String, String> classes = new TreeMap<>(); 
		
		File file = new File(args[0]);
		BufferedReader reader = new BufferedReader(
								new InputStreamReader(
								new FileInputStream(file)
								));

		boolean queryMatchesClassInFile = false;
		String line = null;
		String[] lineElements;
		String[] queryElements = 
				new Cleaner().prepareNameAsArray(args[1], false);

		while ((line = reader.readLine()) != null) {
			lineElements = new Cleaner()
							.prepareNameAsArray(line, true);
			queryMatchesClassInFile = new Matcher()
							.findMatches(queryElements, lineElements);

			if (queryMatchesClassInFile) {
				
				classes.put(
						new Cleaner().prepareArrayAsClassString(lineElements, false), // class name, package name
						new Cleaner().prepareArrayAsClassString(lineElements, true)); // package name, class name
			}
		}
		reader.close();
		new Printer().printMatches(classes);
	}
}
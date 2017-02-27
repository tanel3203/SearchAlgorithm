package ee.classfinder;

import java.util.Map.Entry;
import java.util.TreeMap;

public class Printer {

	/**
	 * To show the user classes that match their query with package names
	 * this method prints them out in <package><class> format
	 * @param matches	TreeMap<String, String>-type object with matching classes
	 */
	public void printMatches(TreeMap<String, String> matches) {
		for (Entry<String, String> entry : matches.entrySet()) {
			System.out.println(entry.getValue());
		}
	}
}
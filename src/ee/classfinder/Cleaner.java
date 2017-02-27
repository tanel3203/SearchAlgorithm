package ee.classfinder;

import java.util.ArrayList;

public class Cleaner {
	
	private static final String PACKAGE_EXISTS_IDENTIFIER = "."; // to separate classes that are full package names
	
	/**
	 * To compare cleaned class names against cleaned user query,
	 * this method prepares the class name or query on request
	 * as an array of elements 
	 * e.g. 'CamelCase' -> 'Camel', 'Case' - two elements
	 * e.g. 'ee.test.CamelCaseZest' -> 'Camel','Case','Zest','ee.test.'
	 * 									moving package name to end
	 * 									simplifies matching query and classes
	 * 
	 * @param classNameIn	String-type line in file, it is a dirty class name
	 * @return
	 * @throws Exception
	 */
	public String[] prepareNameAsArray(String nameIn
										, boolean trimEntry) 
											throws Exception {
		ArrayList<String> currentLineClassElements = new ArrayList<String>();
		String currentLine = null;
		String packageName = null;
		String className = null;
		boolean containsPackageName;

		if (trimEntry) {
			currentLine = nameIn.trim();
		} else {
			currentLine = nameIn;
		}
		
		
		if (currentLine == null) {
			throw new Exception("No data on line");
		}

		if (currentLine.contains(PACKAGE_EXISTS_IDENTIFIER)) {
			packageName = currentLine
						.substring(0, currentLine
										.lastIndexOf(PACKAGE_EXISTS_IDENTIFIER)+1);
			className = currentLine
						.substring(1+currentLine
										.lastIndexOf(PACKAGE_EXISTS_IDENTIFIER));
			containsPackageName = true;
			
		} else {
			className = currentLine;
			containsPackageName = false;
		}
		
		if (className.equals(className.toLowerCase())) {
			className = className.toUpperCase();			
		} 
		for (int i = 0, m = 0; i < className.length(); i++) {

			if (Character.isUpperCase(className.charAt(i)) 
					&& i != 0) {
				currentLineClassElements
					.add(className.substring(m, i));
				m = i;

				if (i+1 == className.length() 
					&& Character.isUpperCase(className.charAt(i))) {
					currentLineClassElements
						.add(String.valueOf(className.charAt(i)));
				}
			} else if (i+1 == className.length()) {
				currentLineClassElements
						.add(className.substring(m, i+1));
			}
		}
		
		if (containsPackageName) {
			currentLineClassElements.add(packageName); // store package name at the end of class elements list
		} else {
			currentLineClassElements.add(null); // to standardize the element count for every class object
		}
		
		return currentLineClassElements
				.toArray(new String[currentLineClassElements.size()]);		
	}
	
	/**
	 * To have the query and class in a form that can be presented to the user
	 * this method takes either user query (it is a class) or class from file
	 * and returns it as full class (and package) name
	 * if necessary, reverses the order of class and package (sorting purposes)
	 * 
	 * @param classAndPackage			String[]-type class (and optionally package)
	 * @param reverseClassAndPackage	boolean-type if reversal is necessary
	 * @return
	 */
	public String prepareArrayAsClassString(String[] classAndPackage
											, boolean reverseClassAndPackage) {

		StringBuilder classElements = new StringBuilder();

		for (int i = 0, n = classAndPackage.length // ignore package (last element) if 1, don't if 0
				; i < n; i++) {
			if (reverseClassAndPackage) {
				if (i == 0) {
					if (classAndPackage[n-1] != null) {
						classElements.append(classAndPackage[n-1]);	
					}
					
					classElements.append(classAndPackage[i]);
				} else if (i != n-1) {
					classElements.append(classAndPackage[i]);
				} 
			} else {
				classElements.append(classAndPackage[i]);	
			}
		}

		return classElements.toString();
	}
}

package ee.testassignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClassFinder {

	//private static String[] queryItem = new String[]{"B*r","Baz"};
	
	private static final String PACKAGE_EXISTS_IDENTIFIER = "."; // to separate classes that are full package names
	private static final String WILDCARD_IDENTIFIER = "*"; // wildcard to replace one character
	private static final String NAME_ENDING_IDENTIFIER = " "; // only accepts spaces as name ending chars
		
	/**
	 * Program starting point. attempts to find classes 
	 * from file (arg1) based on approximate query (arg2)
	 * 
	 * @param args	takes two arguments, filepath and query
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		
		if (args.length != 2) {
			throw new Exception("Invalid argument count");
		}

		ClassFinder classFinder = new ClassFinder();
		File filePath = new File(args[0]); 
		String[] queryItem = classFinder.prepareNameAsArray(args[1]); 
		
		
		if (!filePath.exists()) {
			throw new Exception("Can't find file");
		}
		
		try {
			classFinder.findQueryfromClassesInFile(filePath, queryItem);
		} catch (Exception e) {
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		
		System.out.println("Program ran for " + (endTime - startTime) + " milliseconds");
	
	}
	
	/**
	 * To find the user query (approximate class name) from classes list in file
	 * this method performs a line by line file parsing an initiates match finding
	 * @param filePathIn 	File-type 
	 * @param queryIn		String[]-type approximate class name in camel case
	 * @return				
	 * @throws Exception
	 */
	public int findQueryfromClassesInFile(File filePathIn, String[] queryIn) throws Exception {
		ClassFinder classFinder = new ClassFinder();
		String[] queryElements = queryIn; // 'CamelCase' -> two elements, 'Camel', 'Case'
		ArrayList<String[]> classesList = new ArrayList<String[]>();

		Scanner input;
		try {
			input = new Scanner(filePathIn);
		} catch (FileNotFoundException e) {
			input = null;
			e.printStackTrace();
		}
		
		while (input.hasNextLine()) {
			String currentLine = input.nextLine();
			
			if (currentLine.trim().length() > 0) { // exception handling
				classesList.add(prepareNameAsArray(currentLine));
			}
		}
			
		return classFinder.findMatches(queryElements, classesList);
	}


	/**
	 * To compare cleaned class names against cleaned user query,
	 * this method prepares the class name and query on request
	 * as an array of elements 
	 * e.g. 'CamelCase' -> 'Camel', 'Case' - two elements
	 * e.g. 'ee.test.CamelCaseZest' -> 'Camel','Case','Zest','ee.test.'
	 * 
	 * @param classNameIn	String-type line in file, it is a dirty class name
	 * @return
	 * @throws Exception
	 */
	public String[] prepareNameAsArray(String classNameIn) throws Exception {
		String currentLine = classNameIn.trim();
		ArrayList<String> currentLineClassElements = new ArrayList<String>();
	
		
		String packageName = null;
		String className = null;
		boolean containsPackageName;
		
		
		if (currentLine == null) {
			throw new Exception("No data on line");
		}

		if (currentLine.contains(PACKAGE_EXISTS_IDENTIFIER)) {
			packageName = currentLine.substring(0, currentLine.lastIndexOf(PACKAGE_EXISTS_IDENTIFIER)+1);
			className = currentLine.substring(1+currentLine.lastIndexOf(PACKAGE_EXISTS_IDENTIFIER));
			containsPackageName = true;
			
		} else {
			className = currentLine;
			containsPackageName = false;
		}
		
		if (className.equals(className.toLowerCase())) {
			className = className.toUpperCase();			
		} 
		
		for (int i = 0, m=0; i < className.length(); i++) {
			if (Character.isUpperCase(className.charAt(i)) 
					&& i != 0) {
				currentLineClassElements.add(className.substring(m, i));
				m = i;
			} else if (i+1 == className.length()) {
				currentLineClassElements.add(className.substring(m, i+1));
			}
		}
		
		if (containsPackageName) {
			currentLineClassElements.add(packageName); // store package name at the end of class elements list
		}
		
		return currentLineClassElements
				.toArray(new String[currentLineClassElements.size()]);		
	}
	
	/**
	 * To find matching classes from file
	 * this method implements the chosen algorithm for searching
	 * 
	 * @param queryElemsIn		String[]-type prepared query 
	 * @param classesListIn		ArrayList<String[]>-type prepared classes list
	 * @return
	 * @throws Exception
	 */
	public int findMatches(String[] queryElemsIn, ArrayList<String[]> classesListIn) throws Exception {

		if (queryElemsIn.length < 1) {
			throw new Exception("Works only with valid query");
		}
		
		String[] queryElements = queryElemsIn; //less overhead
		ArrayList<String[]> classesList = classesListIn; //more overhead, but necessary
		
		// TEMPORARY
		int matchCount = 0;
		
		for (String[] classRow : classesList) {

			boolean previousElementsMatch = true; // holds info about class previous elements
			for (int queryRowElementCount = 0, classRowElementCount = 0
					; queryRowElementCount < queryElements.length
					; queryRowElementCount++, classRowElementCount++) {
				
				String currentClassElement = classRow[classRowElementCount];
				String currentQueryElement = queryElements[queryRowElementCount];
				boolean currentQueryElementContainsWildcard = currentQueryElement.contains(WILDCARD_IDENTIFIER);
				
				// No point in looking at current class, 
				// if class element is shorter than query element
				if (currentClassElement.length() < currentQueryElement.trim().length()) {
					break;
				}
				
				if (currentQueryElement.endsWith(NAME_ENDING_IDENTIFIER)) {
					/**
					 * ########################################################
					 * SIMPLE MATCH - full string match with ending " " - faster
					 * Handles anything that looks for complete query element match 
					 * e.g. "Fo " in "Fo" -> true
					 * 		"Fo " in "Foo" -> false
					 * 		"Fo " in " Fo " - true
					 * 		"Fo " in " Foo " -> false
					 * ########################################################
					 */
					if (currentQueryElement.trim().equals(currentClassElement)) {
						if (queryElements.length == (queryRowElementCount+1)) {
							printMatch(queryRowElementCount, classRowElementCount, classRow);
							matchCount++;
							previousElementsMatch = true;
						} else {
							throw new Exception("Invalid class name");
						}
					} else {
						if (queryElements.length == (queryRowElementCount+1)) {
							previousElementsMatch = false;
						} else {
							throw new Exception("Invalid class name");
						}
						
					}
				} else if (currentClassElement.startsWith(currentQueryElement.trim())) {
					/** 
					 * ########################################################
					 * SIMPLE MATCH - beginnings of strings match - faster
					 * Handles anything that looks for query element name beginnings match
					 * e.g. "Fo" in "Foo"  -> true
					 * 		"Foo" in "Foo" -> true
					 * 		" Foo" in "Foo -> true
					 * ########################################################
					*/
					if (queryElements.length == (queryRowElementCount+1)) {
						printMatch(queryRowElementCount, classRowElementCount, classRow);
						matchCount++; // TEMPORARY
						previousElementsMatch = true;
						
					}  /* Otherwise, just continues with next same class element, if is lagged, change previous elements matching to true */
					if (queryRowElementCount != classRowElementCount) {
						previousElementsMatch = true;
					}
				} else if (currentQueryElementContainsWildcard){
					/** 
					 * ########################################################
					 * COMPLEX MATCH - fuzzy strings match - slower
					 * ########################################################
					 * if no results with simple match, continues with complex match
					 * 		includes special cases like '*'
					*/
					
					for (int elementLetterCount = 0
							; elementLetterCount < currentQueryElement.length()
							; elementLetterCount++) {		
						String currentClassLetter = currentClassElement
											.substring(elementLetterCount, elementLetterCount+1);
						String currentQueryLetter = currentQueryElement
											.substring(elementLetterCount, elementLetterCount+1);
								if (currentClassLetter.equals(currentQueryLetter)) {
									/* Match exists so far - do nothing and continue with next letters*/
								} else if (currentQueryLetter.equals(WILDCARD_IDENTIFIER)) {
									/* Match exists so far - do nothing and continue with next letters*/
								} else {
									previousElementsMatch = false;
								}
								if (currentQueryElement.length() == (elementLetterCount+1) 
										&& (queryRowElementCount+1) == queryElements.length) {
									printMatch(queryRowElementCount, classRowElementCount, classRow);
									matchCount++; //TEMPORARY
									previousElementsMatch = true;
									break;
								}  /* Otherwise, just continues with next same element letter, if is lagged, change previous elements matching to true  */
								if (queryRowElementCount != classRowElementCount) {
									previousElementsMatch = true;
								}
							}
				} else {
					previousElementsMatch = false; // no match between query and class name when looking at same location (e.g. comparing first elements)
				}
				if (!previousElementsMatch) {
					/**
					 * Enables lagged queries like the following to be found: 
					 * 'BarBaz' -> "FooBarBaz" -> true
					 * Does it by setting the query element to 
					 * class element comparison to a lag of -1 whenever possible
					 */
					int queryElementsLeftToCheck = (queryElements.length - (queryRowElementCount+1));
					int classElementsLeftToCheck = classRow.length - (classRowElementCount+1);
					boolean classContainsPackage = classRow[classRow.length-1].contains(".");
					
					if (classContainsPackage) {
						if (queryElementsLeftToCheck < (classElementsLeftToCheck-1)) {
							queryRowElementCount--; // perform lagged match finding
						} else {
							previousElementsMatch = true; // reset before looking at next class
							break; // no match for this class
						}
					} else if (!classContainsPackage) {
						if (queryElementsLeftToCheck < classElementsLeftToCheck) {
							queryRowElementCount--; // perform lagged match finding
						} else {
							previousElementsMatch = true; // reset before looking at next class
							break; // no match for this class
						}
						
					} else {
						previousElementsMatch = true; // reset before looking at next class
						break; // no match for this class
					}
				}
			}
		}

	return matchCount;//TEMPORARY
	}
	
	/**
	 * To print out class names with their package (if exists)
	 * to an initial standard, it rebuilds the initial class name
	 * for matching classes
	 * @param queryRowElementCount		int-type query element position
	 * 										where match was found
	 * @param classRowElementCount		int-type class element position
	 * 										where match was found
	 * @param classRow					String[]-type array of matched class elements
	 */
	public void printMatch(int queryRowElementCount
			, int classRowElementCount
			, String[] classRow) {
		
		StringBuilder classNameElements = new StringBuilder();
		
		if (queryRowElementCount != classRowElementCount) {
			classNameElements.append(classRow[classRow.length-1]);
			for (int i = 0; i < classRow.length-1; i++) {
				classNameElements.append(classRow[i]);
			}
		} else {
			for (String classNameElement : classRow) {
				classNameElements.append(classNameElement);
			}
		}

		System.out.println(classNameElements);
	}

	
}

package ee.classfinder;

public class Matcher {

	private static final String WILDCARD_IDENTIFIER = "*"; // wildcard to replace one character
	private static final String NAME_ENDING_IDENTIFIER = " "; // only accepts spaces as name ending chars

	/**
	 * To find matching classes from file
	 * this method implements the chosen algorithm for searching
	 * 
	 * @param queryElemsIn	String[]-type prepared query 
	 * @param classesListIn	ArrayList<String[]>-type prepared classes list
	 * @return
	 * @throws Exception
	 */
	public boolean findMatches(String[] queryElemsIn, 
								String[] classElemsIn) {

		StringBuilder classElements = new StringBuilder();
		int classElemCounter = 0;
		int queryElemCounter = 0;
		int classElemTotal = classElemsIn.length - 1; // ignore package, last element
		int queryElemTotal = queryElemsIn.length - 1; // ignore package, last element
		int matchedElements = 0;
		boolean fullElementSearch = false;
		
		
		for (String classElem : classElemsIn) {
			if (classElem == null) { 							// no match found
				return false; 		
			}
			
			if ((classElemTotal-1) - classElemCounter 			
					< (queryElemTotal-1) - queryElemCounter) { 	//  no match found
				return false;									
			}
			
			if ((classElemTotal-1) < classElemCounter 
					&& classElemTotal > queryElemTotal) {		// no match found
				return false;
			}
			
			for (; queryElemCounter < queryElemTotal ;) {
				fullElementSearch = queryElemsIn[queryElemCounter]
											.substring(queryElemsIn[queryElemCounter]
														.length()-1)
											.equals(NAME_ENDING_IDENTIFIER);


				if ((queryElemTotal-1) <= queryElemCounter 
						&& queryElemTotal > classElemTotal) {		// no match found
					return false;
				}
				
				if (queryElemsIn[queryElemCounter].length() <= 0) {	// no match found
					return false;
				}
				if (fullElementSearch 
						? elementsMatchInFull(classElem,queryElemsIn[queryElemCounter]) 
								: elementsMatch(classElem,queryElemsIn[queryElemCounter])) {
					++queryElemCounter;
					++matchedElements;
					break; // compare next query element against next class element
					
				} else {
					if (queryElemCounter > 0) { // no match found, e.g. q:"FooBaz"
						return false;			// and class: "FooBarBaz"
					}
					break;	// compare same query element against next class element
				}			
			}
			classElements.append(classElem);
			
			if (matchedElements == queryElemTotal) {
				if (fullElementSearch 
					&& (classElemsIn.length-1) > (classElemCounter+1)
					) {
					return false;	// no match found
				}
				return true;	// all query elements exist in the class elements
			}
			++classElemCounter;
		}
		return false;
	}
	
	/**
	 * To see whether an element (e.g. 'Camel' from 'CamelCase') in class file
	 * matches with user query
	 * this method performs the checks and returns true or false
	 * 
	 * @param classElem		String-type class element from file
	 * @param queryElem		String-type query element from user request
	 * @return
	 */
	private boolean elementsMatch(String classElem
									, String queryElem) {
		 
		if (classElem.length() < queryElem.trim().length()) { // class is shorter than query
			return false;
		} else if (classElem.startsWith(queryElem.trim())) { // beginnings match
			return true;
			
		} else if (queryElem
					.trim()
					.contains(WILDCARD_IDENTIFIER)) { // query has wildcard

			for (int i = 0
					; i < queryElem.trim().length()
					; i++) {	// letter-by-letter search
				
				if ((queryElem
							.trim()
							.charAt(i) != classElem
												.charAt(i)) 
						&& !queryElem
								.trim()
								.substring(i, i+1)
								.equals(WILDCARD_IDENTIFIER)) { //no match

					return false;
					
				}
			}
			return true;
		}
		
		return false;
	}
	

	/**
	 * To see whether an element in class file
	 * matches with user query in full 
	 * 			(e.g. 'CaCase ' matches 'CamelCase', not 'CamelCaseZest')
	 * this method performs the checks and returns true or false
	 * 
	 * @param classElem		String-type class element from file
	 * @param queryElem		String-type query element from user request
	 * @return
	 */
	private boolean elementsMatchInFull(String classElem
									, String queryElem) {
		 
		boolean queryContainsWildcard = queryElem
										.trim()
										.contains(WILDCARD_IDENTIFIER);
		
		
		if (!queryContainsWildcard) { 
			if (classElem.equals(queryElem.trim())) {
				return true;
			}
		} else if (queryContainsWildcard 
				&& queryElem.trim().length() == classElem.length()) {

			for (int i = 0
					; i < queryElem.trim().length()
					; i++) {	// letter-by-letter search
				
				if ((queryElem
							.trim()
							.charAt(i) != classElem
												.charAt(i)) 
						&& !queryElem
								.trim()
								.substring(i, i+1)
								.equals(WILDCARD_IDENTIFIER)) { 
					return false; //no match
					
				}
			}
			return true;
		}
		return false;
	}
}

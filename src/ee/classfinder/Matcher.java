package ee.classfinder;

public class Matcher {
	
	private StringBuilder classElements = new StringBuilder();
	private int classElemCounter = 0;
	private int queryElemCounter = 0;
	private int classElemTotal = 0; 
	private int queryElemTotal = 0;
	private int matchedElements = 0;
	private boolean fullElementSearch = false;
	
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

		classElemTotal = classElemsIn.length - 1; // ignore package, last element
		queryElemTotal = queryElemsIn.length - 1; // ignore package, last element
		
		
		for (String classElem : classElemsIn) {
			
			if (classCantBeMatched(classElem)) {
				return false;
			}
			
			for (; queryElemCounter < queryElemTotal ;) {
				fullElementSearch = 
						queryElemsIn[queryElemCounter]
								.substring(queryElemsIn[queryElemCounter]
															.length()-1)
								.equals(NAME_ENDING_IDENTIFIER);

				if (queryCantBeMatched(queryElemsIn[queryElemCounter])) {
					return false;
				}

				
				if (elementMatchIsFound(fullElementSearch
										, classElem
										, queryElemsIn[queryElemCounter])) {
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
				if (fullQueryDoesntMatchWith(classElemsIn.length)) {
					return false;	// no match found
				}
				return true;	// all query elements exist in the class elements
			}
			++classElemCounter;
		}
		return false;
	}
	
	/**
	 * Before resulting in a match between query and class
	 * this method checks whether query requires full element 
	 * match, and if class doesn't satisfy it, then returns false
	 * 
	 * @param classElemsLength		class elements length count
	 * @return
	 */
	
	private boolean fullQueryDoesntMatchWith(int classElemsLength) {
		
		boolean noMatch = false;
		
		if (fullElementSearch 
				&& (classElemsLength-1) > (classElemCounter+1)
				) {
			noMatch = true;
		}
		
		return noMatch;
	}
	
	/**
	 * Check whether elements match
	 * 
	 * @param fullElementSearch	boolean-type whether full element is searched
	 * @param classElem			String-type current class element
	 * @param queryElem			String-type current query element
	 * @return
	 */
	private boolean elementMatchIsFound(boolean fullElementSearch, String classElem, String queryElem) {
		
		boolean noMatch = false;
		
		if (fullElementSearch 
				? elementsMatchInFull(classElem,queryElem) 
						: elementsMatch(classElem,queryElem)) {
			noMatch = true;
		}
		return noMatch;
	}
	
	/**
	 * Basic check whether obvious no matches can be ignored
	 * @param classElem		class element to be checked 
	 * 						(e.g. 'Camel' in 'CamelCase')
	 * @return
	 */
	private boolean classCantBeMatched(String classElem) {

		boolean noMatch = false;
		
		if (classElem == null) { 							// no match found
			noMatch = true;		
		}
		
		if ((classElemTotal-1) - classElemCounter 			
				< (queryElemTotal-1) - queryElemCounter) { 	//  no match found
			noMatch = true;								
		}
		
		if ((classElemTotal-1) < classElemCounter 
				&& classElemTotal > queryElemTotal) {		// no match found
			noMatch = true;
		}
		
		return noMatch;
	}
	
	/**
	 * Basic check whether obvious no matches can be ignored
	 * @param queryElem		query element to be checked 
	 * 						(e.g. 'Camel' in 'CamelCase')
	 * @return
	 */
	private boolean queryCantBeMatched(String queryElem) {
		
		boolean noMatch = false;
		
		if ((queryElemTotal-1) <= queryElemCounter 
				&& queryElemTotal > classElemTotal) {		// no match found
			noMatch = true;
		}
		
		if (queryElem.length() <= 0) {						// no match found
			noMatch = true;
		}
		
		return noMatch;
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

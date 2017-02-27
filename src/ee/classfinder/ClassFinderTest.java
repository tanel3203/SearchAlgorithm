package ee.classfinder;
import static org.junit.Assert.*;

import org.junit.Test;

public class ClassFinderTest {
	
	@Test
	public void testIt() throws Exception {
		ClassFinder classFinder = new ClassFinder();
		// tavaline töötab
		//classFinder.main(new String[]{"resources/classes.txt", "Foo"});
		// tavaline töötab
		classFinder.main(new String[]{"resources/classes.txt", "B ar "});
	}
//	
//	@Test(expected = Exception.class)
//	public void testEmptyQueryThrowsException() throws Exception {
//		ClassFinder classFinder = new ClassFinder();
//		
//		// Test cases
//		classFinder.compareQueryAndClasses(new String[]{});
//	}
//	
//	@Test(expected = Exception.class)
//	public void testInvalidQueryThrowsException() throws Exception {
//		ClassFinder classFinder = new ClassFinder();
//		
//		// Test cases
//		classFinder.compareQueryAndClasses(new String[]{"Fo ","Ba"});	
//	}
//	
//	@Test
//	public void testFullQueryWithTrailingSpace() {
//		ClassFinder classFinder = new ClassFinder();
//		
//		// Test cases
//		try {
//			assertEquals(1, classFinder.compareQueryAndClasses(new String[]{"Fo","Bar "}));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//
//			e.printStackTrace();
//		}
//		
//	}
//	
//	@Test
//	public void testFullQueryWithMultipleTrailingAndStartingSpaces() {
//		ClassFinder classFinder = new ClassFinder();
//		
//		// Test cases
//		try {
//			assertEquals(1, classFinder.compareQueryAndClasses(new String[]{"  Fo"," Bar   "}));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//
//			e.printStackTrace();
//		}
//		
//	}
//	
//	@Test
//	public void testClassAndPackageRows() {
//		
//	}
//	
//	@Test
//	public void InitialTests () throws Exception {
//		ClassFinder classFinder = new ClassFinder();
//		
//		// Initial tests
//		
//		assertEquals(classFinder.compareQueryAndClasses(new String[]{"Fo*","Ba"}),2);
//		assertEquals(classFinder.compareQueryAndClasses(new String[]{"Fo","B*"}),2);
//		assertEquals(classFinder.compareQueryAndClasses(new String[]{"Fo*","Ba"}),2);
//		assertEquals(classFinder.compareQueryAndClasses(new String[]{"Fo*","Ba"}),2);
//		assertEquals(classFinder.compareQueryAndClasses(new String[]{"Foo","B*r"}),1);
//		assertEquals(classFinder.compareQueryAndClasses(new String[]{"Foo","Bz"}),0);
//		
//		

		
		// Search pattern `<pattern>` must include 
		// class name camelcase upper case letters
		// in the right order and it may contain lower case 
		// letters to narrow down the search results
//		assertEquals("FB", "a.b.FooBarBaz");
//		assertEquals("FB", "c.d.FooBar");
//		assertEquals("FoBa", "a.b.FooBarBaz");
//		assertEquals("FoBa", "c.d.FooBar");
//		assertEquals("FBar", "a.b.FooBarBaz");
//		assertEquals("FBar", "c.d.FooBar");
		
		// Upper case letters written in the wrong order 
		// will not find any results
//		assertNotEquals("BF", "c.d.FooBar");
		
		// If the search pattern consists of only lower case 
		// characters then the search becomes case insensitive
//		assertEquals("fbb", "FooBarBaz");
//		assertNotEquals("fBb", "FooBarBaz"); // not all lowercase
		
		// The search pattern may include wildcard characters `'*'` 
		// which match missing letters
//		assertEquals("B*rBaz", "FooBarBaz");
//		assertNotEquals("BrBaz", "FooBarBaz"); // not all lowercase
		
		// The found class names must be sorted in alphabetical order 
		// ignoring package names (package names must still be included in the output).
		
		
//		Solution limitations:
//			- Regexp must not be used.
//			- Use of other libraries (other than the language itself) is prohibited.
//
//			Solution requirements:
//			- Unit tests must be present. For unit tests you are allowed to use libraries
		
//		It is good enough if the solution implements the functionality described in this file.
		
//	}

}
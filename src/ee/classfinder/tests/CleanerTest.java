package ee.classfinder.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import ee.classfinder.Cleaner;

public class CleanerTest {
	
	Cleaner cleaner = new Cleaner();
	
	@Test
	public void testPrepareArrayAsStringWithPackage() {
		String[] classAndPackage = {"Foo", "Bar", "Baz", "codeborne."};
		boolean reverseClassAndPackage = true;
		
		String result = cleaner
						.prepareArrayAsClassString(classAndPackage
													, reverseClassAndPackage);
		
		assertEquals("codeborne.FooBarBaz", result);
	}
	
	@Test
	public void testPrepareArrayAsStringNoPackage() {
		String[] classAndPackage = {"Foo", "Bar", "Baz", null};
		boolean reverseClassAndPackage = true;
		
		String result = cleaner
						.prepareArrayAsClassString(classAndPackage
													, reverseClassAndPackage);
		
		assertEquals("FooBarBaz", result);
	}
	
	@Test
	public void testPrepareArrayAsStringNotReversed() {
		String[] classAndPackage = {"Foo", "Bar", "Baz", "a.b."};
		boolean reverseClassAndPackage = false;
		
		String result = cleaner
						.prepareArrayAsClassString(classAndPackage
													, reverseClassAndPackage);
		
		assertEquals("FooBarBaza.b.", result);
	}
	
	@Test
	public void testPrepareArrayAsStringNotReversedNoPackage() {
		String[] classAndPackage = {"Foo", "Bar", "Baz", null};
		boolean reverseClassAndPackage = false;
		
		String result = cleaner
						.prepareArrayAsClassString(classAndPackage
													, reverseClassAndPackage);
		
		assertEquals("FooBarBaznull", result);
	}
	
	@Test
	public void testPrepareNameAsArray() throws Exception {
		String name = "a.b.CamelCase";
		boolean trimEntry = true;
		
		String[] result = cleaner.prepareNameAsArray(name, trimEntry);
		String[] expectation = new String[]{"Camel", "Case", "a.b."};

		assertTrue(result[0].equals(expectation[0]));
		assertTrue(result[1].equals(expectation[1]));
		assertTrue(result[2].equals(expectation[2]));
		
	}
	
	@Test
	public void testPrepareNameAsArrayWithNull() throws Exception {
		String name = "CamelCase";
		boolean trimEntry = true;
		
		String[] result = cleaner.prepareNameAsArray(name, trimEntry);
		String[] expectation = new String[]{"Camel", "Case", null};
		
		assertTrue(result[0].equals(expectation[0]));
		assertTrue(result[1].equals(expectation[1]));
		assertNull(result[2]);
		assertNull(expectation[2]);
		
	}
	
}

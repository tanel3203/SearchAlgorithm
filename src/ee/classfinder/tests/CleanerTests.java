package ee.classfinder.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import ee.classfinder.Cleaner;

public class CleanerTests {
	
	Cleaner cleaner = new Cleaner();
	
	@Test
	public void testCleanArrayAsStringWithPackage() {
		String[] classAndPackage = {"Foo", "Bar", "Baz", "codeborne."};
		boolean reverseClassAndPackage = true;
		
		String result = cleaner
						.prepareArrayAsClassString(classAndPackage
													, reverseClassAndPackage);
		
		assertEquals("codeborne.FooBarBaz", result);
	}
	
	@Test
	public void testCleanArrayAsStringNoPackage() {
		String[] classAndPackage = {"Foo", "Bar", "Baz", null};
		boolean reverseClassAndPackage = true;
		
		String result = cleaner
						.prepareArrayAsClassString(classAndPackage
													, reverseClassAndPackage);
		
		assertEquals("FooBarBaz", result);
	}
	
	@Test
	public void testCleanArrayAsStringNotReversed() {
		String[] classAndPackage = {"Foo", "Bar", "Baz", "a.b."};
		boolean reverseClassAndPackage = false;
		
		String result = cleaner
						.prepareArrayAsClassString(classAndPackage
													, reverseClassAndPackage);
		
		assertEquals("FooBarBaza.b.", result);
	}
	
	@Test
	public void testCleanArrayAsStringNotReversedNoPackage() {
		String[] classAndPackage = {"Foo", "Bar", "Baz", null};
		boolean reverseClassAndPackage = false;
		
		String result = cleaner
						.prepareArrayAsClassString(classAndPackage
													, reverseClassAndPackage);
		
		assertEquals("FooBarBaznull", result);
	}
	
}

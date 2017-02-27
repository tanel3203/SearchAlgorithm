package ee.classfinder.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import ee.classfinder.Matcher;

public class MatcherTest {
	Matcher matcher = new Matcher();
	
	@Test
	public void testOneToOneMatch() {
		String[] queryElems = {"Foo", "Bar", null};
		String[] classElems = {"Foo", "Bar", null};

		assertTrue(matcher.findMatches(queryElems, classElems));
	}
	
	@Test
	public void testOneToOneMatchWithDifferentPackage() {
		String[] queryElems = {"Foo", "Bar", "codeborne."};
		String[] classElems = {"Foo", "Bar", "a.b."};

		assertTrue(matcher.findMatches(queryElems, classElems));
	}
	
	@Test
	public void testOneToOneMatchWithFullQuery() {
		String[] queryElems = {"Foo", "Bar ", "codeborne."};
		String[] classElems = {"Foo", "Bar", "a.b."};

		assertTrue(matcher.findMatches(queryElems, classElems));
	}
	
	@Test
	public void testOneToOneMatchWithAsterisk() {
		String[] queryElems = {"Foo", "B*r", "codeborne."};
		String[] classElems = {"Foo", "Bar", "a.b."};

		assertTrue(matcher.findMatches(queryElems, classElems));
	}
	
	@Test
	public void testOneToOneMatchWithMultiAsterisk() {
		String[] queryElems = {"F*o", "B**", "codeborne."};
		String[] classElems = {"Foo", "Bar", "a.b."};

		assertTrue(matcher.findMatches(queryElems, classElems));
	}
	
	@Test
	public void testOneToOneNotMatch() {
		String[] queryElems = {"Foo", "Ba ", "codeborne."};
		String[] classElems = {"Foo", "Bar", "a.b."};

		assertFalse(matcher.findMatches(queryElems, classElems));
	}
	
	@Test
	public void testOneToOnePartialMatch() {
		String[] queryElems = {"Fo", "Ba", "codeborne."};
		String[] classElems = {"Foo", "Bar", "a.b."};

		assertTrue(matcher.findMatches(queryElems, classElems));
	}
	
	@Test
	public void testOneToManyMatch() {
		String[] queryElems = {"Foo", "Bar", "codeborne."};
		String[] classElems = {"Foo", "Bar", "Baz", "a.b."};

		assertTrue(matcher.findMatches(queryElems, classElems));
	}
	
	@Test
	public void testOneToManyLaggedMatch() {
		String[] queryElems = {"Bar", "Baz", "codeborne."};
		String[] classElems = {"Foo", "Bar", "Baz", "a.b."};

		assertTrue(matcher.findMatches(queryElems, classElems));
	}
	
	@Test
	public void testOneToManyLaggedMatchWithFullQuery() {
		String[] queryElems = {"Bar", "Baz ", "codeborne."};
		String[] classElems = {"Foo", "Bar", "Baz", "a.b."};

		assertTrue(matcher.findMatches(queryElems, classElems));
	}
	
	@Test
	public void testOneToManyLaggedMatchWithAsterisk() {
		String[] queryElems = {"Bar", "B*z", "codeborne."};
		String[] classElems = {"Foo", "Bar", "Baz", "a.b."};

		assertTrue(matcher.findMatches(queryElems, classElems));
	}
	
	@Test
	public void testOneToManyLaggedNoMatchWithFullQuery() {
		String[] queryElems = {"Bar ", "codeborne."};
		String[] classElems = {"Foo", "Bar", "Baz", "a.b."};

		assertFalse(matcher.findMatches(queryElems, classElems));
	}
	
	@Test
	public void testManyToOneNoMatch() {
		String[] queryElems = {"Foo", "Bar", "Baz", "codeborne."};
		String[] classElems = {"Baz", "a.b."};

		assertFalse(matcher.findMatches(queryElems, classElems));
	}
	
	
	
	
}


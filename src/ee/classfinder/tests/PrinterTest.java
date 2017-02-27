package ee.classfinder.tests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ee.classfinder.Printer;

public class PrinterTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	Printer printer = new Printer();
	
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}
	
	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Test
	public void printResults() {

		TreeMap<String, String> classes = new TreeMap<>(); 
		classes.put("FooBarcodeborne.", "codeborne.FooBar");
		printer.printMatches(classes);
		assertEquals("codeborne.FooBar\n", outContent.toString());
			
	}
	

}

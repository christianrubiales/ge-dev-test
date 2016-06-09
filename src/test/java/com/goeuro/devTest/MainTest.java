package com.goeuro.devTest;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * @see {@link Main}
 */
public class MainTest {

	@Test(expected=NullPointerException.class)
	public void testMain_nullArgument_shouldThrowNullPointerException() throws IOException, QueryProcessorException {
		Main.main(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMain_containsNullArgument_shouldThrowIllegalArgumentException() throws IOException, QueryProcessorException {
		Main.main(new String[] {"not null", null});
	}

	@Test(expected=IllegalArgumentException.class)
	public void testMain_emptyArgument_shouldThrowIllegalArgumentException() throws IOException, QueryProcessorException {
		Main.main(new String[] {});
	}

	@Test(expected=NullPointerException.class)
	public void testGetLocationFromArguments_nullArgument_shouldThrowNullPointerException() {
		Main.getLocationFromArguments(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetLocationFromArguments_containsNullArgument_shouldThrowIllegalArgumentException() {
		Main.getLocationFromArguments(new String[] {"not null", null});
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetLocationFromArguments_emptyArgument_shouldThrowIllegalArgumentException() {
		Main.getLocationFromArguments(new String[] {});
	}
	
	@Test
	public void testGetLocationFromArguments_singleWordLocation() {
		assertEquals("Berlin", Main.getLocationFromArguments(new String[] {"Berlin"}));
	}
	
	@Test
	public void testGetLocationFromArguments_singleWordLocationInSingleQuotes() {
		assertEquals("Berlin", Main.getLocationFromArguments(new String[] {"\'Berlin\'"}));
	}
	
	@Test
	public void testGetLocationFromArguments_singleWordLocationInDoubleQuotes() {
		assertEquals("Berlin", Main.getLocationFromArguments(new String[] {"\"Berlin\""}));
	}
	
	@Test
	public void testGetLocationFromArguments_multiWordLocationInSingleQuotes() {
		assertEquals("Alcazar del Rey", Main.getLocationFromArguments(new String[] {"\'Alcazar del Rey\'"}));
	}
	
	@Test
	public void testGetLocationFromArguments_multiWordLocationInDoubleQuotes() {
		assertEquals("Alcazar del Rey", Main.getLocationFromArguments(new String[] {"\"Alcazar del Rey\""}));
	}
	
	@Test
	public void testGetLocationFromArguments_multiWordLocationNotEntirelyInQuotes() {
		assertEquals("Alcazar del Rey", Main.getLocationFromArguments(new String[] {"\"Alcazar\"", "\'del\'", "Rey"}));
	}
}

package com.goeuro.devTest.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;

import com.goeuro.devTest.QueryProcessorException;

/**
 * @see {@link QueryProcessorImpl}
 */
public class QueryProcessorImplTest {
	
	private static final String JSON = 
			"[{\"_id\":376217,\"name\":\"Berlin\",\"type\":\"location\","
			+ "\"geo_position\":{\"latitude\": 52.52437,\"longitude\": 13.41053}}]";
	
	private QueryProcessorImpl processor;
	
	@Before
	public void setUp() {
		processor = new QueryProcessorImpl("endpointBaseUrl/", "csvBaseDirectory", "jsonEncoding");
	}

	// Constructor tests
	
	@Test(expected=NullPointerException.class)
	public void testQueryProcessorImpl_endpointBaseUrlNull_mustThrowNullPointerException() {
		new QueryProcessorImpl(null, "csvBaseDirectory", "jsonEncoding");
	}
	
	@Test(expected=NullPointerException.class)
	public void testQueryProcessorImpl_csvBaseDirectoryNull_mustThrowNullPointerException() {
		new QueryProcessorImpl("endpointBaseUrl", null, "jsonEncoding");
	}
	
	@Test(expected=NullPointerException.class)
	public void testQueryProcessorImpl_jsonEncodingNull_mustThrowNullPointerException() {
		new QueryProcessorImpl("endpointBaseUrl", "csvBaseDirectory", null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testQueryProcessorImpl_endpointBaseUrlBlank_mustThrowIllegalArgumentException() {
		new QueryProcessorImpl(" ", "csvBaseDirectory", "jsonEncoding");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testQueryProcessorImpl_csvBaseDirectoryBlank_mustThrowIllegalArgumentException() {
		new QueryProcessorImpl("endpointBaseUrl", " ", "jsonEncoding");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testQueryProcessorImpl_jsonEncodingBlank_mustThrowIllegalArgumentException() {
		new QueryProcessorImpl("endpointBaseUrl", "csvBaseDirectory", " ");
	}
	
	@Test
	public void testQueryProcessorImpl() {
		assertNotNull(new QueryProcessorImpl("endpointBaseUrl", "csvBaseDirectory", "jsonEncoding"));
	}
	
	@Test
	public void testQueryProcessorImpl_endpointBaseUrlNoTrailingSlash_slashMustBeAdded() throws IllegalAccessException {
		processor = new QueryProcessorImpl("endpointBaseUrl", "csvBaseDirectory", "jsonEncoding");
		
		assertEquals("endpointBaseUrl/", FieldUtils.readField(processor, "endpointBaseUrl", true));
	}
	
	@Test
	public void testQueryProcessorImpl_endpointBaseUrlWithTrailingSlash_slashMustNotBeAdded() throws IllegalAccessException {
		assertEquals("endpointBaseUrl/", FieldUtils.readField(processor, "endpointBaseUrl", true));
	}

	// process()
	
	@Test
	public void testProcess() throws QueryProcessorException {
		processor = new QueryProcessorImpl("endpointBaseUrl", "csvBaseDirectory", "jsonEncoding") {
			protected InputStream getUrlInputStream(String location) throws MalformedURLException, IOException {
				return new ByteArrayInputStream(JSON.getBytes());
			}
			protected void createDirectoryIfNotExisting(String directory) {
			}
			protected void writeCsv(File file, String csv, String encoding, boolean append) throws IOException {
			}
		};
		processor.process("location");
	}
	
	@Test(expected=QueryProcessorException.class)
	public void testProcess_encountersMalformedURLException_mustThrowQueryProcessorException() throws QueryProcessorException {
		processor = new QueryProcessorImpl("endpointBaseUrl", "csvBaseDirectory", "jsonEncoding") {
			protected InputStream getUrlInputStream(String location) throws MalformedURLException, IOException {
				throw new MalformedURLException();
			}
		};
		processor.process("location");
	}
	
	@Test(expected=QueryProcessorException.class)
	public void testProcess_encountersIOException_mustThrowQueryProcessorException() throws QueryProcessorException {
		processor = new QueryProcessorImpl("endpointBaseUrl", "csvBaseDirectory", "jsonEncoding") {
			protected InputStream getUrlInputStream(String location) throws MalformedURLException, IOException {
				throw new IOException();
			}
		};
		processor.process("location");
	}
	
	// others

	@Test(expected=NullPointerException.class)
	public void testFlatten_null_muustThrowNullPointerException() {
		processor.flatten(null);
	}

	@Test(expected=NullPointerException.class)
	public void testFlatten_nullGeoPosition_muustThrowNullPointerException() {
		LocationJson json = new LocationJson();
		json.set_id(1);
		json.setName("name");
		json.setType("type");
		processor.flatten(json);
	}

	@Test
	public void testFlatten() {
		GeoPosition geoPosition = new GeoPosition();
		geoPosition.setLatitude("latitude");
		geoPosition.setLongitude("longitude");
		
		LocationJson json = new LocationJson();
		json.set_id(1);
		json.setName("name");
		json.setType("type");
		json.setGeoPosition(geoPosition);
		
		Object[] object = processor.flatten(json);
		
		assertEquals(object[0], 1);
		assertEquals(object[1], "name");
		assertEquals(object[2], "type");
		assertEquals(object[3], "latitude");
		assertEquals(object[4], "longitude");
	}

	@Test
	public void testWriteCsv() throws IOException {
		String prefix = Integer.toString(new Random().nextInt(Integer.MAX_VALUE));
		String suffix = Integer.toString(new Random().nextInt(Integer.MAX_VALUE));
		File file = File.createTempFile(prefix, suffix);
		
		processor.writeCsv(file, "csv", "UTF-8", true);
		
		assertEquals("csv", FileUtils.readFileToString(file, "UTF-8"));
		file.delete();
	}

	@Test
	public void testCreateDirectoryIfNotExisting_notExisting_mustBeCreated() {
		String name = Integer.toString(new Random().nextInt(Integer.MAX_VALUE));
		
		processor.createDirectoryIfNotExisting(name);
		File file = new File(name);
		
		assertTrue(file.exists());
		file.delete();
	}

	@Test
	public void testCreateDirectoryIfNotExisting_existing_mustNotBeCreated() {
		String name = Integer.toString(new Random().nextInt(Integer.MAX_VALUE));
		File file = new File(name);
		file.mkdir();
		
		processor.createDirectoryIfNotExisting(name);
		
		assertTrue(file.exists());
		file.delete();
	}

}

package com.goeuro.devTest.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.FastDateFormat;

import com.goeuro.devTest.QueryProcessor;
import com.goeuro.devTest.QueryProcessorException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

/**
 * Default implementation of {@link QueryProcessor}, uses streaming read of the returned JSON 
 * and immediately writes location to CSV to minimize memory use.
 */
public class QueryProcessorImpl implements QueryProcessor {
	
	/**
	 * Format for the resulting CSV files.
	 */
	private static final String FILENAME_FORMAT = "yyyyMMdd-HHmmssSSS";

	/**
	 * This is the URL prepended to the location being queried to form the proper API call.
	 */
	private final String endpointBaseUrl;
	
	/**
	 * The path of the base directory where the generated CSVs will be located.
	 */
	private final String csvBaseDirectory;
	
	/**
	 * The expected Encoding of the returned JSON, will also be used for writing the CSV.
	 */
	private final String jsonEncoding;
	
	/**
	 * Used to format the name of the resulting CSV files.
	 */
	private final FastDateFormat format;
	
	/**
	 * @param endpointBaseUrl the URL prepended to the location being queried to form the proper API call.
	 * Must not be {@code null} or blank.
	 * @param csvBaseDirectory path of the base directory where the generated CSVs will be located.
	 * 
     * @throws NullPointerException if {@code endpointBaseUrl} or {@code csvBaseDirectory} or {@code jsonEncoding} is {@code null}
     * @throws IllegalArgumentException if {@code endpointBaseUrl} or {@code csvBaseDirectory} or {@code jsonEncoding} is blank
	 */
	public QueryProcessorImpl(String endpointBaseUrl, String csvBaseDirectory, String jsonEncoding) {
		Validate.notBlank(endpointBaseUrl);
		Validate.notBlank(csvBaseDirectory);
		Validate.notBlank(jsonEncoding);
		
		if (!endpointBaseUrl.endsWith("/")) {
			endpointBaseUrl = endpointBaseUrl + "/";
		}
		this.endpointBaseUrl = endpointBaseUrl;
		this.csvBaseDirectory = csvBaseDirectory;
		this.jsonEncoding = jsonEncoding;
		this.format = FastDateFormat.getInstance(FILENAME_FORMAT);
	}

	/**
	 * Uses streaming read of the returned JSON and immediately writes location to CSV to minimize memory use.
	 * The name of the CSV will be in timestamp format.
	 * @see com.goeuro.devTest.QueryProcessor#process(java.lang.String)
	 */
	public File process(String location) throws QueryProcessorException {
		File file = null;
		Validate.notBlank(location);

		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		JsonReader reader = null;

		try {
			// prepare JSON reader
			inputStream = this.getUrlInputStream(location);
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			reader = new JsonReader(bufferedReader);
			Gson gson = new GsonBuilder().create();
			reader.beginArray();
	        
			this.createDirectoryIfNotExisting(this.csvBaseDirectory);
			String filename = format.format(new Date()) + ".csv";
			file = new File(this.csvBaseDirectory, filename);
			this.createFile(file);
			
			while (reader.hasNext()) {
	            // Read data into object model
	            LocationJson json = gson.fromJson(reader, LocationJson.class);
	            this.writeCsv(file, CSVFormat.DEFAULT.format(this.flatten(json)) + System.lineSeparator(), jsonEncoding, true);
	        }
		} catch (MalformedURLException mue) {
			throw new QueryProcessorException("Exception with the using the base url \"" 
					+ this.endpointBaseUrl + "\" and location \"" + location + "\"", mue);
		} catch (IOException ioe) {
			throw new QueryProcessorException(ioe.getLocalizedMessage(), ioe);
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(bufferedReader);
			IOUtils.closeQuietly(inputStreamReader);
			IOUtils.closeQuietly(inputStream);
		}
		
		return file;
	}

	/**
	 * @param location appended to the base URL
	 * @return the URL's input stream
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	protected InputStream getUrlInputStream(String location) throws MalformedURLException, IOException {
		InputStream inputStream;
		String encoded = this.endpointBaseUrl 
				+ URLEncoder.encode(location, jsonEncoding).replace("+", "%20");
		URL url = new URL(encoded);
		inputStream = url.openStream();
		
		return inputStream;
	}

	/**
	 * Write CSV String to file.
	 */
	protected void writeCsv(File file, String csv, String encoding, boolean append) throws IOException {
		FileUtils.write(file, csv, encoding, append);
	}
	
	/**
	 * @param non-flat JSON representation of a location.
	 * @return an {@code Object} array as a flat representation of the given {@code LocationJson} object.
	 */
	protected Object[] flatten(LocationJson json) {
		return new Object[] {
				json.get_id(), 
				json.getName(), 
				json.getType(), 
				json.getGeoPosition().getLatitude(), 
				json.getGeoPosition().getLongitude()
			};
	}
	
	/**
	 * @param directory directory to create, must not be blank
	 */
	protected void createDirectoryIfNotExisting(String directory) {
		Validate.notBlank(csvBaseDirectory);
		File file = new File(directory);
		if (!file.exists()) {
			file.mkdir();
		}
	}
	
	/**
	 * @param file the file to create
	 * @throws IOException
	 */
	protected void createFile(File file) throws IOException {
		file.createNewFile();
	}

}

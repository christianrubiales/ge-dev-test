package com.goeuro.devTest;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.Validate;

import com.goeuro.devTest.impl.QueryProcessorImpl;

/**
 * The main class for this application.
 */
public class Main {

	private static final String USAGE_MESSAGE = "Usage: java -jar GoEuroTest.jar \"CITY_NAME\"";

	private static final String PROPERTIES_FILE_PATH = "/GoEuroTest.properties";

	/**
	 * The URL prepended to the location being queried to form the proper API call.
	 */
	private static final String PROPERTY_BASE_URL = "BASE_URL";
	
	/**
	 * The path of the base directory where the generated CSVs will be located.
	 */
	private static final String PROPERTY_BASE_CSV_DIRECTORY = "BASE_CSV_DIRECTORY";
	
	/**
	 * The expected Encoding of the returned JSON, will also be used for writing the CSV.
	 */
	private static final String PROPERTY_JSON_ENCODING = "JSON_ENCODING";
	
	/**
	 * @param args the only argument is the city name. See {@ #getLocationFromArguments(String[])} for details.
	 * 
	 * @throws NullPointerException if {@code args} is {@code null}
	 * @throws IllegalArgumentException if {@code args} is empty or contains {@code null}
	 * @throws IOException 
	 * @throws QueryProcessorException 
	 */
	public static void main(String[] args) throws IOException, QueryProcessorException {
		
		// get supplied location
		
		String location = null;
		try {
			location = getLocationFromArguments(args);
		} catch (NullPointerException e) {
			System.out.println(USAGE_MESSAGE);
		} catch (IllegalArgumentException e) {
			System.out.println(USAGE_MESSAGE);
		}
		
		// proceed with API query and CSV writing
		
		if (location != null) {
			Properties properties = new Properties();
			properties.load(Main.class.getResourceAsStream(PROPERTIES_FILE_PATH));
			
			String baseUrl = properties.getProperty(PROPERTY_BASE_URL);
			String baseCsvDirectory = properties.getProperty(PROPERTY_BASE_CSV_DIRECTORY);
			String jsonEncoding = properties.getProperty(PROPERTY_JSON_ENCODING);
			QueryProcessor processor = new QueryProcessorImpl(baseUrl, baseCsvDirectory, jsonEncoding);
			processor.process(location);
		}
	}
	
	/**
	 * Returns the location as parsed from the arguments array.
	 * If there is only one argument, then that will be the location.
	 * If the argument is multi-word but is inside single or double quotes, then that will be the location.
	 * If the argument is multi-word but is not inside single or double quotes, all of the arguments will be joined with a space
	 * and will be treated as a single multi-word argument.
	 * If there are several arguments and some are in quotes and some are not, then the quotes will be ignored 
	 * and all of the arguments will be joined with a space and will be treated as a single multi-word argument.
	 * 
	 * @param args see {@link #main(String[])}
	 * @return the location as parsed from the arguments array.
	 * @throws NullPointerException if {@code args} is {@code null}
	 * @throws IllegalArgumentException if {@code args} is empty or contains {@code null}
	 */
	public static String getLocationFromArguments(String[] args) {
		Validate.noNullElements(args);
		Validate.notEmpty(args);
		
		String location = null;
		if (args.length == 1) {
			location = args[0].replace("\'", "").replace("\"", "");
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append(args[0].replace("\'", "").replace("\"", ""));
			for (int i = 1; i < args.length; i++) {
				sb.append(" ").append(args[i].replace("\'", "").replace("\"", ""));
			}
			location = sb.toString();
		}
		
		return location;
	}

}

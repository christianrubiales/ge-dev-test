package com.goeuro.devTest;

import java.io.File;

/**
 * Retrieves the JSON reply using the given location query and write the information to CSV.
 */
public interface QueryProcessor {

	/**
	 * Retrieve the JSON reply to the query and write to CSV.
	 * 
	 * @param location
	 * @return the created {@link File} from successful processing of the query.
	 * @throws QueryProcessorException when there are problems with processing the JSON or CSV
	 */
	public File process(String location) throws QueryProcessorException;
}

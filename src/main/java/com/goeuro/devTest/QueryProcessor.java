package com.goeuro.devTest;

/**
 * Retrieves the JSON reply using the given location query and write the information to CSV.
 */
public interface QueryProcessor {

	/**
	 * Retrieve the JSON reply to the query and write to CSV.
	 * 
	 * @param location
	 * @throws QueryProcessorException when there are problems with processing the JSON or CSV
	 */
	public void process(String location) throws QueryProcessorException;
}

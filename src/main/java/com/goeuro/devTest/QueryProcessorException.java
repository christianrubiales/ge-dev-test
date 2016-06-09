package com.goeuro.devTest;

/**
 * Thrown by {@link QueryProcessor} when there are problems with processing the JSON or CSV.
 */
public class QueryProcessorException extends Exception {

	private static final long serialVersionUID = 7889353395271665663L;

	/**
     * @see java.lang.Exception#Exception(String)
     */
    public QueryProcessorException(String message) {
        super(message);
    }
    
    /**
     * @see java.lang.Exception#Exception(String, Throwable)
     */
    public QueryProcessorException(String message, Throwable cause) {
        super(message, cause);
    }
}

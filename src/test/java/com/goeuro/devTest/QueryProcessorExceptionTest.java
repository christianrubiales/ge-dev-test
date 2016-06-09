package com.goeuro.devTest;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @see {@link QueryProcessorException}
 */
public class QueryProcessorExceptionTest {

	private static final String MESSAGE = "message";

	@Test
	public void testQueryProcessorExceptionString() {
		QueryProcessorException exception = new QueryProcessorException(MESSAGE);
		
		assertEquals(MESSAGE, exception.getMessage());
	}

	@Test
	public void testQueryProcessorExceptionStringThrowable() {
		Throwable cause = new Throwable();
		QueryProcessorException exception = new QueryProcessorException(MESSAGE, cause);
		
		assertEquals(MESSAGE, exception.getMessage());
		assertEquals(cause, exception.getCause());
	}

}

/**
 * 
 */
package com.yoyag.api;

import javax.servlet.ServletException;
import org.apache.log4j.Logger;

/**
 * @author ben
 *
 */
public class NLPMedicalParser implements Parser {
	private static final Logger LOGGER = Logger.getLogger(NLPMedicalParser.class);
	public NLPMedicalParser() throws ServletException {
	}
	/* (non-Javadoc)
	 * @see com.yoyag.api.Parser#parseInput(com.yoyag.api.Input)
	 */
	@Override
	public void parseInput(Input in) {
	}
	
}

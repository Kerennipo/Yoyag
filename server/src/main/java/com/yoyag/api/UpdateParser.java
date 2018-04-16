/**
 * 
 */
package com.yoyag.api;

/**
 * @author ben
 *
 */
public class UpdateParser implements Parser {

	/* (non-Javadoc)
	 * @see com.yoyag.api.Parser#parseInput(com.yoyag.api.Input)
	 */
	@Override
	public void parseInput(Input in) {
		UpdateInput uIn = (UpdateInput)in;
		System.out.println("DEBUG: got update request");
	}
}

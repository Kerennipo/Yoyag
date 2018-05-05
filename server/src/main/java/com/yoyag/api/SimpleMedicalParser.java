/**
 * 
 */
package com.yoyag.api;

import java.util.Map;

/**
 * @author Ben Surkiss
 *
 */
public class SimpleMedicalParser implements Parser {

	/* (non-Javadoc)
	 * @see api.Parser#parseInput(api.Input)
	 */
	@Override
	public void parseInput(Input in) {
		NewInput nIn = (NewInput)in;
		FreetextOutput out = new FreetextOutput();
		out.setContent(generateContentFromInput(nIn));
		out.setUserID(nIn.getUserID());
		out.setTimestamp(nIn.getTimestamp());
		new MedicalSummaryHandler().handle(out);
	}
	/*
	 * 
	 * @param in
	 * @return
	 */
	private String generateContentFromInput(NewInput in) {
		Map<String, Object> data = in.getData();
		StringBuilder content = new StringBuilder();
		content.append("The user complained at " + in.getTimestamp() + " about the following: ");
		int counter = 1;
		int size = data.entrySet().size();
		for (String key : data.keySet()) {
			key = (String)key;
			if (key == "bodyTempreture")
				content.append(data.get(key).toString() + " temperature");
			else
				content.append(key);
			if (counter < size)
				content.append(", ");
			else
				content.append(".");
			counter++;
		}
		return content.toString();
	}
}

/**
 * 
 */
package api;

import java.util.Map;

/**
 * @author Ben Surkiss
 *
 */
public class MedicalParser implements Parser {

	/* (non-Javadoc)
	 * @see api.Parser#parseInput(api.Input)
	 */
	@Override
	public void parseInput(Input in) {
		Output out = new Output();
		out.setContent(generateContentFromInput(in));
		out.setUserID(in.getUserID());
		out.setTimestamp(in.getTimestamp());
		new MedicalSummaryHandler().handle(out);
	}
	/*
	 * 
	 * @param in
	 * @return
	 */
	private String generateContentFromInput(Input in) {
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

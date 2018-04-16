/**
 * 
 */
package com.yoyag.api;

import org.springframework.web.client.RestTemplate;

/**
 * @author Ben Surkiss
 *
 */
public class MedicalSummaryHandler implements SummaryHandler {

	/* (non-Javadoc)
	 * @see api.SummaryHandler#handle(api.Output)
	 */
	@Override
	public void handle(Output out) {
		String uri = getUri(out);
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.postForObject(uri, out, String.class);
		System.out.println(result);
		//System.out.println("Will post to " + uri);
	}
	
	private String getUri(Output out) {
		return "http://db.cs.colman.ac.il/yoyag/Doctor/getSummary";
	}
}

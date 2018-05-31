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
		System.out.println("Abour to send out='" + out.toString() + "' to uri='" + uri + "'");
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.postForObject(uri, out, String.class);
//		System.out.println(result);
	}
	
	private String getUri(Output out) {
		return "http://localhost:2115/Doctor/getSummary";
	}
}

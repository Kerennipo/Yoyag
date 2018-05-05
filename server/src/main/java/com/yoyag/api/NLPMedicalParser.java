/**
 * 
 */
package com.yoyag.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.TOP;
import org.xml.sax.SAXException;

import com.yoyag.api.ctakes.Pipeline;

/**
 * @author ben
 *
 */
public class NLPMedicalParser implements Parser {
	
	private static final Logger LOGGER = Logger.getLogger(NLPMedicalParser.class);
	static AnalysisEngine pipeline;
	
	public NLPMedicalParser() throws ServletException {
		LOGGER.debug("Initilizing NLPMedicalParser");
		AggregateBuilder aggregateBuilder;
		try {
			aggregateBuilder = Pipeline.getAggregateBuilder();
			pipeline = aggregateBuilder.createAggregate();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	/* (non-Javadoc)
	 * @see com.yoyag.api.Parser#parseInput(com.yoyag.api.Input)
	 */
	@Override
	public void parseInput(Input in) {
		NewInput input;
		try {
			input = (NewInput)in;
		} catch (Exception e) {
			LOGGER.fatal("Wrong input to NLPMedicalParser");
			return;
		}
		try {
			/*
			 * Set the document text to process And run the cTAKES pipeline
			 */
			FreetextOutput out = new FreetextOutput();
			String text = (String)input.getData().get("text");
			JCas jcas = pipeline.newJCas();
			jcas.setDocumentText(text);
			pipeline.process(jcas);
			List<String> symptoms = formatResults(jcas);
			jcas.reset();
			String result = parseSymptoms(symptoms);
			out.setContent(result);
			out.setTimestamp(input.getTimestamp());
			out.setUserID(input.getUserID());
			new MedicalSummaryHandler().handle(out);
//			LOGGER.info(result);
			
		} catch (Exception e) {
			LOGGER.fatal(e);
			return;
		}
	}
	
	public List<String> formatResults(JCas jcas) throws SAXException, IOException {
		LOGGER.info("Starting to process results");
		List<String> symptoms = new ArrayList<String>();
		for (TOP annotation : JCasUtil.selectAll(jcas)) {
			extractFeatures(symptoms, (FeatureStructure)annotation);
		}
		symptoms = new ArrayList<>(new HashSet<String>(symptoms));
		return symptoms;
	}
	
//	public String formatResults(JCas jcas) throws SAXException, IOException { // JCAS to XML string
//		StringBuffer sb = new StringBuffer();
////		Collection<TOP> annotations = JCasUtil.selectAll(jcas);
//		ByteArrayOutputStream output = new ByteArrayOutputStream();
//		XmiCasSerializer.serialize(jcas.getCas(), output);
//		sb.append(output.toString());
//		output.close();
//		return sb.toString();
//	}
	
	private void extractFeatures(List<String> symptoms, FeatureStructure fs) {
		List<?> plist = fs.getType().getFeatures();
		for (Object obj : plist) {
			if (obj instanceof Feature) {
				Feature feature = (Feature) obj;
				String val = "";
				if (feature.getRange().isPrimitive()) {
					val = fs.getFeatureValueAsString(feature);
				} else if (feature.getRange().isArray()) {
					// Flatten the Arrays
					FeatureStructure featval = fs.getFeatureValue(feature);
					if (featval instanceof FSArray) {
						FSArray valarray = (FSArray) featval;
						for (int i = 0; i < valarray.size(); ++i) {
							FeatureStructure temp = valarray.get(i);
							extractFeatures(symptoms, temp);
						}
					}
				}
				if (feature.getName() != null
						&& val != null
						&& val.trim().length() > 0
						&& !"confidence".equalsIgnoreCase(feature
								.getShortName())) {
					if ("preferredText".equals(feature.getShortName())) {
						symptoms.add(val);
					}
				}
			}
		}
	}
	
	private String parseSymptoms(List<String> symptoms) {
		StringBuilder sb = new StringBuilder();
		sb.append("The patient reported the following symptoms:\n");
		int i;
		for (i = 0 ; i < symptoms.size() ; i++) {
			sb.append(symptoms.get(i));
			if (i < symptoms.size()-1)
				sb.append(", ");
			else
				sb.append(".");
		}
		return sb.toString();
	}
}



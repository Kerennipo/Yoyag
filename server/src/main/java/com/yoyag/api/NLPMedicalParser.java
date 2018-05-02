/**
 * 
 */
package com.yoyag.api;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.jcas.JCas;
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
			String result = formatResults(jcas);
			jcas.reset();
			out.setContent(result);
			out.setTimestamp(input.getTimestamp());
			out.setUserID(input.getUserID());
//			new MedicalSummaryHandler().handle(out);
			LOGGER.info(result);
		} catch (Exception e) {
			LOGGER.fatal(e.getMessage());
			return;
		}
	}
	
	public String formatResults(JCas jcas) throws SAXException, IOException {
		StringBuffer sb = new StringBuffer();
//		Collection<TOP> annotations = JCasUtil.selectAll(jcas);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		XmiCasSerializer.serialize(jcas.getCas(), output);
		sb.append(output.toString());
		output.close();
		return sb.toString();
	}
	
}

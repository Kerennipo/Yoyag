/**
 * 
 */
package com.yoyag.api;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.jcas.JCas;

import com.ctakes.nlp.web.client.servlet.Pipeline;
import com.ctakes.nlp.web.client.servlet.XMLParserNew;

/**
 * @author ben
 *
 */
public class NLPMedicalParser implements Parser {
	private static final Logger LOGGER = Logger.getLogger(NLPMedicalParser.class);
	private static AnalysisEngine analysisEngine;
	public NLPMedicalParser() throws ServletException {
		AggregateBuilder aggregateBuilder;
        try {
            aggregateBuilder = Pipeline.getAggregateBuilder();
            analysisEngine = aggregateBuilder.createAggregate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
	}
	/* (non-Javadoc)
	 * @see com.yoyag.api.Parser#parseInput(com.yoyag.api.Input)
	 */
	@Override
	public void parseInput(Input in) {
		// TODO Auto-generated method stub
		Map<String,List<String>>  resultMap = null;
		Map<String, Object> data = in.getData();
		String analysisText = data.get("analysisText").toString();
        LOGGER.info("###\n" + analysisText + "###\n");
        if (analysisText != null && analysisText.trim().length() > 0) {
            try {
                JCas jcas = analysisEngine.newJCas();
                jcas.setDocumentText(analysisText);
                analysisEngine.process(jcas);
                resultMap = formatResultsNew(jcas);
                jcas.reset();
            } catch (Exception e) {
                e.printStackTrace();
//                throw new ServletException(e);
            }
        }
        NLPOutput out = new NLPOutput();
        out.setContent(resultMap);
        out.setTimeStamp(in.getTimestamp());
        out.setUserID(in.getUserID());
        new MedicalSummaryHandler().handle(out);
	}
	
	private Map<String,List<String>>  formatResultsNew(JCas jcas) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XmiCasSerializer.serialize(jcas.getCas(), output);
        String outputStr = output.toString();
        Files.write(Paths.get("Result.xml"), outputStr.getBytes());
        XMLParserNew parser = new XMLParserNew();
        return parser.parse(new ByteArrayInputStream(outputStr.getBytes()));
    }

}

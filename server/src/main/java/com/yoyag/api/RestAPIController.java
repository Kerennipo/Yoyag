/**
 * 
 */
package com.yoyag.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Ben Surkiss
 *
 */
@RestController
public class RestAPIController {
	private static final Logger LOGGER = Logger.getLogger(RestAPIController.class);
	private IPHandler ipHandler;
	private static final Map<String, Parser> parsersFactory = new HashMap<>(); 
    
    @PostConstruct
    public void init() throws ServletException, IOException {
        LOGGER.info("Initializing API controller");
        ipHandler = new IPHandler();
        parsersFactory.put("NLP", new NLPMedicalParser());
        parsersFactory.put("Simple", new SimpleMedicalParser());
    }
    
	@RequestMapping(value = "/post", method = RequestMethod.POST)
    public ResponseEntity<String> post(@RequestBody NewInput input, HttpServletRequest request) throws ServletException {
		String location = ipHandler.getLocation(request);
		input.getData().put("location", location);
		Parser p = getParserForInput(input);
		p.parseInput(input);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> update(@RequestBody UpdateInput input) {
		Parser updateParser = new UpdateParser();
		updateParser.parseInput(input);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = "/getStatistics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Integer>> getStatistics(StatisticsInput input) {
		Map<String, Integer> list = getStatiscsFromDB(input);
		return new ResponseEntity<Map<String, Integer>>(list, HttpStatus.OK);
	}
	
	private Map<String, Integer> getStatiscsFromDB(StatisticsInput input) {
		Map<String, Integer> map = new HashMap<>();
		map.put("Diabetes", new Integer(21));
		map.put("Back pain", new Integer(40));
		return map;
	}
	
	private Parser getParserForInput(NewInput input) throws ServletException {
//		return new SimpleMedicalParser();
		return parsersFactory.get("NLP");
	}
	
}

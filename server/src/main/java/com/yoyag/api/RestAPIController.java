/**
 * 
 */
package com.yoyag.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
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
    
    @PostConstruct
    public void init() throws ServletException {
        LOGGER.info("Initializing API controller");
    }
    
	@RequestMapping(value = "/post", method = RequestMethod.POST)
    public ResponseEntity<String> post(@RequestBody NewInput input, HttpServletRequest request) throws ServletException {
//		List<String> ips = new ArrayList<String>();
//		try {
//			ips.add(request.getRemoteAddr());
//			ips.add(request.getHeader("X-FORWARDED-FOR"));
//		} catch (Exception e) {
//			LOGGER.error("Got error while adding ips");
//			LOGGER.error(e);
//		}
//		try {
//			Object data = input.getData().get("data");
//			Map<String, Object> mapData;
//			mapData = (Map<String, Object>)data;
//			mapData.put("ips", ips);
//			LOGGER.info("IPs=" + ips);
//		} catch (Exception e) {
//			LOGGER.error("Could not add ips to input object");
//			LOGGER.error(e);
//		}
		Parser p = getParserForInput(input);
		p.parseInput(input);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
	
	private Parser getParserForInput(NewInput input) throws ServletException {
//		return new SimpleMedicalParser();
		return new NLPMedicalParser();
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> update(@RequestBody UpdateInput input) {
		Parser updateParser = new UpdateParser();
		updateParser.parseInput(input);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
}

/**
 * 
 */
package com.yoyag.api;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.maxmind.geoip2.DatabaseReader;

/**
 * @author ben
 *
 */
public class IPHandler {
	private DatabaseReader reader;

	public IPHandler() throws IOException {
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		// A File object pointing to your GeoIP2 or GeoLite2 database
		File database = new File("GeoLite2-City.mmdb");
		

		// This creates the DatabaseReader object. To improve performance, reuse
		// the object across lookups. The object is thread-safe.
		this.reader = new DatabaseReader.Builder(database).build();
	}

	public IPHandler(String DBpath) throws IOException {
		// A File object pointing to your GeoIP2 or GeoLite2 database
		File database = new File(DBpath);

		// This creates the DatabaseReader object. To improve performance, reuse
		// the object across lookups. The object is thread-safe.
		this.reader = new DatabaseReader.Builder(database).build();
	}

	private String getIPForRequest(HttpServletRequest request) {
		return request.getRemoteAddr();
		// String ipAddress = request.getHeader("X-FORWARDED-FOR");
	}

	public String getLocation(HttpServletRequest request) {
		String ip = getIPForRequest(request);
		System.out.println("Got the following IP='" + ip + "'");
		return "TA";
	}
}

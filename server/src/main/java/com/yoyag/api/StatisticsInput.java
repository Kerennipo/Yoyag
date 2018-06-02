/**
 * 
 */
package com.yoyag.api;

/**
 * @author ben
 *
 */
public class StatisticsInput implements Input {
	private String userID;
	private String sessionID;

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}
	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
}

/**
 * 
 */
package com.yoyag.api;

/**
 * @author ben
 *
 */
public class UpdateInput implements Input {
	private String token;
	private String userID;
	private String diagnosis;
	private String sessionID;
	
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
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
	/**
	 * @return the diagnosis
	 */
	public String getDiagnosis() {
		return diagnosis;
	}
	/**
	 * @return the sessionID
	 */
	public String getSessionID() {
		return sessionID;
	}
	/**
	 * @param sessionID the sessionID to set
	 */
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	/**
	 * @param diagnosis the diagnosis to set
	 */
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
}

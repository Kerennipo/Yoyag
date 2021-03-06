/**
 * 
 */
package com.yoyag.api;

/**
 * @author Ben Surkiss
 *
 */
public class FreetextOutput implements Output {
	private String timeStamp;
	private String content;
	private String userID;
	private String sessionID;
	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timeStamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timeStamp = timestamp;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FreetextOutput [timeStamp=" + timeStamp + ", content=" + content + ", userID=" + userID + "]";
	}
}

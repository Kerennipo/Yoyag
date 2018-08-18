/**
 * 
 */
package com.yoyag.api;
/**
 * @author ben
 *
 */
public class NLPOutput implements Output {
	private String timeStamp;
	private Object content;
	private String userID;
	/**
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	/**
	 * @return the content
	 */
	public Object getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(Object content) {
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NLPOutput [timeStamp=" + timeStamp + ", content=" + content + ", userID=" + userID + "]";
	}

}

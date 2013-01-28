package com.mango.tagtweets;


public class StreamObject {
	
	private String mContent;
	private String mAuthor;
	private String mTimeStamp;
	
	public StreamObject (String content, String author, String timeStamp) {
		super();
		
		mContent = content;
		mAuthor = author;
		mTimeStamp = timeStamp;
	}
	
	public String getContent () {
		return mContent;
	}
	
	public String getAuthor () {
		return mAuthor;
	}
	
	public String getTimeStamp () {
		return mTimeStamp;
	}
	
}

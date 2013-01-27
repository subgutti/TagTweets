package com.mango.tagtweets;


public class StreamObject {
	
	private String mContent;
	private String mAuthor;
	
	public StreamObject (String content, String author) {
		super();
		
		mContent = content;
		mAuthor = author;
	}
	
	public String getContent () {
		return mContent;
	}
	
	public String getAuthor () {
		return mAuthor;
	}
}

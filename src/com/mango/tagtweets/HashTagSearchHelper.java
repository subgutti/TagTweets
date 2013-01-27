package com.mango.tagtweets;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class HashTagSearchHelper {

	private static final String BASE_URL = "http://search.twitter.com/search.json?page=";
	private static final String BASE_SEARCH_STRING = "&q=%23";

	private static final String DEFAULT_HASH_TAG = "GenericTweet";
	
	private static final String PREF_FILE = "hashtag_discover";
	private static final String PREF_HASHTAG = "hashtag";
	
	public static String getAbsoluteUrl(Context context, int pageNum) {
		return BASE_URL + pageNum + BASE_SEARCH_STRING + getHashTag(context);
	}
	
	public static void clearHashTag (Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_HASHTAG, DEFAULT_HASH_TAG);
        editor.commit();
	}
	
	public static String getHashTag (Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, Activity.MODE_PRIVATE);
		String str = prefs.getString(PREF_HASHTAG, DEFAULT_HASH_TAG);
		str = str.replaceAll("\\s","");
		return str;
	}
	
	public static void setHashTag (Context context, String hash) {
		SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        hash = hash.replaceAll("\\s","");
        editor.putString(PREF_HASHTAG, hash);
        editor.commit();
	}
	
	//Valid hashtags -
	//1. all characters [a-zA-Z_]+
	//2. characters and numbers mixed [a-zA-Z0-9_]+
	//ignore only numbers
	public static boolean checkHashTagValidity (Context context, String hash) {
		if((hash.matches("\\w*") && !hash.matches("[0-9_]+"))) {
			return true;
		}
		return false;
	}
}

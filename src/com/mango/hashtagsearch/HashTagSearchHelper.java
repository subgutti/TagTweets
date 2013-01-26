package com.mango.hashtagsearch;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class HashTagSearchHelper {

	private static final String BASE_URL = "http://search.twitter.com/search.json?q=%23";

	private static final String DEFAULT_HASH_TAG = "waystogetoffthephone";
	
	private static final String PREF_FILE = "hashtag_discover";
	private static final String PREF_HASHTAG = "hashtag";
	
	public static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
	
	public static String getAbsoluteUrl(Context context) {
		return BASE_URL + getHashTag(context);
	}
	
	public static String getHashTag (Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, Activity.MODE_PRIVATE);
		return prefs.getString(PREF_HASHTAG, DEFAULT_HASH_TAG);
	}
	
	public static void setHashTag (Context context, String hash) {
		SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PREF_HASHTAG, hash);
        editor.commit();
	}
}

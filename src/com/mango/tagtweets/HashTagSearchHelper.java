package com.mango.tagtweets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
	
	
	private static String TWITTER_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";
	public static String getFormattedTime (Context context, String dateStr) {
		SimpleDateFormat twitterFormat = new SimpleDateFormat(TWITTER_FORMAT, Locale.ENGLISH);
		twitterFormat.setLenient(true);
		Date streamPostDate;
		
		try {
			streamPostDate = twitterFormat.parse(dateStr);
		} catch (ParseException e) {
			streamPostDate = new Date();
		}
		
		Date currentDate = new Date();

		long diff = currentDate.getTime() - streamPostDate.getTime();
		if(diff < 0)
			diff = 0;

		int diffMinutes = (int) (diff / (60 * 1000) % 60);
		int diffHours = (int) (diff / (60 * 60 * 1000) % 24);
		int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
		
		String timeStr;
		if(diffDays > 100)
			timeStr = context.getString(R.string.time_long_back);
		else if(diffDays > 0)
			timeStr = context.getResources().getQuantityString(R.plurals.time_days, diffDays, diffDays);
		else if(diffHours > 0)
			timeStr = context.getResources().getQuantityString(R.plurals.time_hours, diffHours, diffHours);
		else if(diffMinutes > 0)
			timeStr = context.getResources().getQuantityString(R.plurals.time_minutes, diffMinutes, diffMinutes);
		else 
			timeStr = context.getString(R.string.time_moments_ago, diffDays);
		
	    return timeStr;
	}
}

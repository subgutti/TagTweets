package com.mango.hashtagsearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

public class TwitterResponseParser {
	
	private static final String TAG = TwitterResponseParser.class.getSimpleName();
	
	private static final boolean DEBUG = false;
	
private static ArrayList <StreamObject> mStreamObjects = new ArrayList<StreamObject>();
	
	public static ArrayList<StreamObject> parse (JSONArray dataArray) {
		
		if(dataArray == null)
			return mStreamObjects;
		
		//data not equal to null
		mStreamObjects.clear();
		
		int length = dataArray.length();
		
		for(int i = 0; i < length ; i ++) {
			try {
				
				JSONObject data = dataArray.getJSONObject(i);
				
				String content = "";
				String author = "";
				
				if(data != null) {
					//Get content
					try {
						content = data.getString("text");
					} catch (JSONException e) {
						content = "";
						if(DEBUG) Log.d(TAG,"no content available");
					}
					
					//Get author
					try {
						author = data.getString("from_user_name");
					} catch (JSONException e) {
						author = "";
						if(DEBUG) Log.d(TAG,"no author available");
					}
					
					if(!TextUtils.isEmpty(author) && !TextUtils.isEmpty(content))
						mStreamObjects.add(new StreamObject(content, author));
				}
				
				if(DEBUG) Log.d(TAG,"response : content - " + content + " , author - " + author);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return mStreamObjects;
	}
}

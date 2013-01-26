package com.mango.hashtagsearch;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StreamAdapter extends BaseAdapter {
	
	private static final String TAG = StreamAdapter.class.getSimpleName();
	
	private String[] mBackgroundColors = new String[] {"#ffffffff","#ffcccccc"};
	private int mBackgroundColorsLength = mBackgroundColors.length;
	
	private ArrayList<StreamObject> mStreamObjects;
	private Context mContext;
	private viewHolder holder;
	
	public StreamAdapter (Context context, List<StreamObject> objects) {
		mContext = context;
		mStreamObjects = (ArrayList<StreamObject>) objects;
	}
	
	@Override
	public int getCount() {
		return mStreamObjects != null ? mStreamObjects.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mStreamObjects != null ? mStreamObjects.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return 0; //TODO : handle this ? 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null) {
			view = View.inflate(mContext, R.layout.stream_item, null);
		
			holder = new viewHolder();
			view.setTag(holder);
			holder.setAuthor((TextView) view.findViewById(R.id.signature));
			holder.setContentView((TextView) view.findViewById(R.id.content));
		} else {
			holder = (viewHolder)view.getTag();
		}
		
		//Use alternate background colors
		int colorPos = position % mBackgroundColorsLength;
	    view.setBackgroundColor(Color.parseColor(mBackgroundColors[colorPos]));
	    
		final StreamObject object = mStreamObjects.get(position);
		
		TextView authorView = holder.getAuthorView();
		TextView contentView = holder.getContentView();
		
		String author = object.getAuthor();
		if(TextUtils.isEmpty(author))
			author = mContext.getString(R.string.unknown);
		
		authorView.setText(author);
		contentView.setText(object.getContent());
		
		return view;
	}
	
	private class viewHolder{
    	private TextView authorView;
    	private TextView contentView;
    	
		public TextView getAuthorView() {
			return authorView;
		}
		public void setAuthor(TextView name) {
			this.authorView = name;
		}
		public TextView getContentView() {
			return contentView;
		}
		public void setContentView(TextView content) {
			this.contentView = content;
		}
	}
}

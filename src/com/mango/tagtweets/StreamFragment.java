package com.mango.tagtweets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class StreamFragment extends ListFragment {
	
	private static final String TAG = StreamFragment.class.getSimpleName();
	private static final boolean DEBUG = false;
	
	private StreamAdapter mAdapter;
	private ArrayList<StreamObject> mStreamObjects = new ArrayList<StreamObject>();
	
	private int mCurrentPage = 1;
	private int mCurrentPos = 0;
	private boolean mLoadingData = false;
	
	private static final int MSG_INITIAL_LOAD = 0;
	private static final int MSG_LOAD_NEXT_PAGE = 1;
	private Handler mHandler = new Handler () {
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			
			switch(what) {
			case MSG_INITIAL_LOAD :
				checkAndDownloadContent();
				break;
			case MSG_LOAD_NEXT_PAGE :
				new RequestTask(getActivity()).execute(HashTagSearchHelper.getAbsoluteUrl(getActivity(), mCurrentPage));
	        	break;
			default:
				break;
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_stream_list, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		checkAndDownloadContent();
	}

	@Override
	public void onResume() {
		super.onResume();

		getListView().setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				mCurrentPos = firstVisibleItem;
				if(DEBUG) Log.d(TAG,"counts : first " + firstVisibleItem + " visible " + visibleItemCount + " totla " + totalItemCount);
				if(!mLoadingData && !mHandler.hasMessages(MSG_LOAD_NEXT_PAGE)) {
					if(visibleItemCount < totalItemCount && firstVisibleItem == totalItemCount - visibleItemCount) {
						mHandler.removeMessages(MSG_LOAD_NEXT_PAGE);
						mHandler.sendEmptyMessage(MSG_LOAD_NEXT_PAGE);
					}
				}
			}
		});
	}
	
	@Override
	public void onPause() {
		getListView().setOnScrollListener(null);
		super.onPause();
	}

	public void checkAndDownloadContent () {
		//reset variables
		mStreamObjects.clear();
		mCurrentPage = 1;
		mCurrentPos = 0;
		
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
        	//load content from base url
        	new RequestTask(getActivity()).execute(HashTagSearchHelper.getAbsoluteUrl(getActivity(), mCurrentPage));
        } else {
        	((MainSearchActivity) getActivity()).showErrorDialog(ErrorDialogFragment.ERROR_NO_NETWORK);
        }
	}
	
	/**
	 * this class performs all the work, shows dialog before the work and dismiss it after
	 */
	public class RequestTask extends AsyncTask<String, Void, String> {

	    private ProgressDialog dialog;
	    private Context mContext;
	    
	    public RequestTask(Context context) {
	    	mContext = context;
	    	
	    	if(mCurrentPage == 1)
	    		dialog = new ProgressDialog(context);
	    }

	    protected void onPreExecute() {
	    	if(dialog != null) {
	    		this.dialog.setMessage(mContext.getResources().getString(R.string.progress));
	    		this.dialog.show();
	    	}
	    	mLoadingData = true;
	    }
	    
	    @Override
	    protected void onPostExecute(final String response) {
	        if (dialog != null && dialog.isShowing()) {
	            dialog.dismiss();
	        }
	        
	        mLoadingData = false;
	        
	        if(TextUtils.isEmpty(response)) {
	        	mAdapter = null;
	        	setListAdapter(mAdapter);
	        	return;
	        }
	        
	        JSONObject responseObject;
			JSONArray dataArray;
			
			try {
				responseObject = new JSONObject(response);
				if(DEBUG) Log.d(TAG,"JSON OBJECT : " + response);
				dataArray = responseObject.getJSONArray("results");
				ArrayList<StreamObject> objects = TwitterResponseParser.parse(getActivity(), dataArray);
				
				mStreamObjects.addAll(objects);
				
				mAdapter = new StreamAdapter(getActivity(), mStreamObjects);
				setListAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				
				if(mAdapter.getCount() > mCurrentPos)
					getListView().setSelectionFromTop(mCurrentPos, 0);
				else
					getListView().setSelectionFromTop(0, 0);
					
				mCurrentPage++;
				
			} catch (JSONException e) {
				e.printStackTrace();
				//TODO: unable to load and quit dialog
			}
	    }

	    protected String doInBackground(final String... uri) {
	    	HttpClient httpclient = new DefaultHttpClient();
	        HttpResponse response;
	        String responseString = null;
	        try {
	            response = httpclient.execute(new HttpGet(uri[0]));
	            StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
	        } catch (ClientProtocolException e) {
	            //TODO  unable to load and quit dialog
	        } catch (IOException e) {
	            //TODO  unable to load and quit dialog
	        } catch (Exception e) {
	        	HashTagSearchHelper.clearHashTag(getActivity());
	        	mHandler.sendEmptyMessage(MSG_INITIAL_LOAD);
	        }
	        return responseString;
	    }
	}
	
}

package com.mango.hashtagsearch;

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
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StreamFragment extends ListFragment {

	private StreamAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_stream_list, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		
		checkAndDownloadContent();
	}

	public void checkAndDownloadContent () {
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
        	//load content from web
        	new RequestTask(getActivity()).execute(HashTagSearchHelper.getAbsoluteUrl(getActivity()));
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
	        dialog = new ProgressDialog(context);
	    }

	    protected void onPreExecute() {
	        this.dialog.setMessage(mContext.getResources().getString(R.string.progress));
	        this.dialog.show();
	    }

	        @Override
	    protected void onPostExecute(final String response) {
	        if (dialog.isShowing()) {
	            dialog.dismiss();
	        }
	        
	        if(TextUtils.isEmpty(response)) {
	        	mAdapter = null;
	        	setListAdapter(mAdapter);
	        	return;
	        }
	        
	        JSONObject responseObject;
			JSONArray dataArray;
			
			try {
				responseObject = new JSONObject(response);
				dataArray = responseObject.getJSONArray("results");
				ArrayList<StreamObject> objects = TwitterResponseParser.parse(dataArray);
			
				mAdapter = new StreamAdapter(getActivity(), objects);
				setListAdapter(mAdapter);
				
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
	        }
	        return responseString;
	    }
	}
	
}

package com.mango.hashtagsearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.mango.hashtagsearch.EditDialogFragment.EditNameDialogListener;

public class MainSearchActivity extends FragmentActivity implements EditNameDialogListener{
	
	private static final String TAG = MainSearchActivity.class.getSimpleName();
	
	private static final String FRAGMENT_EDIT_SEARCH = "fragment_edit_search";
	private static final String FRAGMENT_ERROR = "fragment_error";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_search);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_search, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_search:
			showEditSearchDialog();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void showEditSearchDialog () {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		Fragment old = fm.findFragmentByTag(FRAGMENT_EDIT_SEARCH);
		if(old != null) {
			ft.remove(old);
		}
	    ft.addToBackStack(null);

	    EditDialogFragment editNameDialog = new EditDialogFragment();
        editNameDialog.show(ft, FRAGMENT_EDIT_SEARCH);
	}

	public void showErrorDialog (int type) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		Fragment old = fm.findFragmentByTag(FRAGMENT_ERROR);
		if(old != null) {
			ft.remove(old);
		}
	    ft.addToBackStack(null);

	    ErrorDialogFragment errorDialog = new ErrorDialogFragment(type);
	    errorDialog.show(ft, FRAGMENT_ERROR);
	}
	
	@Override
	public void onFinishEditDialog(String inputText) {
		HashTagSearchHelper.setHashTag(this, inputText);

		//Reload stream as search tag changed
		FragmentManager fm = getSupportFragmentManager();
		StreamFragment fragment = (StreamFragment) fm.findFragmentByTag("stream_fragment");
		fragment.checkAndDownloadContent();
	}
}

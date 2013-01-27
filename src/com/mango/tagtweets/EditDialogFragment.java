package com.mango.tagtweets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditDialogFragment extends DialogFragment {
	
	public interface EditNameDialogListener {
        void onFinishEditDialog(EditDialogFragment fragment, String inputText);
    }

    private EditText mEditText;
    private boolean mAlertDialogShown;
    private AlertDialog mAlertDialog;
    
    public EditDialogFragment () {
        // Empty constructor required for DialogFragment
    }
    
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        
        builder.setTitle(R.string.search_edit_dialog_title);
        
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.fragment_edit_dialog, null);
        mEditText = (EditText) view.findViewById(R.id.input_field);
        String tag = HashTagSearchHelper.getHashTag(getActivity());
        mEditText.setText(tag);
        mEditText.setSelection(tag.length());
        
        builder.setView(view);
        // Set action buttons
        builder.setPositiveButton(R.string.ok, null);
        builder.setNegativeButton(R.string.cancel, null);
        
        mAlertDialog = builder.create();
        
        mAlertDialog.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				if(!mAlertDialogShown) {
					Button button = mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// Return input text to activity
			        		EditNameDialogListener activity = (EditNameDialogListener) getActivity();
			        		activity.onFinishEditDialog(EditDialogFragment.this, mEditText.getText().toString());
						}
					});
				}
				mAlertDialogShown = true;
			}
		});
        
        return mAlertDialog;
	}	
}

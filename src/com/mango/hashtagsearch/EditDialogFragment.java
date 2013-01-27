package com.mango.hashtagsearch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class EditDialogFragment extends DialogFragment {
	
	public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

    private EditText mEditText;

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
        
        builder.setView(view)
        // Set action buttons
               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                	   // Return input text to activity
                       EditNameDialogListener activity = (EditNameDialogListener) getActivity();
                       activity.onFinishEditDialog(mEditText.getText().toString());
                   }
               })
               .setNegativeButton(R.string.cancel, null);   
        
        return builder.create();
	}
}

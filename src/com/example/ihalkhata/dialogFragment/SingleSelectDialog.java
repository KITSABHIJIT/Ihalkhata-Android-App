package com.example.ihalkhata.dialogFragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SingleSelectDialog extends DialogFragment {

	EditText editText;
	TextView hidden;
	int dialogTitle;
	List<Integer> mSelectedItems;
	CharSequence[] dataList;
	CharSequence[] dataListHidden;

	public SingleSelectDialog(EditText eText, TextView hText, CharSequence[] charList, CharSequence[] charListHidden, int title) {
		this.editText = eText;
		this.hidden = hText;
		this.dataList = charList;
		this.dataListHidden = charListHidden;
		this.dialogTitle = title;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mSelectedItems = new ArrayList<Integer>(); // Where we track the
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Set the dialog title
		builder.setTitle(dialogTitle).setSingleChoiceItems(dataList, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						ListView list = ((AlertDialog) dialog).getListView();

						StringBuilder sb = new StringBuilder();
						StringBuilder sbHidden = new StringBuilder();
						for (int i = 0; i < list.getCount(); i++) {
							boolean checked = list.isItemChecked(i);
							if (checked) {
								if (sb.length() > 0)
									sb.append(",");
								sb.append(list.getItemAtPosition(i));
								
								if (sbHidden.length() > 0)
									sbHidden.append(",");
								sbHidden.append(dataListHidden[i]);

							}
						}
						editText.setText(sb.toString());
						hidden.setText(sbHidden.toString());
						dialog.dismiss();
					}
				});
		return builder.create();
	}
}

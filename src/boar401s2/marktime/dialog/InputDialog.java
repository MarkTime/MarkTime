package boar401s2.marktime.dialog;

import boar401s2.marktime.events.InputDialogParent;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

public class InputDialog {
	
	AlertDialog.Builder dialog;
	InputDialogParent parent;
	Integer requestID;
	Activity activityParent;

	public InputDialog(Activity activityParent, final InputDialogParent parent, String title, String message, final Integer requestID){
		this.parent = parent;
		this.activityParent = activityParent;
		this.requestID = requestID;
		dialog = new AlertDialog.Builder(activityParent);
		dialog.setTitle(title);
		dialog.setMessage(message);
		//dialog.setCancelable(false);

		// Set an EditText view to get user input 
		final EditText input = new EditText(activityParent.getApplicationContext());
		dialog.setView(input);

		dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  	parent.onDialogReturn(input.getText().toString(), requestID);
		  }
		});

		dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    parent.onDialogCancelled();
		  }
		});

	}
	
	public void show(){
		dialog.show();
	}

}

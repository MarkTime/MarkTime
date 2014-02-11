package boar401s2.marktime.dialog;

import boar401s2.marktime.MarkTime;
import boar401s2.marktime.events.InputDialogParent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

public class InputDialog {
	
	AlertDialog.Builder dialog;
	InputDialogParent parent;

	public InputDialog(final InputDialogParent parent, String title, String message){
		this.parent = parent;
		dialog = new AlertDialog.Builder(MarkTime.app);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setCancelable(false);

		// Set an EditText view to get user input 
		final EditText input = new EditText(MarkTime.activity.getApplicationContext());
		dialog.setView(input);

		dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  	parent.onDialogReturn(input.getText().toString());
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

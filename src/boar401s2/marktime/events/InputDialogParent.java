package boar401s2.marktime.events;

public interface InputDialogParent {

	public void onDialogReturn(String result, Integer requestID);
	public void onDialogCancelled();
	
}

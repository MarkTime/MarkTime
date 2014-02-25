package boar401s2.marktime.events;

/**
 * Interface for the parent of an AsyncTask.
 * This provides the AsyncTask several callback
 * options to the parent.
 * @author boar401s2
 */

public interface AsyncTaskParent {
	
	public void onStatusChange(String status);
	public void openProgressDialog(String message);
	public void closeProgressDialog();
	public void onPostExecute(Integer taskid, Integer result);
	public void onPreExecute();

}

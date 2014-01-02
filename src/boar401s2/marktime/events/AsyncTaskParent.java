package boar401s2.marktime.events;

public interface AsyncTaskParent {
	
	public void onStatusChange(String status);
	public void openProgressDialog(String message);
	public void closeProgressDialog();
	public void onPostExecute(Integer taskid, Integer result);
	public void onPreExecute();

}

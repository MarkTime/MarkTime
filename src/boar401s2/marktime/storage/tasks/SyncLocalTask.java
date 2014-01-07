package boar401s2.marktime.storage.tasks;

import android.os.AsyncTask;
import boar401s2.marktime.events.AsyncTaskParent;


public class SyncLocalTask {
	
	AsyncTaskParent parent;
	
	public SyncLocalTask(AsyncTaskParent parent){
		this.parent = parent;
	}
	
	public void run(){
		new SyncLocal().execute();
	}
	
	/**
	 * Task that gets the oauth2 token for a user
	 */
	class SyncLocal extends AsyncTask<Void, String, Integer> {	
		boolean doneTask = false;
		
		@Override
		protected void onPreExecute(){
			parent.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			return ResultIDList.RESULT_OK;
		}
		
		@Override
		protected void onPostExecute(Integer result){
			if (result!=ResultIDList.RESULT_NO_RETURN){
				parent.onPostExecute(TaskIDList.TASK_SYNC_LOCAL, result);
			}
		}
		
		@Override
		public void onProgressUpdate(String... text){
			parent.onStatusChange(text[0]);
		}
	}

}

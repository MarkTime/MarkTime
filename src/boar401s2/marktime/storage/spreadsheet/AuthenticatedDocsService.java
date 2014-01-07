package boar401s2.marktime.storage.spreadsheet;

import java.net.URL;

import android.app.Activity;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.storage.tasks.GetDocsFeed;
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.docs.DocumentListFeed;

/**
 * Used to autenticated services. At the moment it only authenticates
 * spreadsheet services.
 */
public class AuthenticatedDocsService implements AsyncTaskParent{
	
	DocsService service;
	URL feedUri; 
	DocumentListFeed feed;
	AsyncTaskParent parent;
	GetDocsFeed task;
	Activity activityParent;
	String token;
	
	public AuthenticatedDocsService(AsyncTaskParent parent, Activity activityParent){
		this.parent = parent;
		this.activityParent = activityParent;
	}
	
	public void authenticate(String token){
		task = new GetDocsFeed(this, activityParent);
		task.run(token);
	}
	
	public DocsService getDocsService(){
		return task.getService();
	}
	
	public URL getFeedURL(){
		return task.getFeedURL();
	}
	
	public DocumentListFeed getDocsFeed(){
		return task.getFeed();
	}

	@Override
	public void onStatusChange(String status) {
		parent.onStatusChange(status);
	}

	@Override
	public void onPostExecute(Integer taskid, Integer result) {
		parent.onPostExecute(taskid, result);
	}

	@Override
	public void openProgressDialog(String message) {}

	@Override
	public void closeProgressDialog() {}

	@Override
	public void onPreExecute() {}
}
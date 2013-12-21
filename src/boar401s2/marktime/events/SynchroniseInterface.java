package boar401s2.marktime.events;

/**
 * Part of the "Event system"
 * 
 * Used by several classes to update status messages,
 * and provide callbacks
 */
public interface SynchroniseInterface extends AsyncTaskParent{

	public void onConnected();
	public void onSquadFetched();
	
}

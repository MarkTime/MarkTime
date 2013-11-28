package boar401s2.marktime.interfaces;

/**
 * Part of the "Event system"
 * 
 * Used by several classes to update status messages,
 * and provide callbacks
 */
public interface SynchroniseInterface {

	public void onStatusChange(String status);
	public void onConnected();
	public void onSquadFetched();
	
}

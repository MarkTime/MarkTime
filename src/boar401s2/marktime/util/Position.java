package boar401s2.marktime.util;

public class Position{
	public int x = 0;
	public int y = 0;
	
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public String getString(){
		return String.valueOf(x)+", "+String.valueOf(y);
	}
}
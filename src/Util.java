
public class Util {
	public static int Manhattan (int x1,int y1, int x2,int y2){
		int res=Math.abs(x2-x1)+Math.abs(y2-y1);
		return res;
	}
	
	public static Direction randomDir(int x){
		if(x == 0){
			return Direction.Nord;
		}
		if(x == 1){
			return Direction.Est;
		}
		if(x == 2){
			return Direction.Sud;
		}
		else{
			return Direction.Ouest;
		}
		
	}
	public static Direction getDirection(String d) {
		d = d.replace(" ", "");
		if (d.equals("Sud"))
			return Direction.Sud;
		if (d.equals("Nord"))
			return Direction.Nord;
		if (d.equals("Ouest"))
			return Direction.Ouest;
		if (d.equals("Est"))
			return Direction.Est;
		return null;
	}
	
}

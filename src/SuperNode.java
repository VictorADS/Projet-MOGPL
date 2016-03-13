
public abstract class SuperNode {
	protected int x;	//Coordone ligne dans le tableau
	protected int y; //Coordone colonee dans le tableau
	protected int no;
	protected int id;

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}

	public SuperNode(int x, int y,int id){
		this.x=x;
		this.y=y;
		this.id=id;
	}
	public abstract boolean IsWall();
}

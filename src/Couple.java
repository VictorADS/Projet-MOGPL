
public class Couple {
	private int id;
	private Direction deplacement;
	public Couple(int id ,Direction d){
		this.id=id;
		this.deplacement=d;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Direction getDeplacement() {
		return deplacement;
	}
	public void setDeplacement(Direction deplacement) {
		this.deplacement = deplacement;
	}
}

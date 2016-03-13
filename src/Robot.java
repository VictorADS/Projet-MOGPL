import java.util.ArrayList;

public class Robot {
	private int x;
	private int y;
	private Direction direction;
	private SuperNode n1, n2, n3;
	private SuperNode s1, s2, s3;
	private SuperNode o1, o2, o3;
	private SuperNode e1, e2, e3;

	public Robot(int x, int y, Direction d) {
		this.x = x;
		this.y = y;
		this.direction = d;
		n1 = n2 = n3 = s1 = s2 = s3 = o1 = o2 = o3 = e1 = e2 = e3 = null;
	}

	public int TimeTravel(SuperNode n) {
		int res = 1 + NbRotation(n);
		return res;
	}

	public void ResetFils() {
		n1 = n2 = n3 = s1 = s2 = s3 = o1 = o2 = o3 = e1 = e2 = e3 = null;
	}

	public int NbRotation(SuperNode n) {
		int res = 0;
		switch (direction) {
		case Nord:
			if (x - n.getX() < 0)
				res = 2;
			else {
				if (x - n.getX() > 0) {
					res = 0;
				} else
					res = 1;
			}
			break;
		case Ouest:
			if (y - n.getY() < 0)
				res = 2;
			else {
				if (y - n.getY() > 0) {
					res = 0;
				} else
					res = 1;
			}
			break;

		case Est:
			if (y - n.getY() < 0)
				res = 0;
			else {
				if (y - n.getY() > 0) {
					res = 2;
				} else
					res = 1;
			}
			break;

		case Sud:
			if (x - n.getX() < 0)
				res = 0;
			else {
				if (x - n.getX() > 0) {
					res = 2;
				} else
					res = 1;
			}
			break;
		}
		return res;
	}
public void SonarRobot(SuperNode[][] graphe,int m , int n) {
		int i = getX();
		int j = getY();

		if (i - 1 >= 0 && graphe[i - 1][j].getNo() == 0) { // Create Fils Nord
			setN1(graphe[i - 1][j]);
			if (i - 2 >= 0 && graphe[i - 2][j].getNo() == 0) {
				setN2(graphe[i - 2][j]);
				if (i - 3 >= 0 && graphe[i - 3][j].getNo() == 0)
					setN3(graphe[i - 3][j]);
			}
		}
		if (i + 1 < m && graphe[i + 1][j].getNo() == 0) { // Create Fils Sud
			setS1(graphe[i + 1][j]);
			if (i + 2 < m && graphe[i + 2][j].getNo() == 0) {
				setS2(graphe[i + 2][j]);
				if (i + 3 < m && graphe[i + 3][j].getNo() == 0)
					setS3(graphe[i + 3][j]);
			}
		}
		if (j + 1 < n && graphe[i][j + 1].getNo() == 0) { // Create Fils Est
			setE1(graphe[i][j + 1]);
			if (j + 2 < n && graphe[i][j + 2].getNo() == 0) {
				setE2(graphe[i][j + 2]);
				if (j + 3 < n && graphe[i][j + 3].getNo() == 0)
					setE3(graphe[i][j + 3]);
			}
		}
		if (j - 1 >= 0 && graphe[i][j - 1].getNo() == 0) { // Create Fils OEst
			setO1(graphe[i][j - 1]);
			if (j - 2 >= 0 && graphe[i][j - 2].getNo() == 0) {
				setO2(graphe[i][j - 2]);
				if (j - 3 >= 0 && graphe[i][j - 3].getNo() == 0)
					setO3(graphe[i][j - 3]);
			}
		}
	}
	public ArrayList<SuperNode> getFils() {
		ArrayList<SuperNode> nc = new ArrayList<SuperNode>();
		if (o1 != null)
			nc.add(o1);
		if (o2 != null)
			nc.add(o2);
		if (o3 != null)
			nc.add(o3);
		if (s1 != null)
			nc.add(s1);
		if (s2 != null)
			nc.add(s2);
		if (s3 != null)
			nc.add(s3);
		if (e1 != null)
			nc.add(e1);
		if (e2 != null)
			nc.add(e2);
		if (e3 != null)
			nc.add(e3);
		if (n1 != null)
			nc.add(n1);
		if (n2 != null)
			nc.add(n2);
		if (n3 != null)
			nc.add(n3);
		return nc;

	}

	public SuperNode getN1() {
		return n1;
	}

	public void setN1(SuperNode n1) {
		this.n1 = n1;
	}

	public SuperNode getN2() {
		return n2;
	}

	public void setN2(SuperNode n2) {
		this.n2 = n2;
	}

	public SuperNode getN3() {
		return n3;
	}

	public void setN3(SuperNode n3) {
		this.n3 = n3;
	}

	public SuperNode getS1() {
		return s1;
	}

	public void setS1(SuperNode s1) {
		this.s1 = s1;
	}

	public SuperNode getS2() {
		return s2;
	}

	public void setS2(SuperNode s2) {
		this.s2 = s2;
	}

	public SuperNode getS3() {
		return s3;
	}

	public void setS3(SuperNode s3) {
		this.s3 = s3;
	}

	public SuperNode getO1() {
		return o1;
	}

	public void setO1(SuperNode o1) {
		this.o1 = o1;
	}

	public SuperNode getO2() {
		return o2;
	}

	public void setO2(SuperNode o2) {
		this.o2 = o2;
	}

	public SuperNode getO3() {
		return o3;
	}

	public void setO3(SuperNode o3) {
		this.o3 = o3;
	}

	public SuperNode getE1() {
		return e1;
	}

	public void setE1(SuperNode e1) {
		this.e1 = e1;
	}

	public SuperNode getE2() {
		return e2;
	}

	public void setE2(SuperNode e2) {
		this.e2 = e2;
	}

	public SuperNode getE3() {
		return e3;
	}

	public void setE3(SuperNode e3) {
		this.e3 = e3;
	}

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

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void Avance(int n) {
		if (n >= 1 || n <= 3) {
			if (direction == Direction.Nord)
				x -= n;
			if (direction == Direction.Ouest)
				y -= n;
			if (direction == Direction.Sud)
				x += n;
			if (direction == Direction.Est)
				y += n;
		} else {
		}

	}

	public String findMoveToDo(Direction d) {
		String s = "G";
		switch (direction) {
		case Nord:
			if (d == Direction.Est) {
				s = "D";
			}
			if (d == Direction.Ouest) {
				s = "G";
			}
			break;
		case Ouest:
			if (d == Direction.Nord) {
				s = "D";
			}
			if (d == Direction.Sud) {
				s = "G";
			}
			break;
		case Sud:
			if (d == Direction.Ouest) {
				s = "D";
			}
			if (d == Direction.Est) {
				s = "G";
			}
			break;

		case Est:
			if (d == Direction.Sud) {
				s = "D";

			}
			if (d == Direction.Nord) {
				s = "G";
			}
			break;
		}
		return s;
	}

	public void Gauche() {
		switch (direction) {
		case Nord:
			direction = Direction.Ouest;
			break;
		case Ouest:
			direction = Direction.Sud;
			break;
		case Sud:
			direction = Direction.Est;
			break;
		case Est:
			direction = Direction.Nord;
			break;
		}
	}

	public void Droite() {
		switch (direction) {
		case Nord:
			direction = Direction.Est;
			break;
		case Ouest:
			direction = Direction.Nord;
			break;
		case Sud:
			direction = Direction.Ouest;
			break;
		case Est:
			direction = Direction.Sud;
			break;
		}
	}
}

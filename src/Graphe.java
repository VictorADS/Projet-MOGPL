import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

public class Graphe {
	private int m;
	private int n;
	private SuperNode[][] graphe;
	private Robot me;
	private SuperNode fin;
	private SuperNode deb;
	private Direction debdir;
	private static int cpt = 0;
	private  int cptF = 0;
	private static int id=0;
	public ArrayList<Double> listTime = new ArrayList<>();
	public ArrayList<Integer> listTailleGrille = new ArrayList<>();
	public ArrayList<Integer> listWall = new ArrayList<>();

	String styleSheet = "node {" + " fill-color: black;" + " size: 10px;"
			+ " stroke-mode: plain;" + " stroke-color: black;"
			+ " stroke-width: 1px; z-index:0;" + "}" +

			"node.deb {" + "fill-color:red;" + "}" + "node.sol {"
			+ "fill-color:orange;" + "}" + "node.fin {" + "fill-color:green;"
			+ "}" + "node.wall {" + "fill-color:blue;" + "}" + "edge.sol{"
			+ "size:2px;" + "fill-color:orange;}";

	public Graphe(int m, int n) {
		this.m = m;
		this.n = n;
		graphe = new SuperNode[m][n];
	}
	public Graphe(int m, int n,int id) {
		this.m = m;
		this.n = n;
		graphe = new SuperNode[m][n];
		cptF=id;
	}

	public int getCptF() {
		return cptF;
	}
	public void setCptF(int cptF) {
		this.cptF = cptF;
	}

	public int getM() {
		return m;
	}

	public int getN() {
		return n;
	}

	public static void InitialiserGraphe(File f) {
		int m=0;
		int n=0;
		try {
			Scanner nc = new Scanner(f);
			do {
				m = nc.nextInt() + 1;
				n = nc.nextInt() + 1;
				if (m == 1 || n == 1)
					break;
				Graphe g=new Graphe(m, n, id++);
				for (int i = 0; i < m; i++) {
					for (int j = 0; j < n; j++) {
						if (i < m - 1 && j < n - 1) {
							int res = nc.nextInt();
							if (res == 0)
								g.setGraphe(i, j,new NodeCase(i, j, cpt++));
							else {
								g.setGraphe(i, j,new NodeWall(i, j, cpt++));
							}
						} else
							g.setGraphe(i, j,new NodeCase(i, j, cpt++));
					}
				}
				g.UpdateGraphe(); // Maj Noeud inacessible a cote de mur
				int d1 = nc.nextInt();
				int d2 = nc.nextInt();
				int f1 = nc.nextInt();
				int f2 = nc.nextInt();
				g.setFin(f1, f2);

				String d = nc.nextLine();
				g.setDeb(d1,d2,Util.getDirection(d));
				// Calculer SOlution
				long debut = System.currentTimeMillis();
				g.CalculerSolution();
				System.out.println(System.currentTimeMillis() - debut);
				// Fin solution
				cpt = 0;
			} while (m > 0 && n > 0);
			nc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<SuperNode> CalculerSolution() {
		// Liste des noeuds a visiter
		MinHeap toVisit = new MinHeap(n * m);
		// Stocke lid du pere ainsi que la direction du robot
		Couple[] prec = new Couple[n * m];
		// En tab[0][i] distance reelle de deb a i
		// En tab[1][i] heuristique entre i et fin
		int[] distance = new int[n * m];
		// -1 = non visite , 0 = visite , 1 = marque
		int[] marque = new int[n * m];
		for (int i = 0; i < n * m; i++) {
			if (deb.getId() == i) {
				prec[i] = new Couple(i, me.getDirection());
				distance[i] = 0;
				marque[i] = 1;
			} else {
				prec[i] = new Couple(Integer.MAX_VALUE, Direction.Nord);
				distance[i] = Integer.MAX_VALUE;
				marque[i] = -1;
			}
		}
		toVisit.insert(deb, distance[deb.getId()]);// +
														// distance[1][deb.getId()]);
		// Boucle Dijkstra
		while (!toVisit.isEmpty()) {
			SuperNode min = toVisit.delete();
			if (min.getX() == fin.getX() && min.getY() == fin.getY()) {
				break;
			}

			marque[min.getId()] = 1;
			// On place le robot a lendroit du noeud pour connaitre les fils
			me.setDirection(prec[min.getId()].getDeplacement());
			me.setX(min.getX());
			me.setY(min.getY());
			me.ResetFils();
			me.SonarRobot(graphe, m, n);
			// On collecte les fils
			ArrayList<SuperNode> minVoisins = me.getFils();
			for (SuperNode v : minVoisins) {
				int distV = me.TimeTravel(v);
				Direction toV = getDirection(me, v);
				if (marque[v.getId()] == 0) { // Comparer la distance
					if (distance[v.getId()] > distance[min.getId()]
							+ distV) {
						distance[v.getId()] = distance[min.getId()]
								+ distV;
						prec[v.getId()].setId(min.getId());
						prec[v.getId()].setDeplacement(toV);
					}
				}
				if (marque[v.getId()] == -1) { // On ajoute le noeud dans les
												// noeuds visitable

					prec[v.getId()].setId(min.getId());
					prec[v.getId()].setDeplacement(toV);
					distance[v.getId()] = distV + distance[min.getId()];

					int dis = distance[v.getId()] ;
					toVisit.insert(v, dis);
					marque[v.getId()] = 0;
				}
			}
		}
		ArrayList<SuperNode> ns = RetraceCHemin(prec, distance);
		return ns;

	}

	// Retrace CHemin
	public ArrayList<SuperNode> RetraceCHemin(Couple[] prec, int[] distance) {
		ArrayList<SuperNode> ns = new ArrayList<SuperNode>();
		FileWriter fw=null;
		try {
			FileWriter fw2=new FileWriter("InstanceGraph"+cptF+".txt",true);
			fw2.write(m+" "+n+"\n");
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					if(graphe[i][j].IsWall())
						fw2.write("1 ");
					else
						fw2.write("0 ");
				}
				fw2.write("\n");
			}
			fw2.write(deb.getX()+" "+deb.getY()+" "+fin.getX()+" "+fin.getY()+" "+debdir+"\n");
			fw2.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (prec[fin.getId()].getId() == Integer.MAX_VALUE) {
			try {
				fw = new FileWriter("SolGraph"+cptF+".txt", true);
				fw.write("-1\n");
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			int finL = fin.getId();
			while (prec[finL].getId() != finL) {
				int i = finL / n;
				int j = finL % n;
				ns.add(graphe[i][j]);
				finL = prec[finL].getId();
			}
			try {
				 fw = new FileWriter("SolGraph"+cptF+".txt", true);
				Collections.reverse(ns);
				me.setX(deb.getX());
				me.setY(deb.getY());
				me.setDirection(debdir);
				fw.write(distance[fin.getId()] + " ");
				for (SuperNode n : ns) {
					Direction d = getDirection(me, n);
					while (me.getDirection() != d) {
						String s = me.findMoveToDo(d);
						if (s == "D")
							me.Droite();
						else
							me.Gauche();
						fw.write(s + " ");
					}
					int numberofjump = Util.Manhattan(me.getX(), me.getY(),
							n.getX(), n.getY());
					me.Avance(numberofjump);
					fw.write("a" + numberofjump + " ");
				}
				fw.write("\n");
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ns;
	}

	/* Retourne la direction du robot si il cetait deplace sur le noeud v */
	public Direction getDirection(Robot r, SuperNode v) {
		Direction d = null;
		if (r.getY() - v.getY() < 0)
			d = Direction.Est;
		if (r.getY() - v.getY() > 0)
			d = Direction.Ouest;
		if (r.getX() - v.getX() < 0)
			d = Direction.Sud;
		if (r.getX() - v.getX() > 0)
			d = Direction.Nord;
		return d;
	}

	public void UpdateGraphe() {
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (graphe[i][j].IsWall()) {
					UpdateWalls(i, j);
				}
			}
		}
	}

	public void UpdateWalls(int i, int j) {
		if (i + 1 < m) {
			graphe[i + 1][j].setNo(1);
			if (j + 1 < n) {
				graphe[i + 1][j + 1].setNo(1);
			}
		}
		if (j + 1 < n) {

			graphe[i][j + 1].setNo(1);
		}
	}



	public void setGraphe(int i, int j, SuperNode x) {
		graphe[i][j] = x;
	}

	public Boolean isRobot(int i, int j) {
		if (me.getX() == i && me.getY() == j)
			return true;
		else
			return false;
	}

	public void AfficheSolution(ArrayList<SuperNode> ns) {
		Graph g = new SingleGraph("MyGraphe");
		g.addAttribute("ui.stylesheet", styleSheet);
		for (int i = 0; i < graphe.length; i++) {
			for (int j = 0; j < graphe[0].length; j++) {
				g.addNode(i + "," + j);
				Node n = g.getNode(i + "," + j);
				n.addAttribute("xy", j, m - i);
				if (graphe[i][j].getNo() == 1)
					n.addAttribute("ui.class", "wall");

			}
		}
		for (int i = 0; i < graphe.length; i++) {
			for (int j = 0; j < graphe[0].length; j++) {
				if (i - 1 >= 0)
					g.addEdge(i + "," + (i - 1) + "-" + j + "," + j, i + ","
							+ j, (i - 1) + "," + j);
				if (j - 1 >= 0)
					g.addEdge(i + "," + i + "-" + j + "," + (j - 1), i + ","
							+ j, i + "," + (j - 1));
			}
		}
		SuperNode Father = deb;
		for (SuperNode n : ns) {
			Node node = g.getNode(n.getX() + "," + n.getY());
			node.addAttribute("ui.class", "sol");
			if (n.getX() == Father.getX()) {
				int cpt = Father.getY();
				while (cpt != n.getY()) {
					Edge e = null;
					if (n.getY() < Father.getY())
						e = g.getEdge(Father.getX() + "," + Father.getX() + "-"
								+ cpt + "," + (cpt-- - 1));
					else
						e = g.getEdge(Father.getX() + "," + Father.getX() + "-"
								+ (cpt + 1) + "," + cpt++);
					e.addAttribute("ui.class", "sol");

				}
			}
			if (n.getY() == Father.getY()) {
				int cpt = Father.getX();
				while (cpt != n.getX()) {
					Edge e = null;
					if (n.getX() < Father.getX())
						e = g.getEdge(cpt + "," + (cpt-- - 1) + "-"
								+ Father.getY() + "," + Father.getY());
					else
						e = g.getEdge((cpt + 1) + "," + cpt++ + "-"
								+ Father.getY() + "," + Father.getY());
					e.addAttribute("ui.class", "sol");

				}
			}
			Father = n;
		}
		Node node = g.getNode(deb.getX() + "," + deb.getY());
		node.addAttribute("ui.class", "deb");
		Node finn = g.getNode(fin.getX() + "," + fin.getY());
		finn.addAttribute("ui.class", "fin");

		g.addAttribute("ui.antialias");
		g.addAttribute("ui.quality");

		Viewer viewer = g.display(false);
		viewer.getDefaultView().setMaximumSize(new Dimension(1000, 1000));
		viewer.disableAutoLayout();

	}

	public SuperNode getFin() {
		return fin;
	}

	public void setFin(int i, int j) {
		this.fin = graphe[i][j];
	}

	public SuperNode getDeb() {
		return deb;
	}

	public void setDeb(int i, int j, Direction dir) {
		this.deb = graphe[i][j];
		if (me == null) {
			me = new Robot(deb.getX(), deb.getY(), dir);
		} else {
			this.me.setX(deb.getX());
			this.me.setY(deb.getY());
		}
		debdir=dir;

	}
	public void CleanGraphe(){
		for (int i = 0; i < m; i++) {
			for(int j=0;j<n;j++){
				if(graphe[i][j].IsWall()){
					graphe[i][j]=new NodeCase(i, j, graphe[i][j].getId());
					continue;
				}
				if(graphe[i][j].getNo()==1)
					graphe[i][j].setNo(0);
			}
		}
	}
	protected void RandomWalls(int nbWall) {
		int cpt=0;
		Random r=new Random();
		CleanGraphe();
		while(cpt<nbWall){
			int i=r.nextInt(m);
			int j=r.nextInt(n);
			if(graphe[i][j].getNo()==1 || (deb.getX()==i && deb.getY()==j) || (fin.getX()==i && fin.getY()==j) ){
				continue;
			}else{
				graphe[i][j]=new NodeWall(i, j, graphe[i][j].getId());
				UpdateWalls(i, j);
				cpt++;
			}
		}
	}
	
	
	public void randomDebFin(){
		Random random = new Random();
		int x=random.nextInt(m);
		int y=random.nextInt(n);
		while(graphe[x][y].getNo()==1){
			 x=random.nextInt(m);
			 y=random.nextInt(n);
		}
		setDeb(x, y, Util.randomDir(random.nextInt(3)));
		int x2=random.nextInt(m);
		int y2=random.nextInt(n);
		while(graphe[x2][y2].getNo()==1){
			 x2=random.nextInt(m);
			 y2=random.nextInt(n);
		}
		setFin(x2, y2);
	}
	
	public static Graphe creerGraphe(int n,int id){
		
		Graphe graphe = new Graphe(n,n,id);
		int cpt=0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				graphe.setGraphe(i, j, new NodeCase(i,j,cpt));
				cpt++;
			}
		}
		graphe.randomDebFin();
		return graphe;
	}
	
	
	public void genererRandomGrille(){
		double tempsmoyen=0;
		for (int k = 10; k <=50; k+=10) {
			Graphe graphe = creerGraphe(k,k);
			for (int i = 0; i < 10; i++) {				
				graphe.RandomWalls(k);
				double deb=System.currentTimeMillis();
				graphe.CalculerSolution();
				double fin=System.currentTimeMillis();
				tempsmoyen+=(fin-deb)/1000f;
				graphe.randomDebFin();
			}
			try {
				FileWriter fw2=new FileWriter("InstanceGraph"+k+".txt",true);
				fw2.write("0 0");
				fw2.close();
			}catch(IOException e){
				e.getMessage();
			}
			
			listTime.add(tempsmoyen/10);
			listTailleGrille.add(k);
			
			System.out.println("Pour k="+k+" temps moyen : "+tempsmoyen/10);
			tempsmoyen=0;
		}
	}
	
	public void genererRandomObstacle(){		
			Graphe graphe = creerGraphe(20,2010);
			double tempsmoyen=0;
			for (int i = 10; i <= 50; i+=10) {
				for(int j =0;j<10;j++){
					graphe.RandomWalls(i);
					double deb=System.currentTimeMillis();
					graphe.CalculerSolution();
					double fin=System.currentTimeMillis();
					tempsmoyen+=(fin-deb)/1000f;
					graphe.randomDebFin();
				}
			try {
				FileWriter fw2=new FileWriter("InstanceGraph"+(2000+i)+".txt",true);
				fw2.write("0 0");
				fw2.close();
			}catch(IOException e){
				e.getMessage();
			}
			
			
			listTime.add(tempsmoyen/10);
			listWall.add(i);
			
			System.out.println("Pour k="+i+" temps moyen : "+tempsmoyen/10);
			tempsmoyen=0;
			graphe.setCptF(2000+i+10);
		}
	}

	
	public ArrayList<Double> getListTime(){
		return listTime;
	}
	
	public ArrayList<Integer> getListTailleGrille(){
		return listTailleGrille;
	}
	public ArrayList<Integer> getListWall(){
		return listWall;
	}
	
	public SuperNode getNode(int i, int j) {
		return graphe[i][j];
	}

	public Robot getMe() {
		return me;
	}

	public void setMe(Robot me) {
		this.me = me;
	}
}

import static org.graphstream.ui.graphicGraph.GraphPosLengthUtils.nodePosition;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

public class MyAffichage extends JFrame {
	
	String styleSheet="node {"+
			   " fill-color: black;"+
			   " size: 10px;"+
			   " stroke-mode: plain;"+
			   " stroke-color: black;"+
			   " stroke-width: 1px; z-index:0;"+
			   "}"+

			   "node.deb {"+
			    "fill-color:red;"+
			    "}"+
			    "node.sol {"
			    + "fill-color:orange;"+
			    "}"+
				"node.fin {"+
				"fill-color:green;"+
				"}"+
			    "node.wall {"+
			     "fill-color:blue;"+
			    "}"+
			     "edge.sol{"
			     + "size:2px;"+
			     "fill-color:orange;}";
	private static final long serialVersionUID = 1L;
	private Graphe graphe;
	private Graph g;
    private ArrayList<SuperNode> ns=new ArrayList<SuperNode>();
	JComboBox<Direction> DirectionChooser=new JComboBox<Direction>();
    private boolean isDep=false;
    private boolean isArrive=false;
    private boolean isWall=false;
    
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MyAffichage() {
		JFrame f = new JFrame("GraphStream Window");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		g = new SingleGraph("MyGraphe");

		JPanel panel1 = new JPanel();
        Viewer viewer = new Viewer(g, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        ViewPanel view = viewer.addDefaultView(false);
		panel1.setLayout(new BorderLayout());
		panel1.add(view, BorderLayout.CENTER);
        
		JSlider slider= new JSlider(JSlider.HORIZONTAL,
                1, 50, 10);
		Hashtable labelTable = new Hashtable();
		labelTable.put( new Integer( 1 ), new JLabel("1") );
		labelTable.put( new Integer( 50), new JLabel("35") );
		slider.setLabelTable( labelTable );
		slider.setPaintLabels(true);
		
		JSlider slider2= new JSlider(JSlider.HORIZONTAL,
                0, 35, 10);
		Hashtable labelTable2 = new Hashtable();
		labelTable2.put( new Integer( 0 ), new JLabel("0") );
		labelTable2.put( new Integer( 35), new JLabel("35") );
		slider2.setLabelTable( labelTable2 );
		slider2.setPaintLabels(true);

		JLabel ecran=new JLabel("N=10");
		JLabel ecran2=new JLabel("Nombre de murs = 10");

		slider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
		    	  ecran.setText("N="+ slider.getValue());
		    	  updateGraphe(slider.getValue(),slider2.getValue(),(Direction)DirectionChooser.getSelectedItem());
		    	  AfficheGraph(ns);
		      }
		    });
		slider2.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent e) {
		    	  ecran2.setText("Nombre de murs ="+ slider2.getValue());
		    	  graphe.RandomWalls(slider2.getValue());
		    	  AfficheGraph(ns);
		      }
		    });

		
		JPanel N = new JPanel(); //Affiche N
	    N.setLayout(new BoxLayout(N , BoxLayout.LINE_AXIS));
		N.setSize(new Dimension(50,200));
		N.add(ecran);
		
		JPanel SliderN = new JPanel(); // Affiche Slider choisir N
	    SliderN.setLayout(new BoxLayout(SliderN , BoxLayout.LINE_AXIS));
		SliderN.add(slider);
		
		
		JPanel nbWall = new JPanel(); //Affiche N
	    nbWall.setLayout(new BoxLayout(nbWall , BoxLayout.LINE_AXIS));
		nbWall.add(ecran2);

		JPanel SliderWall= new JPanel(); // Affiche Slider choisir N
	    SliderWall.setLayout(new BoxLayout(SliderWall , BoxLayout.LINE_AXIS));
		SliderWall.add(slider2);
		
		JButton b1=new JButton("Depart");
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if(isArrive){
					isArrive=false;
				}
				if(isWall){
					isWall=false;
				}
				isDep=true;
			}
		});
		JButton b2=new JButton("Arrive");
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if(isDep){
					isDep=false;
				}
				if(isWall){
					isWall=false;
				}
				isArrive=true;
			}
		});
		JButton b3=new JButton("Mur");
		b3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if(isDep){
					isDep=false;
				}
				if(isArrive){
					isArrive=false;
				}
				isWall=true;
			}
		});
		JPanel ButtonContainer= new JPanel(); // Affiche Slider choisir N
	    ButtonContainer.setLayout(new BoxLayout(ButtonContainer , BoxLayout.LINE_AXIS));
		ButtonContainer.add(b1);
		ButtonContainer.add(b2);
		ButtonContainer.add(b3);

		
		JButton b4=new JButton("Resoudre");
		b4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				graphe.getMe().setDirection((Direction)DirectionChooser.getSelectedItem());
				double deb=System.currentTimeMillis();
				ns=graphe.CalculerSolution();
				System.out.println((System.currentTimeMillis()-deb)/1000f);
				if(ns.size()==0){
					JOptionPane jop1 = new JOptionPane();
					jop1.showMessageDialog(null, "Le robot n'a pas trouvé de chemin", "Information", JOptionPane.WARNING_MESSAGE);
				}else
				AfficheGraph(ns);
			}
		});
		JPanel ButtonContainer2= new JPanel(); // Affiche Slider choisir N
	    ButtonContainer2.setLayout(new BoxLayout(ButtonContainer2 , BoxLayout.LINE_AXIS));
	    ButtonContainer2.add(b4);
	    
	//    DirectionChooser.setLayout(new BoxLayout(DirectionChooser , BoxLayout.LINE_AXIS));
		DirectionChooser.addItem(Direction.Est);
		DirectionChooser.addItem(Direction.Nord);
		DirectionChooser.addItem(Direction.Ouest);
		DirectionChooser.addItem(Direction.Sud);
		DirectionChooser.setMaximumSize(new Dimension(200,30));
		DirectionChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(DirectionChooser.getSelectedItem());
				graphe.getMe().setDirection((Direction) DirectionChooser.getSelectedItem());
			}
		});
		
		JPanel panel4 = new JPanel(); // Contient tout pour la grille
	    panel4.setLayout(new BoxLayout(panel4 , BoxLayout.PAGE_AXIS));
	    panel4.add(N);
	    panel4.add(SliderN);
	    panel4.add(nbWall);
	    panel4.add(SliderWall);
	    panel4.add(ButtonContainer);
	    panel4.add(DirectionChooser);
	    panel4.add(ButtonContainer2);
	    
		JButton b5=new JButton("Resoudre un fichier");
		b5.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				JFileChooser choix = new JFileChooser();
				int retour=choix.showOpenDialog(null);
				if(retour==JFileChooser.APPROVE_OPTION){
					File f=new File(choix.getSelectedFile().getAbsolutePath());
					Graphe.InitialiserGraphe(f);
					JOptionPane jop1 = new JOptionPane();
					jop1.showMessageDialog(null, "Les fichiers ont étés générés dans votre répértoire.", "Information", JOptionPane.INFORMATION_MESSAGE);
				};
			}
		});
		JButton b6=new JButton("Resoudre random grille");
		b6.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				graphe.genererRandomGrille();
				JOptionPane jop1 = new JOptionPane();
				jop1.showMessageDialog(null, "Les fichiers ont étés générés dans votre répértoire.", "Information", JOptionPane.INFORMATION_MESSAGE);
				
				Chart chart = new Chart("Courbe en fonction de la taile de la grille");
				
				ArrayList<Double> listTime;
				ArrayList<Integer> listTaille;
				
				listTime = graphe.getListTime();
				listTaille = graphe.getListTailleGrille();
			
				for (int i = 0; i < listTime.size(); i++) {
					chart.createDataset(listTaille.get(i), listTime.get(i));
				}
				
				chart.afficheChart();
				
				listTime.clear();
				listTaille.clear();
				
				chart.setVisible(true);
				
				
				
			}
		});
		JButton b7=new JButton("Resoudre random obstacle");
		b7.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				graphe.genererRandomObstacle();
				JOptionPane jop1 = new JOptionPane();
				jop1.showMessageDialog(null, "Les fichiers ont étés générés dans votre répértoire.", "Information", JOptionPane.INFORMATION_MESSAGE);
				
				Chart chart = new Chart("Courbe en fonction des obstacles");
				
				ArrayList<Double> listTime;
				ArrayList<Integer> listWall;
				
				listTime = graphe.getListTime();
				listWall = graphe.getListWall();
			
				for (int i = 0; i < listTime.size(); i++) {
					chart.createDataset(listWall.get(i), listTime.get(i));
				}
				
				
				chart.afficheChart();
				
				listTime.clear();
				listWall.clear();
				
				chart.setVisible(true);
				
				
			}
		});


	    JPanel panel5=new JPanel(); //Contient tout pour les fichier / random
	    panel5.setLayout(new BoxLayout(panel5, BoxLayout.PAGE_AXIS));
	    panel5.add(b5);
	    panel5.add(b6);
	    panel5.add(b7);

	    JPanel panelContainerRight=new JPanel();
	    panelContainerRight.setLayout(new GridLayout(2,1));
	    panelContainerRight.add(panel4);
	    panelContainerRight.add(panel5);

		updateGraphe(10,10,(Direction)DirectionChooser.getSelectedItem());
		AfficheGraph(ns);
	    
	    f.add("Center", panel1);
		f.add("East", panelContainerRight);
		f.setMinimumSize(new Dimension(300, 300));
		f.setSize(1000, 1000);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		
        ViewerPipe fromViewer = viewer.newViewerPipe();
        Clicks c=new Clicks(this);
        fromViewer.addViewerListener(c);
        fromViewer.addSink(g);
        while(true)
        fromViewer.pump();
	}

	public  void receivedListener(String id){
		 Node node = g.getNode(id);
	        double pos[] = nodePosition(node);
	        int i=(int) (graphe.getM()-pos[1]);
	        int j=(int) pos[0];
	        System.out.println(i+" et "+j);
	        if(isArrive){
	        	graphe.setFin(i,j);
	        	isArrive=false;
	        }
	        if(isDep){
	        	graphe.setDeb(i, j,(Direction)DirectionChooser.getSelectedItem());
	        	isDep=false;
	        }
	        if(isWall){
	        	int idn=graphe.getNode(i, j).getId();
	        	graphe.setGraphe(i, j, new NodeWall(i, j,idn ));
	        	graphe.UpdateGraphe();
	        	isWall=false;
	        }
	        AfficheGraph(ns);
	        
	}
	public void updateGraphe(int m,int nbwall,Direction dir){
		graphe=new Graphe(m+1, m+1);
		int cpt=0;

		for(int i=0;i<m+1;i++){
			for(int j=0;j<m+1;j++){
				graphe.setGraphe(i, j, new NodeCase(i, j, cpt++));
			}
		}
		graphe.setDeb(0,0,(Direction)DirectionChooser.getSelectedItem());
		graphe.setFin(0,1);
	}

	public void AfficheGraph(ArrayList<SuperNode> ns) {
		g.clear();
		g.addAttribute("ui.stylesheet",styleSheet);
		for (int i = 0; i < graphe.getM(); i++) {
			for (int j = 0; j < graphe.getN(); j++) {
				g.addNode(i + "," + j);
				Node n = g.getNode(i + "," + j);
				n.addAttribute("xy", j, graphe.getM()- i);
				if (graphe.getNode(i, j).getNo() == 1)
					n.addAttribute("ui.class","wall");
			}
		}
		for (int i = 0; i < graphe.getM(); i++) {
			for (int j = 0; j < graphe.getN(); j++) {
				if (i - 1 >= 0)
					g.addEdge(i + "," + (i - 1) + "-" + j + "," + j, i + ","
							+ j, (i - 1) + "," + j);
				if (j - 1 >= 0)
					g.addEdge(i + "," + i + "-" + j + "," + (j - 1), i + ","
							+ j, i + "," + (j - 1));
			}
		}
		SuperNode Father = graphe.getDeb();
		for (SuperNode n : ns) {
			Node node = g.getNode(n.getX() + "," + n.getY());
			node.addAttribute("ui.class","sol");
			if(n.getX()==Father.getX() ){
				int cpt=Father.getY();
				while(cpt!=n.getY()){
					Edge e =null;
					if(n.getY()<Father.getY())
						e=g.getEdge(Father.getX()+","+Father.getX()+"-"+cpt+","+(cpt---1));
					else
						e=g.getEdge(Father.getX()+","+Father.getX()+"-"+(cpt+1)+","+cpt++);
					e.addAttribute("ui.class","sol");

				}
			}
			if(n.getY()==Father.getY() ){
				int cpt=Father.getX();
				while(cpt!=n.getX()){
					Edge e =null;
					if(n.getX()<Father.getX())
						e=g.getEdge(cpt+","+(cpt---1)+"-"+Father.getY()+","+Father.getY());
					else
						e=g.getEdge((cpt+1)+","+cpt+++"-"+Father.getY()+","+Father.getY());
						e.addAttribute("ui.class","sol");

				}
			}
			Father=n;
		}
		Node node = g.getNode(graphe.getDeb().getX() + "," + graphe.getDeb().getY());
		node.addAttribute("ui.class","deb");
		Node finn = g.getNode(graphe.getFin().getX() + "," + graphe.getFin().getY());
		finn.addAttribute("ui.class","fin");

		g.addAttribute("ui.antialias");
		g.addAttribute("ui.quality");
		ns.clear();

	}
}

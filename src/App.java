import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/*
 * App.java - class containing the main method and interface specifications.
 * @author Selvan Kumar
 * @version 1.0
 * 
 */
public class App extends JFrame implements Runnable{
	
	private static final long serialVersionUID = 1L;
	private boolean isprog  = false;
	private JPanel inputPnl;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem exportItem;
	private JMenuItem citItem;
	private Border inBdr;
	private Border outBdr;
	private FlowLayout fl;
	private JLabel alphaLbl;
	private JLabel betaLbl;
	private JLabel evaLbl;
	private JLabel colLbl;
	private JLabel tspLbl;
	private JLabel itrLbl;
	private JComboBox<Double> alphaBox;
	private JComboBox<Double> betaBox;
	private JComboBox<Double> evaBox;
	private JComboBox<Integer> colBox;
	private JComboBox<Integer> itrBox;
	private JButton genBtn;
	private JButton clrBtn; 
	private JTable table;
	private DefaultTableModel tabMod;
	private JTextField tspFld;
	private JButton tspBtn;
	private File tspFile;
	
	private static double alpha;
	private static double beta;
	private static double evapRate;
	private static int colonies;
	private static int iterations;
	private static TSP tsp;
	private static long runTime;
	private static double distance;
	static Object[] result = new Object[8];
	
	
	//Method to create GUI and functionalities of application.
	@Override
	public void run() {
       
		/*
		 * MenuBar configurations
		 */
		menuBar = new JMenuBar();
		fileMenu = new JMenu ("File");
		exportItem = new JMenuItem ("Export Results");
		citItem = new JMenuItem ("Show cities");
		fileMenu.add(exportItem);
		fileMenu.add(citItem);
		menuBar.add(fileMenu); 
		
		/*
		 * Input Panel configurations
		 */
		
		//Border and layout settings
		inBdr = BorderFactory.createTitledBorder("Parameters");
		outBdr = BorderFactory.createEmptyBorder(5,5,5,5);
		fl = new FlowLayout();
		fl.setAlignment(FlowLayout.CENTER);
		fl.setVgap(10);
		fl.setHgap(10);
		
		//Initialize input panel
		inputPnl = new JPanel();
		inputPnl.setBorder(BorderFactory.createCompoundBorder(outBdr, inBdr));
		inputPnl.setLayout(fl);
		
		//Initialize labels 
		alphaLbl = new JLabel("Alpha :");
		betaLbl = new JLabel("Beta :");
		evaLbl = new JLabel("Evaporation :");
		colLbl = new JLabel("Colonies :");
		tspLbl = new JLabel("TSP :");
		itrLbl = new JLabel("Iterations :");
		
		/*
		 * Features to capture user input
		 */
		tspFld = new JTextField(8);
		tspFld.setText("20.tsp");
		tspFld.setEditable(false);
		tspBtn = new JButton("File");
		
		alphaBox = new JComboBox<Double>();
		for(double i=1;i<=5;i++){
			alphaBox.addItem(i);
		}
		
		betaBox = new JComboBox<Double>();
		for(double i=1;i<=5;i++){
			betaBox.addItem(i);
		}
		
		evaBox = new JComboBox<Double>();
		double[] evapRates = {0.1,0.2,0.3,0.4,0.5, 0.6,0.7,0.8,0.9};
		for(double d : evapRates) {
			evaBox.addItem(d);
		}
		
		colBox = new JComboBox<Integer>();
		for(int i=1;i<=50;i++){
			colBox.addItem(i);
		}
		
		itrBox = new JComboBox<Integer>();
		for(int i=1;i<=100;i++){
			itrBox.addItem(i);
		}
		
		//Create Buttons
		genBtn = new JButton("Generate");
		clrBtn = new JButton("Clear");
		
		//Add features to input panel
		inputPnl.add(tspLbl);
		inputPnl.add(tspFld);
		inputPnl.add(tspBtn);
		inputPnl.add(alphaLbl);
		inputPnl.add(alphaBox);
		inputPnl.add(betaLbl);
		inputPnl.add(betaBox);
		inputPnl.add(evaLbl);
		inputPnl.add(evaBox);
		inputPnl.add(itrLbl);
		inputPnl.add(itrBox);
		inputPnl.add(colLbl);
		inputPnl.add(colBox);
		inputPnl.add(genBtn);
		inputPnl.add(clrBtn);
		
		/*
		 * Table configurations
		 */
		
		String[] clnName = {"Cities", "Alpha","Beta","Evaporation", "Iterations", "Colonies", "Runtime", "Distance"};
		table = new JTable() {
			private static final long serialVersionUID = 1L;
			//make table uneditable
			public boolean isCellEditable(int row, int column){  
		        return false;  
		      }  
		};
		
		tabMod = new DefaultTableModel();
		tabMod.setColumnIdentifiers(clnName);
		table.setModel(tabMod);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i=0; i<table.getColumnCount();i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
        
        table.setFillsViewportHeight(true);
		
      //Feature to delete table row
      	table.addMouseListener(new MouseAdapter() {
      		 public void mousePressed(MouseEvent mouseEvent) {
      		        JTable table =(JTable) mouseEvent.getSource();
      		        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
      		        	 int res = JOptionPane.showConfirmDialog(null, "Are you sure to delete this row?", "", JOptionPane.YES_NO_OPTION);
      				        switch (res) {
      				            case JOptionPane.YES_OPTION:
      				            	int p = table.getSelectedRow();
      				            	tabMod.removeRow(p);
      				            	JOptionPane.showMessageDialog(null, "Deleted Successfully");
      				            break;
      				           
      				            case JOptionPane.NO_OPTION:
      				            break;
      				        }
      		        }
      		    }
      	});
        
      	/*
      	 *Listeners
      	 *1.File button listener
      	 *2.Generate button listener
      	 *3.Clear button listener 
      	 */
      	
      	//1. File button listener
      	tspBtn.addActionListener(e->{
      		
      		//function to select file
			JFileChooser tspFC = new JFileChooser();
			tspFC.setSelectedFile(new File( ".tsp"));
			int status = tspFC.showOpenDialog(tspFC);
			
			//store and track tsp file
			if (status == JFileChooser.APPROVE_OPTION) {                                                 
				tspFile = tspFC.getSelectedFile();
				tspFld.setText(tspFile.getName());
			}
      	});
      	
      	//2.Generate button listener
		genBtn.addActionListener(e->{
			//Default file
			if(tspFld.getText().equals("20.tsp")) {
				tsp = new TSP("src/"+tspFld.getText());
			}else {
				tsp = new TSP(tspFile.getAbsolutePath());
			}
			
			//Get parameter input from user
			alpha = (double) alphaBox.getSelectedItem();
			beta = (double) betaBox.getSelectedItem();
			evapRate =(double) evaBox.getSelectedItem();
			iterations = (int) itrBox.getSelectedItem();
			colonies = (int) colBox.getSelectedItem();
			
			//Initialize threads 
			List<Thread> threads = new ArrayList<Thread>();
			List<AntColony> colonyList = new ArrayList<AntColony>();
			
			//Start duration timining
			final long startTime = System.currentTimeMillis();
			
			//Partly adapted from: Jade J. (2018), "AntColOpt", github, [online at https://github.com/jkbestami/AntColOpt/blob/master/ACO.java, accessed 3 Feb 2020].
			for(int k = 0; k<colonies; k++){
				int numberOfAnts;
				
				//Set number of ants; if cities less than 50, number of cities = number of ants
				if(tsp.getnumCities()>50){
					numberOfAnts = 50;
				}else{
					numberOfAnts = tsp.getnumCities();
				}
				
				//Create colony and add to array
				AntColony col = new AntColony(alpha, beta+k, evapRate, numberOfAnts, tsp, iterations);
				colonyList.add(col);
				
				//Create thread and start thread
				Thread t = new Thread(col);
				threads.add(t);
				t.start();
			}
			
			try{
				for (Thread thread : threads) {
					thread.join();
				}
				
				double bestDistance = Double.MAX_VALUE;
				
				AntColony bestColony = new AntColony(0,0,0,1,tsp,1);
				//Optimize
				for(AntColony colony: colonyList){
					if(colony.OverallOptimalAnt.tourLength < bestDistance){
						bestDistance = colony.OverallOptimalAnt.tourLength;
						bestColony = colony;
					}
				}
				
				//Store best distance
				distance = bestDistance;
				
			}catch(Exception ex){
				System.err.println("thread problem");
			}
			//Obtain duration
			final long duration = System.currentTimeMillis() - startTime;
			runTime = duration;
			
			//Store results
			result[0] = tspFld.getText();
			result[1] = alpha;
			result[2] = beta;
			result[3] = evapRate;
			result[4] = iterations;
			result[5] = colonies;
			result[6] = runTime;
			result[7] = distance;
			
			//Append results to table
			tabMod.addRow(result);
		});
		
		//3.Clear button listener
		clrBtn.addActionListener(e->{
			//reset file 
			int res1 = JOptionPane.showConfirmDialog(null, "Reset file?", "", JOptionPane.YES_NO_OPTION);
			switch (res1) {
            	case JOptionPane.YES_OPTION:
            		tspFld.setText("20.tsp");
            	break;
           
            	case JOptionPane.NO_OPTION:
            		break;
			}
			
			//delete rows
			int res2 = JOptionPane.showConfirmDialog(null, "Delete table?", "", JOptionPane.YES_NO_OPTION);
	        switch (res2) {
	            case JOptionPane.YES_OPTION:
	            	 if (tabMod.getRowCount() > 0) {
	                     for (int i = tabMod.getRowCount() - 1; i > -1; i--) {
	                    	 tabMod.removeRow(i);
	                     }
	                 }
	            break;
	           
	            case JOptionPane.NO_OPTION:
	            break;
	        }
		});
      	
		/*
		 * Item Listeners
		 * 1.Export CSV
		 * 2.Show Cities
		 */
		
		//1. Export CSV
		exportItem.addActionListener(e->{
			//choose file
			JFileChooser expFC = new JFileChooser();
			expFC.setSelectedFile(new File( ".csv"));
			int status = expFC.showSaveDialog(expFC);

			try {   
				if (status == JFileChooser.APPROVE_OPTION) {                                                 
					/*
					 * Write table output to selected file
					 */
					File file = expFC.getSelectedFile();
					FileWriter csv = new FileWriter(file);
                     
					for(int i=0; i<table.getModel().getColumnCount();i++) {
					csv.write(table.getModel().getColumnName(i)+ ",");
					}
				
					csv.write("\n");
				
					for(int j=0; j< table.getModel().getRowCount(); j++) {
						for(int k=0; k < table.getModel().getColumnCount(); k++) {
							csv.write(table.getModel().getValueAt(j,k).toString()+",");
						}
		            
						csv.write("\n");
					}

					csv.close();
		        
					JOptionPane.showMessageDialog(null, "Exported Successfully");
				}
			}catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		
		//2.Show Cities
		citItem.addActionListener(e->{
			//Display sample cities list 
			JFrame citFrame = new JFrame();
			
			JTextArea ta = new JTextArea();
			ta.setEditable(false);
			File file = new File("src/238.tsp");
			
			try {
				BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				ta.read(input, "");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			citFrame.getContentPane().add(new JScrollPane(ta), BorderLayout.CENTER);
		    citFrame.pack();
		    citFrame.setVisible(true);
		});
		
		//Append input panel and table to main frame
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(inputPnl, BorderLayout.NORTH);
		this.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
		this.setJMenuBar(menuBar);
		this.setTitle("Parallel ACO");
		this.isResizable();
		
		//Show frame window
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		if (this.isprog) {
			this.setVisible(true);
		}
	}
	
	/*
	 * Method to invoke interface in run from EventQueue to prevent threading issues.
	 * @param isProg A variable of boolean type
	 */
	public App (boolean isProg){ 
		this.isprog = isProg;
	    
		try { EventQueue.invokeAndWait(this); 
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } 
	}

	/*
	 * Constructor method.
	 */
	public App (){ 
		this.isprog = false;
	    
		try { 
			EventQueue.invokeAndWait(this); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/*
	 * Main method.
	 * @param args
	 */
	public static void main (String [] args) {
		App app = new App(true);
	}
}
		

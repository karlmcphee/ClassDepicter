import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.Border;

/*
 * The DrawUML class displays an interface allowing a user to submit code, which will be analyzed.  An error
 * message is displayed if the code is invalid; otherwise, a graph of the code is produced on the right hand
 * panel.
 * 
 * @author Karl McPhee
 */
	public class DrawUML extends JPanel {
		
		private static JEditorPane txtFld;
		private static PlotPanel mainPanel;
		private static JGraphText tSummary;
		
	/*
	 * Starts the class displayer.
	 * @param args
	 */
	public static void main(String args[])  {
					DrawUML display = new DrawUML();
					if(args.length != 0) {
						try {
							Source source = new Source();
							String z = source.readTextFile(args[0]);
							txtFld.setText(z);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					JFrame centerDisplay = new JFrame();
					centerDisplay.add(display);
					JMenuBar menuBar = new JMenuBar();
					JMenu menu = new JMenu("File");
					menuBar.add(menu);
					JMenu runMenu = new JMenu("Run");
					menuBar.add(runMenu);
					JMenuItem addNew = new JMenuItem("Run"); //naming conventions
					runMenu.add(addNew);
			        addNew.addActionListener(new ActionListener() {
		
			            @Override
						public void actionPerformed(ActionEvent e) {
			        		tSummary.setForeground(Color.white);
			        		String z = txtFld.getText();
			            	Source source = new Source();
			            	source.addObserver(mainPanel);
			            	source.addObserver(tSummary);
			        		source.translate(z);
			            }});
					centerDisplay.setJMenuBar(menuBar);
					centerDisplay.setSize(1500,900);
				    centerDisplay.setLocationRelativeTo(null);  
				    centerDisplay.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
				    centerDisplay.setVisible(true);  
				}

	/*
	 * Constructor for the UI.
	 */
		public DrawUML() {
		    setLayout(new GridBagLayout());
	        mainPanel = new PlotPanel();
		    tSummary = new JGraphText();
		    tSummary.setBackground(Color.black);
		    tSummary.setForeground(Color.white);
		    txtFld = new JEditorPane();
		    Border border  = BorderFactory.createLineBorder(Color.black);
	    	txtFld.setBorder(border);
	    	GridBagConstraints gbc = new GridBagConstraints();
	    	gbc.gridx = 0;
	    	gbc.gridy = 0;
	    	gbc.weightx = .3;
	    	gbc.weighty = 1;
	    	gbc.gridwidth = 1;
	    	gbc.fill = (GridBagConstraints.BOTH);
	    	add(txtFld, gbc);
	    	gbc.gridx = 1;
	    	gbc.gridy = 0;
			gbc.weighty = 1;
			gbc.weightx = 300;
			gbc.gridwidth = 1;
	    	gbc.fill = GridBagConstraints.BOTH;
			add(mainPanel, gbc);
			mainPanel.setSize(500,500);
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = gbc.weighty = .30;
			gbc.gridx = 0;
			gbc.gridy = 1;
	    	gbc.gridwidth = 2;
	    	add(tSummary, gbc); 
			JMenuBar menuBar = new JMenuBar();
			JMenu menu = new JMenu("File");
			menuBar.add(menu);
			JMenu runMenu = new JMenu("Run");
			JMenuItem rMenuItem = new JMenuItem("Run");
			runMenu.add(rMenuItem);
			menuBar.add(runMenu);
		}
	}
	

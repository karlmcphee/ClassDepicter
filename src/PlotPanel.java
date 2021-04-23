import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * The PlotPanel class displays the diagram of the classes shown in the text, complete with function listings,
 * the presence of while/if loops, and aggregation or association relationships.
 */
public class PlotPanel extends JPanel implements Observer {
	
	private Vector<ClassData> nums;	
    private Vector<Integer> bottomCoord1;
    private Vector<Integer> bottomCoord2;
    private Vector<Integer> topCoord1;
    private Vector<Integer> topCoord2;
    private int counter;
	
	/*
	 * Constructor for the class.
	 */
	public PlotPanel() {
		nums = new Vector<ClassData>();
		bottomCoord1 = new Vector<Integer>();
		bottomCoord2 = new Vector<Integer>();
		topCoord1 = new Vector<Integer>();
		topCoord2 = new Vector<Integer>();
		
		
	} 
	
	/*
	 * Painting function for the class.
	 */
	protected void paintComponent(Graphics g) {
	      super.paintComponent(g);		  
	      g.setColor(Color.BLACK); 
    	  setBackground(Color.white);
	      //System.out.print("RESULTS " + Math.ceil((float)nums.size()/5));
	      for(int w = 0; w< Math.ceil((float)nums.size()/5); w++) {
	      for(int i = 0; i < Math.min(5, nums.size()-5*w); i++) {
	    	  g.setColor(Color.BLACK);
	    	  drawClass(g, i*120+120, 100+150*w, 80, 100, nums.get(i+5*w)); //at least 40
	  		  bottomCoord1.add(i*120+120+40);
			  bottomCoord2.add(100+w*150+100);
	  		  topCoord1.add(i*120+120+40);
			  topCoord2.add(100+w*150);
	      }
	      }
	      counter = 0;
	      System.out.print("REPAINTING \n");
	      for(int i = 0; i < nums.size(); i++) {
	    	  for(int j = 0; j < nums.get(i).getAssoclinks().size();j++ )
	    		  for(int k = 0; k < nums.size(); k++) {
	    			  if(nums.get(k).getcName().equals(nums.get(i).getAssoclinks().get(j))) {
	    				  g.setColor(Color.MAGENTA);
	    				  System.out.print("\nLinks between class " + nums.get(k).getcName() + " and class " + nums.get(i).getcName() + "\n");
	    				  g.drawLine(bottomCoord1.get(i)+6*counter, bottomCoord2.get(i), bottomCoord1.get(i)+6*counter, bottomCoord2.get(i)+20+6*counter);
	    				  g.drawLine(bottomCoord1.get(i)+6*counter, bottomCoord2.get(i)+ 20+6*counter, 80-6*counter, bottomCoord2.get(i)+ 20+6*counter);
	    				  g.drawLine(80-6*counter, bottomCoord2.get(i)+ 20+6*counter, 80-6*counter, 150*k%5+80-6*counter);
	    				  g.drawLine(80-6*counter, 150*k%5+80-6*counter, topCoord1.get(k)+6*counter, 150*k%5+80-6*counter);	  
	    				  drawArrow(g, topCoord1.get(k)+6*counter, 150*k%5+80-6*counter, topCoord1.get(k)+6*counter, topCoord2.get(k));
	    				  counter += 1;
	    			  }
	    		  }
	    	  for(int j = 0; j < nums.get(i).getAggrlinks().size();j++ )
	    		  for(int k = 0; k < nums.size(); k++) {
	    			  System.out.print("TESTER " + nums.get(i).getAggrlinks().get(j));
	    			  if(nums.get(k).getcName().equals(nums.get(i).getAggrlinks().get(j))) {
	    				  g.setColor(Color.BLUE);
	    				  System.out.print("WOOOOOOOOOO " + k + "\n");
	    				  System.out.print("\nLinks between class " + nums.get(k).getcName() + " and class " + nums.get(i).getcName() + "\n");
	       				  g.drawLine(bottomCoord1.get(i)+6*counter, bottomCoord2.get(i), bottomCoord1.get(i)+6*counter, bottomCoord2.get(i)+20+6*counter);
	    				  g.drawLine(bottomCoord1.get(i)+6*counter, bottomCoord2.get(i)+ 20+6*counter, 80-6*counter, bottomCoord2.get(i)+ 20+6*counter);
	    				  g.drawLine(80-6*counter, bottomCoord2.get(i)+ 20+6*counter, 80-6*counter, 150*k%5+80-6*counter);
	    				  g.drawLine(80-6*counter, 150*k%5+80-6*counter, topCoord1.get(k)+6*counter, 150*k%5+80-6*counter);	  
	    				  drawDiamond(g, topCoord1.get(k)+6*counter, 150*k%5+80-6*counter, topCoord1.get(k)+6*counter, topCoord2.get(k));
	    				  counter += 1;
	    			  }
	    		  }
	      }
	}
	
	/*
	 * Draws an individual class on the screen.
	 */
	private void drawClass(Graphics g, int x, int y, int x2, int y2, ClassData funcData) {
		g.setColor(Color.BLACK);
		g.drawRect(x, y, x2, y2);
		g.setColor(Color.ORANGE);
		g.fillRect(x, y, x2, y2);
		g.setColor(Color.BLACK);
		Font stringFont = new Font( "SansSerif", Font.PLAIN, 10 ); 
		g.setFont( stringFont );
		g.drawString(funcData.getcName(), x+5, y+10);
		g.setColor(Color.LIGHT_GRAY);
		for(int i = 0; i < funcData.getMethodType().size(); i++) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(x+10, y+20, x2-20, 10);
			if(funcData.getMethodType().get(i).iscWhile()) {
				g.setColor(Color.RED);
				g.fillOval(x+10, y+20, 10, 10);
			}
			if(funcData.getMethodType().get(i).iscIf()) {
				g.setColor(Color.GREEN);
				int xz[]={x+20,x+25,x+30};
				int yz[]={y+30,y+20,y+30};
				g.fillPolygon(xz,yz,3);
			}
			y+= 20;
		}
	}
	
	/*
	 * Draws a line with an arrow point.
	 * 
	 * @args graphics g, integer starting x-coordinate, integer starting y-coordinate, integer ending x-coordinate,
	 * integer ending y-coordinate.
	 */
	private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
		int d = 5;
		int h = 5;
	    int dx = x2 - x1, dy = y2 - y1;
	    double D = Math.sqrt(dx*dx + dy*dy);
	    double xm = D - d, xn = xm, ym = h, yn = -h, x;
	    double sin = dy / D, cos = dx / D;

	    x = xm*cos - ym*sin + x1;
	    ym = xm*sin + ym*cos + y1;
	    xm = x;

	    x = xn*cos - yn*sin + x1;
	    yn = xn*sin + yn*cos + y1;
	    xn = x;

	    int[] xpoints = {x2, (int) xm, (int) xn};
	    int[] ypoints = {y2, (int) ym, (int) yn};

	    g.drawLine(x1, y1, x2, y2);
	    g.drawLine(x2, y2, (int)xm, (int)ym);
	    g.drawLine(x2, y2, (int)xn, (int)yn);
	}
	
	/*
	 * Draws a diamond for aggregation relationships.
	 */
	private void drawDiamond(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2-10);
		g.drawLine(x2, y2-10, x2-5, y2-5);
		g.drawLine(x2, y2-10, x2+5, y2-5);
		g.drawLine(x2+5, y2-5, x2, y2);
		g.drawLine(x2-5, y2-5, x2, y2);
	}

     /*
      *Updates the painted panel with new values. 
      */
	@Override
	public void update(Observable o, Object arg1) {
		nums = ((Source)o).getNumbers();
		counter = 0;
		repaint();

		// TODO Auto-generated method stub
		
	}

}

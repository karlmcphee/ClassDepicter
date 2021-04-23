import java.awt.Color;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JTextArea;

/*
 * The JGraphText displays the textual results of parsing input text.  An error is shown if something
 * goes wrong.
 */
public class JGraphText extends JTextArea implements Observer {
	
	public JGraphText() {
		this.setText("Enter input to be translated");
	}


	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		Vector<ClassData> nums = ((Source)o).getNumbers();
		String Error = ((Source)o).getError();
		this.setText(Error);
		
	}

}

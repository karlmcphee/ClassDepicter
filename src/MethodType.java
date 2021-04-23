
/*
 * The methodType class corresponds to the methods read in by the parser.  Each method may contain at least one
 * while and if loop and will have a name.
 */

public class MethodType {
	
	private boolean cWhile;
	private boolean cIf;
	private String name;
	
	/*
	 * Creates a new method object.
	 */
	public MethodType(String n) {
		this.name = n;
		this.cWhile = false;
		this.cIf = false;	
	}
	
	/*
	 * Getter for the method's name.
	 */
	public String getName() {
		return name;
	}

	/*
	 * Setter for the method's name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * Checks to see if this method has a while loop.
	 */
	public boolean iscWhile() {
		return cWhile;
	}

	/*
	 * Changes the status of whether the method has a while loop.
	 */
	public void setcWhile(boolean cWhile) {
		this.cWhile = cWhile;
	}
	
	/*
	 * Checks to see if this method has an if loop.
	 */
	public boolean iscIf() {
		return cIf;
	}
	
	/*
	 * Sets the status of the method's if loop.
	 */
	public void setcIf(boolean cIf) {
		this.cIf = cIf;
	}
	
	

}

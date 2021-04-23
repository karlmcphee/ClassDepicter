import java.util.Vector;

/*
 * The ClassData class returns information about a read-in class.  Each class has a list of methods, a name,
 * and a list of associated classes through aggregation or association.
 */
public class ClassData {
	
	private Vector<MethodType> methodType;
	private String cName;
	private Vector<String> aggrlinks;
	private Vector<String> assoclinks;
	
	/*
	 * Creates a new class object and initializes its variable vectors.
	 */
	public ClassData(String name) {
		methodType = new Vector<MethodType>();
		aggrlinks = new Vector<String>();
		assoclinks = new Vector<String>();
		cName = name;
	}
	
	/*
	 * Returns the name of the class
	 */
	public String getcName() {
		return cName;
	}

	/*
	 * Sets the name of the class.
	 */
	public void setcName(String cName) {
		this.cName = cName;
	}


	/*
	 * Getter for the list of a class's methods.
	 */
	public Vector<MethodType> getMethodType() {
		return methodType;
	}
	
	/*
	 * Adds a method to the class.
	 */
	public void addMethod(MethodType methodType) {
		MethodType m = new MethodType(methodType.getName());
		m.setcWhile(methodType.iscWhile());
		m.setcIf(methodType.iscIf());
		this.methodType.addElement(m);
	}
	
	/*
	 * Sets the class's methods to the input list.
	 */
	public void setMethodType(Vector<MethodType> methodType) {
		this.methodType = methodType;
	}

	/*
	 * Getter for the class's aggregation.
	 */
	public Vector<String> getAggrlinks() {
		return aggrlinks;
	}

	/*
	 * Setter for the class's aggregation.
	 */
	public void setAggrlinks(String links) {
		this.aggrlinks.add(links);
	}

	/*
	 * Getter for the class's associations.
	 */
	public Vector<String> getAssoclinks() {
		return assoclinks;
	}

	/*
	 * Setter for the class's associations.
	 */
	public void setAssoclinks(String links) {
		this.assoclinks.add(links);
	}

}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Vector;

/*
 * The Source class performs operations for reading, parsing, and translating the data input by a user.
 */

public class Source extends Observable {
	
	
	private static int currentToken = 0;
	private static int vectSize = 0;
	private static Vector<String> wordList;
	private static String currWord;
	private static String descText;
	private static ClassData funcData;
	private static Vector<ClassData> methList;
	private static String errorM;
	private static MethodType methodType;
	private static Vector<String> cList;
	private static HashMap<String, String> methodInfo; 

/*
 * Constructor for the Source class.  This initializes the arrays and response strings.
 */
	public Source() {
		descText = "";
		methList = new Vector<ClassData>();
		errorM = "";
		cList = new Vector<String>();
		methodInfo = new HashMap<String, String>();
	}
	
	/*
	 * This calls the operations to split lines of code into words and then the following operations to translate
	 * and evaluate the accuracy of the input program lines.
	 * 
	 * @param text
	 */
    public void translate(String text) {
    	currentToken = 0;
    	cList.clear();
    	Vector<String> zz = splitWords(text);
    	do {
    		checkClass();
    	} while (currentToken < vectSize);
    }
    
    /*
     * Getter for descText, a string representation of the input program, with symbols standing for if, while, etc.
     */
    public String getDescText() {
    	return descText;
    }
    /*
     * Splits input text into lines of code.
     */
    private static Vector<String> splitWords(String text) {
    	wordList = new Vector<String>();
    	String line = "";
    	do {
    		int eolAt = text.indexOf("\r");
    		if (eolAt >= 0) {
    			line = text.substring(0, eolAt);
    			if (text.length() > 0) text = text.substring(eolAt+1);
    		} else {
    			line = text;
    			text = "";
    		}
    		splitLine(line);
    		
    	} while (!text.equals(""));
    	vectSize = wordList.size();
		//System.out.print(wordList);
    	return wordList;
    }
    
    /**
     * Splits a line taken as input to a vector of the words in the line, returned by the method.
     * @param line
     */
    private static void splitLine(String line) {
    	int n = 0;
    	String thisWord = "";
    	for(int i = 0; i < line.length(); i++) {
    		if(line.charAt(i)==' ' || line.charAt(i)=='\t' || line.charAt(i)=='(' || line.charAt(i)==')' || line.charAt(i)=='\n'
    				|| line.charAt(i)=='{' || line.charAt(i)=='}' || line.charAt(i)=='\r' || line.charAt(i)==';' ||
    						line.charAt(i)=='.'|| line.charAt(i)==','|| ((i+1)==line.length())) {
    			if(thisWord.equals("class")) n = 1;
    			if (!thisWord.equals("while") && !thisWord.equals("if") && !thisWord.equals("class")  && thisWord.length() > 0
    					&& !thisWord.equals("float") && !thisWord.equals("int")  && !thisWord.equals("integer") &&
    					!thisWord.equals("boolean") && !thisWord.equals("char") && !thisWord.equals("string")  )
    				thisWord = thisWord + "1";
    			if (thisWord.length() > 0 && thisWord!="\r1") {
    				wordList.add(thisWord);
    				if(n==1 && !thisWord.equals("class")) {
    					if(cList.contains(thisWord.substring(0, thisWord.length()-1))) {
    						errorM = "Duplicate class";
    					}
    					cList.add(thisWord.substring(0, thisWord.length()-1));
    					n = 0;
    				}
    				thisWord = "";
    				}
    			if (line.charAt(i)  == '(' || line.charAt(i) == ')'
    					|| line.charAt(i)=='{'  || line.charAt(i)=='}' || line.charAt(i)==',' || line.charAt(i)==';' || line.charAt(i)=='.')
    				{
    				thisWord = line.charAt(i) + "2";    	
    				wordList.add(thisWord);
    				thisWord = "";
    				}
    		}
    			else {
    		thisWord+= line.charAt(i);
    	}
    	if((i+1) == line.length() && thisWord.length()>0)
    	wordList.add(thisWord);
    	}	
    }
    
    /*
     * Returns a vector containing the information about the read-in code as a list of classes.
     * 
     * @return methList
     */
	public Vector<ClassData> getNumbers() {
		// TODO Auto-generated method stub
		return methList;
	}
	
	/*
	 * Checks whether the section of code being read is a syntactically appropriate method.  Sets up an error message otherwise.
	 */
	 private void checkMethod() {
	    	currWord = wordList.get(currentToken);
	    	if(currWord.equals("int") || currWord.equals("string") || currWord.equals("boolean") || currWord.equals("char") 
	    		|| currWord.equals("float")|| (cList.contains(currWord.substring(0, currWord.length()-1))&&!wordList.get(currentToken+1).equals(".2"))) {
	    		checkVariable(1);
	    		return;
	    	}
	    	if (currWord.endsWith("1")) currentToken++;
	    	else {
	    		errorM= "Method composition is incorrect";
	    		handleError();
	    	}
	    	currWord = wordList.get(currentToken);
	    	if (currWord.endsWith("2")  && currWord.charAt(0) == '(') 	currentToken++;
	    	else {
	    		errorM= "Method composition is incorrect";
	    		handleError();
	    	}
	    	currWord = wordList.get(currentToken);
	    	if (currWord.endsWith("2") && currWord.charAt(0) == ')') 	currentToken++;
	    	else {
	    		errorM= "Method composition is incorrect";
	    		handleError();
	    	}
	    	methodType = new MethodType(wordList.get(currentToken-3).substring(0, wordList.get(currentToken-3).length()-1));
	    	descText = descText + ("[");
	    	currWord = wordList.get(currentToken);
	    	if (currWord.endsWith("2")  && currWord.charAt(0) == '{') {
	    		currentToken++;
	    	}
	    	else {
	    		errorM= "Method composition is incorrect";
	    		handleError();
	    	}
	    	checkMethod2();
	 }
	 
	 /*
	  * The succession function for checking a method, which decides what type of instruction is coming
	  * next (variables, while, etc) and how to handle it.
	  * @throw
	  */
	 private void checkMethod2() {
	    	do {
	    	currWord = wordList.get(currentToken);
	    	if (currWord.equals("if")) {
	    		checkIf();
	    	}
	    	else if (currWord.equals("while")) {
	    		checkWhile();
	    	}
	    	else if(currWord.equals("int") || currWord.equals("string") || currWord.equals("boolean") || currWord.equals("char") 
	    			|| currWord.equals("float") ||(cList.contains(currWord.substring(0, currWord.length()-1))&&!wordList.get(currentToken+1).equals(".2"))) checkVariable(2);
	    	else if (currWord.substring(currWord.length() - 1).equals("1")) checkInstruction();
	    	else if (currWord.charAt(0)== '}') {
	    		descText = descText + ("]");
	            funcData.addMethod(methodType);
	    		currentToken++;
	    		return;
	    	}
	    	else {
	    		handleError();
	    	}
	    	} while (currentToken < vectSize);
    		errorM= "Reached end of method while parsing";
    		handleError();		
	    }
	    
		/** This method makes sure that the "if" keyword conforms to its rules of use.  There are no
		* parameters or returned values but an error will be thrown if coding syntax standards are violated
	    * and "<>" is printed to screen to represent a functional if statement.
	    * @throw
	    */
		private void checkIf() {
	    	currWord = wordList.get(currentToken);
	    	if (currWord.equals("if")) currentToken++;
	    	else {
	    		errorM= "If composition is incorrect";
	    		handleError();
	    	}
	    	currWord = wordList.get(currentToken);
	    	if (currWord.endsWith("2")  && currWord.charAt(0) == '(') 	currentToken++;
	    	else {
	    		errorM= "If composition is incorrect";
	    		handleError();
	    	}
	    	checkInner();
	    	methodType.setcIf(true);
	    	descText = descText + ("<");
	    	currWord = wordList.get(currentToken);
	    	if (currWord.endsWith("2")  && currWord.charAt(0) == '{') {
	    		currentToken++;
	    	}
	    	else {
	    		errorM= "If composition is incorrect";
	    		handleError();
	    	}
	    	checkIf2();
		}
		
		/*
		 * Handles the code within the if method's brackets and picks an instruction to follow based on the code until 
		 * the if method is exited.
		 */
		
		private void checkIf2() {
	    	do {
	    	currWord = wordList.get(currentToken);
	    	if (currWord.equals("if")) checkIf();
	    	else if (currWord.equals("while")) checkWhile();
	    	else if(currWord.equals("int") || currWord.equals("string") || currWord.equals("boolean") || currWord.equals("char") 
	    			|| currWord.equals("float") ||(cList.contains(currWord.substring(0, currWord.length()-1))&&!wordList.get(currentToken+1).equals(".2"))) checkVariable(2);
	    	else if (currWord.endsWith("1")) checkInstruction();
	    	else if (currWord.endsWith("2")  && currWord.charAt(0) == '}') {
	    		descText = descText + (">");
	    		currentToken++;
	    		return;
	    	}
	    	else {
	    		errorM= "If composition is incorrect";
	    		handleError();
	    	}
	    	} while (currentToken < vectSize);
	    	handleError();
		}
		
		/** This method makes sure that the "while" keyword conforms to its rules of use.  There are no
		* parameters or returned values but an error will be thrown if coding syntax standards are violated
		* and "()" is printed to screen to represent a functional while command.
		* @throw
		*/
		private void checkWhile() {
	    	currWord = wordList.get(currentToken);
	    	if (currWord.equals("while")) currentToken++;
	    	else {
	    		errorM= "While composition is incorrect";
	    		handleError();
	    	}
	    	currWord = wordList.get(currentToken);
	    	if(currWord.endsWith("2") && currWord.charAt(0) == '(' ) 	currentToken++;
	    	else {
	    		errorM= "While composition is incorrect";
	    		handleError();
	    	}
	   		checkInner();
	    	descText = descText + ("(");
	    	methodType.setcWhile(true);
	    	currWord = wordList.get(currentToken);
	    	if (currWord.endsWith("2")  && currWord.charAt(0) == '{') {
	    		currentToken++;
	    	}
	    	else {
		    		errorM= "While composition is incorrect";
		    		handleError();
	    	}
	    	checkWhile2();
		}
		
		/*
		 * Handles the loop after the while line and the choice of operation to check.
		 */
		private void checkWhile2() {
	    	do {
	    	currWord = wordList.get(currentToken);
	    	if (currWord.equals("if")) checkIf();
	    	else if (currWord.equals("while")) checkWhile();
	    	else if(currWord.equals("int") || currWord.equals("string") || currWord.equals("boolean") || currWord.equals("char") 
	    			|| currWord.equals("float") ||(cList.contains(currWord.substring(0, currWord.length()-1))&&!wordList.get(currentToken+1).equals(".2"))) checkVariable(2);
	    	else if (currWord.endsWith("1")) checkInstruction();
	    	else if (currWord.endsWith("2")  && currWord.charAt(0) == '}') {
	    		descText = descText + (")");
	    		currentToken++;
	    		return;
	    	}
	    	else throw new java.lang.Error("Bad.");
	    	} while (currentToken < vectSize);
	    		errorM= "While composition is incorrect";
	    		handleError();
		}
		/*
		 * This method checks the inner section of if and while parameters to make sure that proper syntax
		 * is followed.
		 */
		private void checkInner() {
			int counter = 0;
			do {
	    	currWord = wordList.get(currentToken);
	   		currentToken++;
	    	if (currWord.endsWith("2")  && currWord.charAt(0) == ')') {
	    		if(counter == 0) {
	    		return;
	    	} else counter--;
	    	}
	        if (currWord.endsWith("2")  && currWord.charAt(0) == '(') {
	        		counter++;
	        	}
			} while (currentToken < vectSize);
			errorM="Inner parentheses of while is incorrect";
			handleError();
		}
		
		/** This method makes sure that normal instructions/single lines of statements are used properly.  There are no
		* parameters or return values but an error will be thrown if coding syntax standards are violated
		* and "-" is printed to screen to represent a functional statement.
		* @throw
		*/
		private void checkInstruction() {
			currWord = wordList.get(currentToken);
	    	if (currWord.substring(currWord.length() - 1).equals("1")) currentToken++;
	    	else {
	    		errorM="Incorrect instruction";
	    		handleError();
	    	}
	    	currWord = wordList.get(currentToken);
	    	if(currWord.equals(".2") ) {
	    		currentToken++;
	    		if(!cList.contains(wordList.get(currentToken-2).substring(0, wordList.get(currentToken-2).length()-1))) throw new java.lang.Error("Bad");
	    		if (wordList.get(currentToken).substring(wordList.get(currentToken).length() - 1).equals("1")) currentToken++;
	    		else {
		    		errorM="Incorrect instruction";
		    		handleError();
	    		}
	    		methodInfo.put(wordList.get(currentToken-3).substring(0, wordList.get(currentToken-3).length()-1), wordList.get(currentToken-1).substring(0, wordList.get(currentToken-1).length()-1));
	    		if (wordList.get(currentToken).equals("(2")) currentToken++;
	    		else {
		    		errorM="Incorrect instruction";
		    		handleError();
	    		}
	    		if (wordList.get(currentToken).equals(")2")) currentToken++;
	    		else {
		    		errorM="Incorrect instruction";
		    		handleError();
	    		}
	    		if (wordList.get(currentToken).equals(";2")) currentToken++;
	    		else {
		    		errorM="Incorrect instruction";
		    		handleError();
	    		}
	    		descText = descText + ("-");
	    		funcData.setAssoclinks(wordList.get(currentToken-6).substring(0, wordList.get(currentToken-6).length()-1));
	    	}
	    	else if (currWord.equals(";2"))
	    	{
	    		descText = descText + ("-");
	    		//funcData.setAssoclinks(wordList.get(currentToken-1));
	    		currentToken++;
	    	}
	    	else {
	    		errorM="Incorrect instruction";
	    		handleError();
	    	}
			
		}
		/*
		 * checkClass analyzes the section of code being read to make sure that it corresponds to the proper format
		 * for a class (for example, the first token input is the word "class").
		 * @throw
		 */
		private void checkClass() {
			currWord = wordList.get(currentToken);
			if(currWord.equals("class"))currentToken++;
	    	else {
	    		errorM = "Incorrect class construction";
	    		handleError();
	    	}
			currWord = wordList.get(currentToken);
			if(currWord.substring(currWord.length() - 1).equals("1")) currentToken++;
			else {
				errorM = "Incorrect class construction";
				handleError();
			}
			currWord = wordList.get(currentToken);
			if(currWord.equals("{2")) currentToken++;
			else {
				errorM = "Incorrect class construction";
				handleError();
			}
			funcData = new ClassData(wordList.get(currentToken-2).substring(0, wordList.get(currentToken-2).length()-1));
			do {
			checkMethod();
			currWord = wordList.get(currentToken);
			if(currWord.endsWith("2")  && currWord.charAt(0) == '}') {
				methList.add(funcData);
				currentToken++;
			    setChanged();
			    notifyObservers();
				//for(int i = 0; i < methList.size(); i++) {
				//	System.out.print("aggr " + methList.get(i).getAggrlinks().size()+"\n");
				//	System.out.print("assoc " + methList.get(i).getAssoclinks().size()+"\n");
				//}
				errorCheck();
			   	return;
			}
			} while (currentToken < vectSize);
			errorM = "Reached end of class while parsing.";
			handleError();
		}
		
		/*
		 * Handles calls to nonexistent methods from an extant class.
		 */
		private void errorCheck() {
		    Iterator it = methodInfo.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        for(int i = 0; i < methList.size(); i++) {
		        	if(methList.get(i).getcName().equals(pair.getKey())) {
		        		int ww = 0;
		        		for(int n = 0; n<methList.get(i).getMethodType().size(); n++) {
		        			if(methList.get(i).getMethodType().get(n).getName().equals(pair.getValue()))
		        				ww = 1;
		        		}
		        		if(ww==0) {
		        		errorM="Invalid method call by a class";
	        			handleError();
		        		}
		        	}
		        }	
		        }
		}
		/*
		 *Checks to make sure that the line of code being read properly conforms to a variable declaration. 
		 */
		private void checkVariable(int loc) {
			currWord = wordList.get(currentToken);
			if(currWord.equals("int") || currWord.equals("float") || currWord.equals("char") || currWord.equals("boolean") ||
					currWord.equals("string") || (cList.contains((currWord).substring(0, currWord.length()-1)))) currentToken++;
	    	else {
	    		errorM = "Incorrect variable statement";
	    		handleError();
	    	}
			if(cList.contains(wordList.get(currentToken-1).substring(0, wordList.get(currentToken-1).length()-1))) {
				if(loc==1 && !funcData.getAggrlinks().contains(wordList.get(currentToken-1).substring(0, wordList.get(currentToken-1).length()-1))) {
				funcData.setAggrlinks(wordList.get(currentToken-1).substring(0, wordList.get(currentToken-1).length()-1));
			}else if(loc==2 && !funcData.getAssoclinks().contains(wordList.get(currentToken-1).substring(0, wordList.get(currentToken-1).length()-1))) {
				funcData.setAssoclinks(wordList.get(currentToken-1).substring(0, wordList.get(currentToken-1).length()-1));
			}
			}
			do {
				currWord = wordList.get(currentToken);
				if(currWord.endsWith("1")) {
					currentToken++;
				}
				else {
					errorM = "Incorrect class format";
					throw new java.lang.Error("Bad");
				}
				currWord = wordList.get(currentToken);
				if(currWord.equals(",2")) currentToken++;
				else if(currWord.equals(";2")) {
					currentToken++;
					return;
				}		
			} while (currentToken < vectSize);
			currWord = wordList.get(currentToken);
			if(currWord.equals("{2")) currentToken++;
			else {
				errorM = "Incorrect variable statement";
				handleError();
			}			
		}
		
		/*
		 * Reads in input from a text file submitted by the command line.
		 */
	    public static String readTextFile(String z) throws IOException {
	    	String text = "";
	    	String str;
	    	BufferedReader myReader = 
	    			  new BufferedReader(new FileReader(z));
	        while ((str = myReader.readLine()) != null) {
	          text+= str+ " ";
	        }
	        myReader.close();
	    	return text;
	    }
		
	    /*
	     * Called when an error is detected.  The observers print the correctly parsed material and post
	     * the error to the user.
	     */
	    private void handleError() {
		    setChanged();
		    notifyObservers();
		    throw new java.lang.Error("Incorrect syntax");
	    }
	    
	/*
	 * Returns the error status, or a visual depiction of the classes.
	 */
		public String getError() {
			// TODO Auto-generated method stub
			if(!errorM.equals(""))
			return errorM;
			else return descText;
		}
		}


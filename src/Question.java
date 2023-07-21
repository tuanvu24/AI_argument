import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Question {
	private Semantic semantic;
	private ArrayList<String> semExtension;
	private Statement reference;
	private Statement contrast;
	private String question;
	
	public Question(Statement reference, Statement contrast) {
		this.semantic = Semantic.CONFLICT_FREE;
		this.semExtension = new ArrayList<>();
		this.reference = reference;
		this.contrast = contrast;
		this.question = "";
	}
	
	public Statement getReference () {
		return reference;
	}
	
	public void setContrast (Statement contrast) {
		this.contrast = contrast;
	}
	
	public Statement getContrast () {
		return contrast;
	}
	
	public void setSemantic (Semantic semantic) {
		this.semantic = semantic;
	}
	
	public Semantic getSemantic () {
		return semantic;
	}
	
	public void setSemExtension (ArrayList<String> semExtension) {
		this.semExtension = semExtension;
	}
	
	public ArrayList<String> getSemExtension () {
		return semExtension;
	}
	
	public String semExtensionToString() {
		String semE = "";
		for (String arg: semExtension) {
			semE += arg + ", ";
		}
		semE = "{" + semE.substring(0, semE.length()-2) + "}";
		return semE;
	}
	
	
	public void buildQuestion() {
		String question = "Why do we have ";
		// Reference Statement
		String eleIntRef = "";
		ArrayList<String> listEleIntRef = reference.getListEleInt();
		for (String ele: listEleIntRef) {
			eleIntRef += ele + ", ";
		}
		
		String notPRef;
		if (reference.getNotProp()) {
			notPRef = "not ";
		}
		else {
			notPRef = "";
		}
		
		Property propertyRef = reference.getProperty();
		question += "{" + eleIntRef.substring(0, eleIntRef.length()-2) + "} " + notPRef; 
		
		if (propertyRef != Property.ACCEPTED) {
			question += "as " + propertyRef.toString() + " ";
		}
		else {
			question += propertyRef.toString() + " ";
		}
				
		
		Context contextRef = reference.getContext();
		switch (contextRef) {
		case EXTENSION_S:
			question += "in the extension " + semExtensionToString() + " ";
			break;
		case UNDEFINED:
			break;
		default:
			question += contextRef.toString() + " ";
			break;	
		}
		
		// Contrast Statement
		if (contrast.getTypeStatement() != TypeStatement.UNDEFINED) {
			question += "and not ";
			String eleIntContr = "";
			ArrayList<String> listEleIntContr = contrast.getListEleInt();
			if (!listEleIntContr.isEmpty()) {
				for (String ele: listEleIntContr) {
					eleIntContr += ele + ", ";
				}
			}
			
			String notContr;
			if (contrast.getNotProp()) {
				notContr = "not ";
			}
			else {
				notContr = "";
			}
			
			
			if (eleIntContr.length() > 0) {
				question += "{" + eleIntContr.substring(0, eleIntContr.length()-2) + "} ";
			}
			
			Property propertyContr = contrast.getProperty();
			question += notContr + propertyContr.toString() + " ";

			Context contextContr = contrast.getContext();
			switch (contextContr) {
			case EXTENSION_S:
				question += "in the extenstion " + semExtensionToString() + " ";
				break;
			case UNDEFINED:
				break;
			default:
				question += contextContr.toString() + " ";
				break;	
			}
		}
		question += "?";
		
		this.question = question;
	}
	
	public String getQuestion() {
		return question;
	}
	
}

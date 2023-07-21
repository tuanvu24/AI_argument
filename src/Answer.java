import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Answer {
	private HashMap<String, ArrayList<String>> graph;
	private Question question;
	private String answerConflitFree;
	private String answerDefence;
	private String answerReinstatement1;
	private String answerReinstatement2;
	private String answerComplementAttack;
	private String finalAnswer;
	private boolean cpConflictFree; // cp = checking procedure
	private boolean cpDefence;
	private boolean cpReinstatement1;
	private boolean cpReinstatement2;
	private boolean cpComplementAttack;
	private boolean checkingProcedureTotal;
	private boolean positiveAcceptanceFail; // pour savoir la réponse OUI ou NON pour la question positive
	private boolean negativeAcceptanceFail;	// pour savoir la réponse OUI ou NON pour la question négative "not"	
	
	public Answer(HashMap<String, ArrayList<String>> graph, Question question) {
		this.graph = graph;
		this.question = question;
		this.answerConflitFree = "";
		this.answerDefence = "";
		this.answerReinstatement1 = "";
		this.answerReinstatement2 = "";
		this.answerComplementAttack = "";
		this.finalAnswer = "";
		this.cpConflictFree = true;
		this.cpDefence = true;
		this.cpReinstatement1 = true;
		this.cpReinstatement2 = true;
		this.cpComplementAttack = true;
		this.checkingProcedureTotal = true;
		this.positiveAcceptanceFail = false;
		this.negativeAcceptanceFail = false;
	}
	
	public void setGraph(HashMap<String, ArrayList<String>> graph) {
		this.graph = graph;
	}
	
	public void setcheckingProcedureTotal(boolean checkingProcedureTotal) {
		this.checkingProcedureTotal = checkingProcedureTotal;
	}
	
	public void setAnswerConflitFree(String answerConflitFree) {
		this.answerConflitFree = answerConflitFree;
	}
	
	public void setAnswerDefence(String answerDefence) {
		this.answerDefence = answerDefence;
	}
	
	public String getAnswerConflictFree() {
		return answerConflitFree;
	}
	
	public String getAnswerDefence() {
		return answerDefence;
	}
	
	public String getAnswerReinstatement1() {
		return answerReinstatement1;
	}
	
	public String getAnswerReinstatement2() {
		return answerReinstatement2;
	}
	
	public String getAnswerComplementAttack() {
		return answerComplementAttack;
	}
	
	public String getFinalAnswer() {
		return finalAnswer;
	}
	
	public boolean getCpConflictFree() {
		return cpConflictFree;
	}
	
	public boolean getCpDefence() {
		return cpDefence;
	}
	
	public boolean getCpReinstatement1() {
		return cpReinstatement1;
	}
	
	public boolean getCpReinstatement2() {
		return cpReinstatement2;
	}
	
	public boolean getCpComplementAttack() {
		return cpComplementAttack;
	}
	
	public boolean getcheckingProcedureTotal() {
		return checkingProcedureTotal;
	}
	
	public boolean getPositiveAcceptanceFail() {
		return positiveAcceptanceFail;
	}
	
	public boolean getNegativeAcceptanceFail() {
		return negativeAcceptanceFail;
	}
	
	private void writeGraphFile() {
		try {
			FileWriter myWriter = new FileWriter("./graphviz/graph.dot");
			// layout = sfdp
			String graphDot = "digraph G { layout = circo; ";
		    for (Map.Entry mapentry : graph.entrySet()) {
		    	String argAttack = (String) mapentry.getKey();
		    	ArrayList<String> listArg = (ArrayList<String>) mapentry.getValue();
		    	graphDot += argAttack + "; ";
				for(String a : listArg) {
					graphDot += argAttack +" -> "+ a + "[color = black, fontcolor = black]; ";
				}
		 	}
		    graphDot += "}";
		      
		    myWriter.write(graphDot);
		    myWriter.close();
		    System.out.println("Successfully wrote to graph.dot");
		} 
		catch (IOException e) {
			System.out.println("An error occurred with graph.dot");
			e.printStackTrace();	
		}	
	}
	
	private String listArgToString(ArrayList<String> S) {
		String listString = "";
		if (!S.isEmpty()) {
			for (String ele: S) {
				listString += ele + ", ";
			}
		}
		listString = "{" + listString.substring(0, listString.length()-2) + "}";
		
		return listString;
	}
	
	// conflit-free OK
	private void conflictFree() {
		cpConflictFree = true;
		String conflitFreeDot = "digraph G { layout = circo; ";
		Statement ref = question.getReference();
		ArrayList<String> S = new ArrayList<>();
		if (ref.getProperty() != Property.ACCEPTED) {
			S = ref.getListEleInt();
		}
		else {
			ArrayList<String> S1 = question.getSemExtension();
			ArrayList<String> S2 = ref.getListEleInt();
			if (!ref.getNotProp()) {
				S = Operation.difference(S1, S2);
			}
			else {
				S = Operation.union(S1, S2);
			}
		}
		HashMap<String, ArrayList<String>> subgraph = Operation.inducedSubgraph(graph, S);
		// graphNotDrawn contient la partie de graphe non concerné à griser 
		HashMap<String, ArrayList<String>> graphNotDrawn = new HashMap<String, ArrayList<String>>(graph);	
		
		// Start checking procedure conflict-free
		int nbArcs = 0;
		for (Map.Entry mapentry : graph.entrySet()) {
			String a = (String) mapentry.getKey();
			conflitFreeDot += a + "[color = \"#DEE1DF\", fontcolor = \"#DEE1DF\"]; ";
		}
		
		for (Map.Entry mapentry : subgraph.entrySet()) {
			String a = (String) mapentry.getKey();
			ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
			if (S.contains(a)) {
				conflitFreeDot += a + "[color = \"#4da6ff\", fontcolor = black, fillcolor = \"#99ccff\", style = filled]; ";
			}
			else {
				conflitFreeDot += a + "[color = black, fontcolor = black]; ";
			}
			
			if(!listArgAttacked.isEmpty()) {
				for(String b: listArgAttacked) {
					conflitFreeDot += a + " -> "+ b + " [color = red]; ";
					checkingProcedureTotal = false;
					cpConflictFree = false;
					nbArcs += 1; 
				}
			}	
				
			ArrayList<String> listArgAttackedNotDrawn = new ArrayList<>(graphNotDrawn.get(a));
			listArgAttackedNotDrawn.removeAll(listArgAttacked);
			graphNotDrawn.put(a, listArgAttackedNotDrawn);			
		}
		// End checking procedure conflict-free
		System.out.println("cpConflictFree: "+cpConflictFree );
		
		// Griser la partie de graphe non concerné
		for (Map.Entry mapentry : graphNotDrawn.entrySet()) {
			String a = (String) mapentry.getKey();
			ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
			
			/*if (!subgraph.containsKey(a)) {
				conflitFreeDot += a + "[color = \"#DEE1DF\", fontcolor = \"#DEE1DF\"]; ";
			}*/	
			for (String b: listArgAttacked) {
				conflitFreeDot += a + " -> "+ b + "[color = \"#DEE1DF\"]; ";	
			}
		}
		conflitFreeDot += "}";	
		
		
		switch (nbArcs) {
		case 0:
			answerConflitFree = "All arcs between " + listArgToString(S) + " are represented and since there are none.";
			break;
		case 1:
			answerConflitFree = "All arcs between " + listArgToString(S) + " are represented and there is 1 between them.";
			break;
		default:
			answerConflitFree = "All arcs between " + listArgToString(S) + " are represented and there are " + nbArcs + " between them.";
			break;
		}
			
		try {
			FileWriter myWriter = new FileWriter("./graphviz/conflict_free.dot");   
		    myWriter.write(conflitFreeDot);
		    myWriter.close();
		    System.out.println("Successfully wrote to conflict_free.dot"); 
		} 
		catch (IOException e) {
			System.out.println("An error occurred with conflict_free.dot");
			e.printStackTrace();	
		}	
	}
	
	// Defence OK
	private void defence() {
		cpDefence = true;
		Statement ref = question.getReference();
		ArrayList<String> S = new ArrayList<>();
		if (ref.getProperty() != Property.ACCEPTED) {
			S = ref.getListEleInt();
		}
		else {
			ArrayList<String> S1 = question.getSemExtension();
			ArrayList<String> S2 = ref.getListEleInt();
			if (!ref.getNotProp()) {
				S = Operation.difference(S1, S2);
			}
			else {
				S = Operation.union(S1, S2);
			}
		}
		ArrayList<String> pre1EleInt = Operation.predecessor(graph, S, 1);
		ArrayList<String> union_EleInt_pre1EleInt = Operation.union(S, pre1EleInt);
		HashMap<String, ArrayList<String>> induceSubgraph = Operation.inducedSubgraph(graph, union_EleInt_pre1EleInt);
		
		HashMap<String, ArrayList<String>> S2 = new HashMap<>();
		for (Map.Entry mapentry : induceSubgraph.entrySet()) {
			String a = (String) mapentry.getKey();
			ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
			
			ArrayList<String> listArgAttacked1 = new ArrayList<>();
			if (pre1EleInt.contains(a) && !listArgAttacked.isEmpty()) {		
				for (String b: listArgAttacked) {
					if (S.contains(b)) {
						listArgAttacked1.add(b);
					}
				}
			}
			
			ArrayList<String> listArgAttacked2 = new ArrayList<>();
			if (S.contains(a) && !listArgAttacked.isEmpty()) {			
				for (String b: listArgAttacked) {
					if (pre1EleInt.contains(b)) {
						listArgAttacked2.add(b);
					}
				}
			}
			
			ArrayList<String> listArgAttacked12 = Operation.union(listArgAttacked1, listArgAttacked2);
			if (!S2.containsKey(a)) {
				S2.put(a, listArgAttacked12);
			}
			else {
				S2.put(a, Operation.union(S2.get(a), listArgAttacked12));
			}
		}
		HashMap<String, ArrayList<String>> partialSubgraph = Operation.partialSubgraph(induceSubgraph, S2);
			
		String defenceDot = "digraph G { layout = circo; "; 
		
		// Start checking procedure defence
		int nbRedArcs = 0;
		ArrayList<String> redArg = new ArrayList<>();
		ArrayList<String> argNotDefended = new ArrayList<>();
		// graphNotDrawn contient la partie de graphe non concerné à griser 
		HashMap<String, ArrayList<String>> graphNotDrawn = new HashMap<String, ArrayList<String>>(graph);
		ArrayList<String> succ1S = Operation.successor(partialSubgraph, S, 1);
		
		for (Map.Entry mapentry : graph.entrySet()) {
			String a = (String) mapentry.getKey();
			defenceDot += a + "[color = \"#DEE1DF\", fontcolor = \"#DEE1DF\"]; ";
		}
		
		for (Map.Entry mapentry : partialSubgraph.entrySet()) {
			String a = (String) mapentry.getKey();
			ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
			
			if (S.contains(a)) {
				defenceDot += a + "[color = \"#4da6ff\", fontcolor = black, fillcolor = \"#99ccff\", style = filled]; ";
				for(String b: listArgAttacked) {
					defenceDot += b + "[color = black, fontcolor = black]; ";
					defenceDot += a + " -> "+ b + "; ";
				}
			}
			else {
				if (!succ1S.contains(a)) {
					checkingProcedureTotal = false;
					cpDefence = false;
					defenceDot += a + " [color = red, fontcolor = red, penwidth = 1.9]; ";
					redArg.add(a);
					for(String b: listArgAttacked) {
						defenceDot += a + " -> "+ b + "[color = red]; ";
						nbRedArcs++;
						if (!argNotDefended.contains(b)) {
							argNotDefended.add(b);
						}				
					}
				}
				else {
					for(String b: listArgAttacked) {
						defenceDot += a + " -> "+ b + "; ";			
					}
				}
			}
			
			ArrayList<String> listArgAttackedNotDrawn = new ArrayList<>(graphNotDrawn.get(a));
			listArgAttackedNotDrawn.removeAll(listArgAttacked);
			graphNotDrawn.put(a, listArgAttackedNotDrawn);
		}
		// End checking procedure defence
		
		// Griser la partie de graphe non concerné
		for (Map.Entry mapentry : graphNotDrawn.entrySet()) {
			String a = (String) mapentry.getKey();
			ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
			
			for (String b: listArgAttacked) {
				defenceDot += a + " -> "+ b + "[color = \"#DEE1DF\"]; ";	
			}
		}		
		
		defenceDot += "}";
		
		switch (nbRedArcs) {
		case 0:
			answerDefence = "All the attackers of " + listArgToString(S) + " are attacked in return.";
			break;
		default:
			String argNotDefendedText = "{";
			for (String b: argNotDefended) {
				argNotDefendedText += b + ", ";
			}
			argNotDefendedText = argNotDefendedText.substring(0, argNotDefendedText.length()-2) + "}";
			
			String redArgText = "{";
			for (String a: redArg) {
				redArgText += a + ", ";
			}
			redArgText = redArgText.substring(0, redArgText.length()-2) + "}";
			answerDefence = argNotDefendedText + " is attacked by " + redArgText + " and " + 
							redArgText + " is not attacked by " + listArgToString(S) + " in return.";				
			break;
		}
		
		try {			
			FileWriter myWriter = new FileWriter("./graphviz/defence.dot");     
		    myWriter.write(defenceDot);
		    myWriter.close();
		    System.out.println("Successfully wrote to defence.dot");	    
		} 
		catch (IOException e) {
			System.out.println("An error occurred with defence.dot");
			e.printStackTrace();	
		}	
	}
	
	// reinstatement1 OK
	private void reinstatement1() {
		cpReinstatement1 = true;
		Statement ref = question.getReference();
		ArrayList<String> S = new ArrayList<>();
		if (ref.getProperty() != Property.ACCEPTED) {
			S = ref.getListEleInt();
		}
		else {
			ArrayList<String> S1 = question.getSemExtension();
			ArrayList<String> S2 = ref.getListEleInt();
			if (!ref.getNotProp()) {
				S = Operation.difference(S1, S2);
			}
			else {
				S = Operation.union(S1, S2);
			}
		}

		ArrayList<String> listArgNonAttacked = new ArrayList<>();
		for (Map.Entry mapentry : graph.entrySet()) {
			String a = (String) mapentry.getKey();
			ArrayList<String> A = new ArrayList<>();
			A.add(a);
			ArrayList<String> pre1A = Operation.predecessor(graph, A, 1);
			if (pre1A.isEmpty()) {
				listArgNonAttacked.add(a);
			}
		}
		
		HashMap<String, ArrayList<String>> induceSubgraph = Operation.inducedSubgraph(graph, listArgNonAttacked);
		
		// Start checking procedure reinstatement1
		String reinstatement1Dot = "digraph G { layout = circo; ";
		ArrayList<String> redArg = new ArrayList<>();
		// graphNotDrawn contient la partie de graphe non concerné à griser 
		HashMap<String, ArrayList<String>> graphNotDrawn = new HashMap<String, ArrayList<String>>(graph);
		
		for (Map.Entry mapentry : graph.entrySet()) {
			String a = (String) mapentry.getKey();
			reinstatement1Dot += a + "[color = \"#DEE1DF\", fontcolor = \"#DEE1DF\"]; ";
		}
		
		for (Map.Entry mapentry : induceSubgraph.entrySet()) {
			String a = (String) mapentry.getKey();
			
			if (S.contains(a)) {
				reinstatement1Dot += a + " [color = \"#4da6ff\", fontcolor = black, fillcolor = \"#99ccff\", style = filled]; ";
			}
			else {
				reinstatement1Dot += a + " [color = red, fontcolor = red, penwidth = 1.9]; ";
				redArg.add(a);
				cpReinstatement1 = false;
				checkingProcedureTotal = false;
			}
		}
		// End checking procedure reinstatement1
		
		// griser la partie de graphe non concerné
		for (Map.Entry mapentry : graphNotDrawn.entrySet()) {
			String a = (String) mapentry.getKey();
			ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
	
			for (String b: listArgAttacked) {
				reinstatement1Dot += a + " -> "+ b + "[color = \"#DEE1DF\"]; ";	
			}
		}		
		reinstatement1Dot += "}";	
		
		if (cpReinstatement1) {
			answerReinstatement1 = "All arguments which are not attacked belong to " + listArgToString(S) + ".";
		}
		else {
			answerReinstatement1 = listArgToString(redArg) + " is not attacked by any argument but does not belong to " + listArgToString(S) + ".";
		}
		
		try {    
			FileWriter myWriter = new FileWriter("./graphviz/reinstatement1.dot");
		    myWriter.write(reinstatement1Dot);
		    myWriter.close();
		    System.out.println("Successfully wrote to reinstatement1.dot");	    
		} 
		catch (IOException e) {
			System.out.println("An error occurred with reinstatement1.dot");
			e.printStackTrace();	
		}
	}
	
	// reinstatement2 OK
	private void reinstatement2() {
		cpReinstatement2 = true;
		Statement ref = question.getReference();
		ArrayList<String> S = new ArrayList<>();
		if (ref.getProperty() != Property.ACCEPTED) {
			S = ref.getListEleInt();
		}
		else {
			ArrayList<String> S1 = question.getSemExtension();
			ArrayList<String> S2 = ref.getListEleInt();
			if (!ref.getNotProp()) {
				S = Operation.difference(S1, S2);
			}
			else {
				S = Operation.union(S1, S2);
			}
		}
		ArrayList<String> succ2S = Operation.successor(graph, S, 2);
		ArrayList<String> pre1_Succ2S = Operation.predecessor(graph, succ2S, 1);
		ArrayList<String> union_S_succ2S = Operation.union(S, succ2S);
		ArrayList<String> union1 = Operation.union(union_S_succ2S, pre1_Succ2S);
		HashMap<String, ArrayList<String>> induceSubgraph = Operation.inducedSubgraph(graph, union1);
		
		HashMap<String, ArrayList<String>> arcs = new HashMap<>();
		for (Map.Entry mapentry : graph.entrySet()) {
			String a = (String) mapentry.getKey();
			ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
			
			ArrayList<String> listArgAttacked1 = new ArrayList<>();
			if (pre1_Succ2S.contains(a)) {
				for (String b: listArgAttacked) {
					if (succ2S.contains(b)) {
						listArgAttacked1.add(b);
					}
				}
			}
			
			ArrayList<String> listArgAttacked2 = new ArrayList<>();
			if (S.contains(a)) {
				for (String b: listArgAttacked) {
					if (pre1_Succ2S.contains(b)) {
						listArgAttacked1.add(b);
					}
				}
			}
			
			ArrayList<String> union2 = Operation.union(listArgAttacked1, listArgAttacked2);
			if (!arcs.containsKey(a)) {
				arcs.put(a, union2);
			}
			else {
				arcs.put(a, Operation.union(arcs.get(a), union2));
			}
		}
			
		HashMap<String, ArrayList<String>> partialSubgraph = Operation.partialSubgraph(induceSubgraph, arcs);	
		
		// Start checking procedure reinstatement2
		String reinstatement2Dot = "digraph G { layout = circo; ";
		HashMap<String, ArrayList<String>> redAttackers = new HashMap<>(); // les attaquants de a ne sont pas l'extrémité d'une arête dont l'origine est en S et les arguments qu'ils attaquent 
		ArrayList<String> redArg = new ArrayList<>(); // a est acceptable wrt S mais a n'appartient pas à S
		
		for (Map.Entry mapentry : graph.entrySet()) {
			String a = (String) mapentry.getKey();
			reinstatement2Dot += a + "[color = \"#DEE1DF\", fontcolor = \"#DEE1DF\"]; ";
		}
		
		for (Map.Entry mapentry : partialSubgraph.entrySet()) {
			String a = (String) mapentry.getKey();
			ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
			if (succ2S.contains(a)) {
				ArrayList<String> A = new ArrayList<>();
				A.add(a);
				ArrayList<String> attackersOfa = Operation.predecessor(partialSubgraph, A, 1); // 1,2
				ArrayList<String> argsAttackedByS = Operation.successor(partialSubgraph, S, 1); // 2,3,4
				ArrayList<String> attackersOfaNotAttackedByS = new ArrayList<>();
				if (S.contains(a)) {	
					reinstatement2Dot += a + " [color = \"#4da6ff\", fontcolor = black, fillcolor = \"#99ccff\", style = filled]; ";
				}
				else {
					reinstatement2Dot += a + "[color = black, fontcolor = black]; ";
					if (Operation.isSubsetOf(attackersOfa, argsAttackedByS)) {
						checkingProcedureTotal = false;
						cpReinstatement2 = false;
						if (!redArg.contains(a)) {
							redArg.add(a);
						}
						reinstatement2Dot += a + " [color = red, fontcolor = red, penwidth = 1.9]; ";
					}
				}
			}
			else {
				if(S.contains(a)) {
					reinstatement2Dot += a + " [color = \"#4da6ff\", fontcolor = black, fillcolor = \"#99ccff\", style = filled]; ";
				}
				else {
					reinstatement2Dot += a + "[color = black, fontcolor = black]; ";
				}
			}
		}
		// End checking procedure reinstatement2
		
		// graphNotDrawn contient la partie de graphe non concerné à griser 
		HashMap<String, ArrayList<String>> graphNotDrawn = new HashMap<String, ArrayList<String>>(graph);
			
		for (Map.Entry mapentry : partialSubgraph.entrySet()) {
			String a = (String) mapentry.getKey();
			ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
			if (redAttackers.containsKey(a)) {
				ArrayList<String> argAttackedDrawn = redAttackers.get(a);
				for (String b: listArgAttacked) {
					if (!argAttackedDrawn.contains(b)) {
						reinstatement2Dot += a + " -> "+ b + "; ";	
					}	
				}
			}
			else {
				for (String b: listArgAttacked) {
					reinstatement2Dot += a + " -> "+ b + "; ";	
				}
			}
			
			ArrayList<String> listArgAttackedNotDrawn = new ArrayList<>(graphNotDrawn.get(a));
			listArgAttackedNotDrawn.removeAll(listArgAttacked);
			graphNotDrawn.put(a, listArgAttackedNotDrawn);
		}
		
		// griser la partie de graphe non concerné
		for (Map.Entry mapentry : graphNotDrawn.entrySet()) {
			String a = (String) mapentry.getKey();
			ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
	
			for (String b: listArgAttacked) {
				reinstatement2Dot += a + " -> "+ b + "[color = \"#DEE1DF\"]; ";	
			}
		}		
		reinstatement2Dot += "}";
	    
	    switch (redArg.size()) {
		case 0:
			switch (redAttackers.size()) {
			case 0:
				answerReinstatement2 = "Any argument defended by " + listArgToString(S) + " belongs to " + ref.listEleIntToString() + ".";
				break;
			default:
				String redArgText = "{";
				String argNotDefendedText = "{";
				for (Map.Entry mapentry : redAttackers.entrySet()) {
					String a = (String) mapentry.getKey();
					ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
					redArgText += a + ", ";
					for (String b: listArgAttacked) {
						argNotDefendedText += b + ", ";
					}
				}
				redArgText = redArgText.substring(0, redArgText.length()-2) + "}";
				argNotDefendedText = argNotDefendedText.substring(0, argNotDefendedText.length()-2) + "}";
				answerReinstatement2 = argNotDefendedText + " is attacked by " + redArgText + " and " + redArgText + " is not attacked by " + listArgToString(S) + " in return.";
			}			
			break;
		default:
			String redArgText = "{";
		    if (!redArg.isEmpty()) {
			    for (String a: redArg) {
			    	redArgText += a + ", ";
			    }
			    redArgText = redArgText.substring(0, redArgText.length()-2) + "}";
		    }
			answerReinstatement2 = redArgText + " is defended by " + listArgToString(S) + " but does not belong to the set.";
			break;
	    }
	    
	    try {    
			FileWriter myWriter = new FileWriter("./graphviz/reinstatement2.dot");
		    myWriter.write(reinstatement2Dot);
		    myWriter.close();
		    System.out.println("Successfully wrote to reinstatement2.dot");	    
		} 
		catch (IOException e) {
			System.out.println("An error occurred with reinstatement2.dot");
			e.printStackTrace();	
		}
	}
	
	// Complement attack OK
	private void complementAttack() {
		cpComplementAttack = true;
		Statement ref = question.getReference();
		ArrayList<String> S = new ArrayList<>();
		if (ref.getProperty() != Property.ACCEPTED) {
			S = ref.getListEleInt();
		}
		else {
			ArrayList<String> S1 = question.getSemExtension();
			ArrayList<String> S2 = ref.getListEleInt();
			if (!ref.getNotProp()) {
				S = Operation.difference(S1, S2);
			}
			else {
				S = Operation.union(S1, S2);
			}
		}
		HashMap<String, ArrayList<String>> arcs = new HashMap<>();
		for (Map.Entry mapentry : graph.entrySet()) {
			String a = (String) mapentry.getKey();
			ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
			
			ArrayList<String> listArgAttacked1 = new ArrayList<>();
			if (S.contains(a) && !listArgAttacked.isEmpty()) {		
				for (String b: listArgAttacked) {
					if (!S.contains(b)) {
						listArgAttacked1.add(b);
					}
				}
			}
			arcs.put(a, listArgAttacked1);
		}

		HashMap<String, ArrayList<String>> partialSubgraph = Operation.partialSubgraph(graph, arcs);
		
		String complementAtkDot = "digraph G { layout = circo; ";
		
		// Start checking procedure complement attack
		int nbArgs = 0;
		ArrayList<String> redArg = new ArrayList<>();
		ArrayList<String> succ1EleInt = Operation.successor(partialSubgraph, S, 1);		
		// graphNotDrawn contient la partie de graphe non concerné à griser 
		HashMap<String, ArrayList<String>> graphNotDrawn = new HashMap<String, ArrayList<String>>(graph);
		
		for (Map.Entry mapentry : graph.entrySet()) {
			String a = (String) mapentry.getKey();
			complementAtkDot += a + "[color = \"#DEE1DF\", fontcolor = \"#DEE1DF\"]; ";
		}
		
		for (Map.Entry mapentry : partialSubgraph.entrySet()) {
			String a = (String) mapentry.getKey();
			ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
			if (S.contains(a)) {
				complementAtkDot += a + " [color = \"#4da6ff\", fontcolor = black, fillcolor = \"#99ccff\", style = filled]; ";
			}		
			else {
				if (!succ1EleInt.contains(a)) {
					complementAtkDot += a + " [color = red, fontcolor = red, penwidth = 1.9]; ";
					nbArgs++;
					redArg.add(a);
					checkingProcedureTotal = false;
					cpComplementAttack = false;
				}	
				else {
					complementAtkDot += a + " [color = black, fontcolor = black]; ";
				}
			}
			
			for (String b: listArgAttacked) {
				complementAtkDot += a + " -> " + b + "; ";			
			}
			
			ArrayList<String> listArgAttackedNotDrawn = new ArrayList<>(graphNotDrawn.get(a));
			listArgAttackedNotDrawn.removeAll(listArgAttacked);
			graphNotDrawn.put(a, listArgAttackedNotDrawn);
		}
		// End checking procedure complement attack
		
		// Griser la partie de graphe non concerné
		for (Map.Entry mapentry : graphNotDrawn.entrySet()) {
			String a = (String) mapentry.getKey();
			ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
			
			for (String b: listArgAttacked) {
				complementAtkDot += a + " -> "+ b + "[color = \"#DEE1DF\"]; ";	
			}
		}			
		complementAtkDot += "}";
		
		String redArgText = "{";
	    if (!redArg.isEmpty()) {
		    for (String a: redArg) {
		    	redArgText += a + ", ";
		    }
		    redArgText = redArgText.substring(0, redArgText.length()-2) + "}";
	    }
	    
	    switch (nbArgs) {
		case 0:
			answerComplementAttack = "Any argument that does not belong to " + listArgToString(S) + " is attacked by " + listArgToString(S) + ".";
			break;
		default:
			answerComplementAttack = redArgText + " is not attacked by " + listArgToString(S) + ".";
			break;
	    }
				
		try {    
			FileWriter myWriter = new FileWriter("./graphviz/complement_attack.dot");
		    myWriter.write(complementAtkDot);
		    myWriter.close();
		    System.out.println("Successfully wrote to complement_attack.dot"); 
		} 
		catch (IOException e) {
			System.out.println("An error occurred with complement_attack.dot");
			e.printStackTrace();	
		}	
	}
	
	public void generateAF() {
		writeGraphFile();
		ProcessBuilder processBuilder = new ProcessBuilder();

		// Run a shell command
		processBuilder.command("bash", "-c", "dot -Tpng ./graphviz/graph.dot > ./graphviz/graph.png ");

		try {

			Process process = processBuilder.start();

			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println("Generate graph success!");
				System.out.println(output);
				//System.exit(0);
			} else {
				//abnormal...
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void makeExplanation() {
		checkingProcedureTotal = true;
		positiveAcceptanceFail = false;
		negativeAcceptanceFail = false;
		Statement ref = question.getReference();
		ArrayList<String> listCommand = new ArrayList<>();
		ArrayList<String> S1 = question.getSemExtension();
		ArrayList<String> S2 = ref.getListEleInt();
		
		switch (ref.getProperty()) {
		case ACCEPTED:
			if (!ref.getNotProp()) {
				if (Operation.isSubsetOf(S2, S1)) {
					switch (question.getSemantic()) {
					case CONFLICT_FREE:
						conflictFree();
						listCommand.add("dot -Tpng ./graphviz/conflict_free.dot > ./graphviz/conflict_free.png");
						break;
					case ADMISSIBLE:
						conflictFree();
						listCommand.add("dot -Tpng ./graphviz/conflict_free.dot > ./graphviz/conflict_free.png");
						defence();
						listCommand.add("dot -Tpng ./graphviz/defence.dot > ./graphviz/defence.png");
						break;
					case COMPLETE:
						conflictFree();
						listCommand.add("dot -Tpng ./graphviz/conflict_free.dot > ./graphviz/conflict_free.png");
						defence();
						listCommand.add("dot -Tpng ./graphviz/defence.dot > ./graphviz/defence.png");
						reinstatement1();
						listCommand.add("dot -Tpng ./graphviz/reinstatement1.dot > ./graphviz/reinstatement1.png");
						reinstatement2();
						listCommand.add("dot -Tpng ./graphviz/reinstatement2.dot > ./graphviz/reinstatement2.png");
						break;
					case STABLE:
						conflictFree();
						listCommand.add("dot -Tpng ./graphviz/conflict_free.dot > ./graphviz/conflict_free.png");
						complementAttack();
						listCommand.add("dot -Tpng ./graphviz/complement_attack.dot > ./graphviz/complement_attack.png");
						break;
					default:
						break;
					}
				}
				else {
					positiveAcceptanceFail = true;
				}
			}
			else {
				ArrayList<String> S = Operation.difference(S1, S2);
				if (S.size() < S1.size()) {
					negativeAcceptanceFail = true;
				}
				else {
					switch (question.getSemantic()) {
					case CONFLICT_FREE:
						conflictFree();
						listCommand.add("dot -Tpng ./graphviz/conflict_free.dot > ./graphviz/conflict_free.png");
						break;
					case ADMISSIBLE:
						conflictFree();
						listCommand.add("dot -Tpng ./graphviz/conflict_free.dot > ./graphviz/conflict_free.png");
						defence();
						listCommand.add("dot -Tpng ./graphviz/defence.dot > ./graphviz/defence.png");
						break;
					case COMPLETE:
						conflictFree();
						listCommand.add("dot -Tpng ./graphviz/conflict_free.dot > ./graphviz/conflict_free.png");
						defence();
						listCommand.add("dot -Tpng ./graphviz/defence.dot > ./graphviz/defence.png");
						reinstatement1();
						listCommand.add("dot -Tpng ./graphviz/reinstatement1.dot > ./graphviz/reinstatement1.png");
						reinstatement2();
						listCommand.add("dot -Tpng ./graphviz/reinstatement2.dot > ./graphviz/reinstatement2.png");
						break;
					case STABLE:
						conflictFree();
						listCommand.add("dot -Tpng ./graphviz/conflict_free.dot > ./graphviz/conflict_free.png");
						complementAttack();
						listCommand.add("dot -Tpng ./graphviz/complement_attack.dot > ./graphviz/complement_attack.png");
						break;
					default:
						break;
					}
				}
			}
			
			break;
		case CONFLICT_FREE:
			conflictFree();
			listCommand.add("dot -Tpng ./graphviz/conflict_free.dot > ./graphviz/conflict_free.png");
			break;
		case ADMISSIBLE:
			conflictFree();
			listCommand.add("dot -Tpng ./graphviz/conflict_free.dot > ./graphviz/conflict_free.png");
			defence();
			listCommand.add("dot -Tpng ./graphviz/defence.dot > ./graphviz/defence.png");
			break;
		case COMPLETE:
			conflictFree();
			listCommand.add("dot -Tpng ./graphviz/conflict_free.dot > ./graphviz/conflict_free.png");
			defence();
			listCommand.add("dot -Tpng ./graphviz/defence.dot > ./graphviz/defence.png");
			reinstatement1();
			listCommand.add("dot -Tpng ./graphviz/reinstatement1.dot > ./graphviz/reinstatement1.png");
			reinstatement2();
			listCommand.add("dot -Tpng ./graphviz/reinstatement2.dot > ./graphviz/reinstatement2.png");
			break;
		case STABLE:
			conflictFree();
			listCommand.add("dot -Tpng ./graphviz/conflict_free.dot > ./graphviz/conflict_free.png");
			complementAttack();
			listCommand.add("dot -Tpng ./graphviz/complement_attack.dot > ./graphviz/complement_attack.png");
			break;
		default:
			break;
		}
		
		for(String command: listCommand) {
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("bash", "-c", command);

			try {

				Process process = processBuilder.start();
				StringBuilder output = new StringBuilder();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

				String line;
				while ((line = reader.readLine()) != null) {
					output.append(line + "\n");
				}

				int exitVal = process.waitFor();
				if (exitVal == 0) {
					System.out.println("Generate" + command.split(">")[1] + " success!");
					System.out.println(output);
					//System.exit(0);
				} else {
					//abnormal...
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		switch (ref.getProperty()) {
		case ACCEPTED:
			if (!ref.getNotProp()) {
				if (!positiveAcceptanceFail) { 
					finalAnswer = "<html>" + ref.listEleIntToString() + " is <font color=green>" + ref.getProperty() + " </font>in the " + question.getSemantic() + " extension " + listArgToString(S1) + " and you have an explanation below.";
				}
				else {
					finalAnswer = "<html>Your question is <font color='red'>NOT</font> appropriate, <font color='red'>" + ref.listEleIntToString() + " must be a subset of " + listArgToString(question.getSemExtension()) + "</font>.</html>";
				}
			}
			else {
				if (!negativeAcceptanceFail) { 
					finalAnswer = "<html>" + ref.listEleIntToString() + " is <font color=green>not " + ref.getProperty() + " </font>in the " + question.getSemantic() + " extension " + listArgToString(S1) + " and you have an explanation below.";
				}
				else {
					finalAnswer = "<html>Your question is <font color='red'>NOT</font> appropriate, " + "<font color='red'> argument(s) of " + ref.listEleIntToString() + " may not be in " + listArgToString(question.getSemExtension()) + "</font>.</html>";
				}
			}
			
			break;

		default:
			if(!ref.getNotProp()) {
				if (checkingProcedureTotal) {
					finalAnswer = "<html>" + ref.listEleIntToString() + " is <font color=green>" + ref.getProperty() + "</font> and you have an explanation below.";
				}
				else {
					finalAnswer = "<html>Your question is <font color='red'>NOT</font> appropriate, " + ref.listEleIntToString() + " is <font color='red'>NOT </font>" + ref.getProperty() + ".</html>";
				}
			}
			else {
				if (checkingProcedureTotal) {	
					finalAnswer = "<html>Your question is <font color='red'>NOT</font> appropriate, " + ref.listEleIntToString() + " <font color='red'>IS</font> " + ref.getProperty() + ".</html>";
				}
				else {
					finalAnswer = "<html>" + ref.listEleIntToString() + " <font color=green>is not " + ref.getProperty() + "</font> and you have an explanation below.";
				}
			}
			break;
		}
	}
}

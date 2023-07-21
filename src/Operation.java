import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Operation {
	public static HashMap<String, ArrayList<String>> inducedSubgraph (HashMap<String, ArrayList<String>> graph, ArrayList<String> S) {
		HashMap<String, ArrayList<String>> inducedSubgraph = new HashMap<>();
	
		for (String a1: S) {
			ArrayList<String> listArgAttackedSubGraph = new ArrayList<>();
 			if (graph.containsKey(a1)) {
				ArrayList<String> listArgAttacked = graph.get(a1);
				for (String a2: S) {
					if (listArgAttacked.contains(a2)) {
						listArgAttackedSubGraph.add(a2);
					}
				}
			}
			inducedSubgraph.put(a1, listArgAttackedSubGraph);
		}

		return inducedSubgraph;
	}
	
	public static HashMap<String, ArrayList<String>> partialSubgraph (HashMap<String, ArrayList<String>> graph, HashMap<String, ArrayList<String>> S) {
		HashMap<String, ArrayList<String>> partialSubgraph = new HashMap<>();
	
		for (Map.Entry mapentry : graph.entrySet()) {
			String ArgAttack = (String) mapentry.getKey();
			if (!S.containsKey(ArgAttack)) {
				partialSubgraph.put(ArgAttack, new ArrayList<>());
			}
			else {
				partialSubgraph.put(ArgAttack, S.get(ArgAttack));
			}
		}

		return partialSubgraph;
	}
	
	public static HashMap<String, ArrayList<String>> union (HashMap<String, ArrayList<String>> graph1, HashMap<String, ArrayList<String>> graph2) {
		for (Map.Entry mapentry : graph2.entrySet()) {
			String ArgAttack2 = (String) mapentry.getKey();
			ArrayList<String> listArgAttacked2 = (ArrayList<String>) mapentry.getValue();
			if (!graph1.containsKey(ArgAttack2)) {
				graph1.put(ArgAttack2, listArgAttacked2);
			}
			else {
				ArrayList<String> listArgAttacked1 = graph1.get(ArgAttack2);
		        graph1.put(ArgAttack2, Operation.union(listArgAttacked1, listArgAttacked2));
			}
		}
		
		return graph1;
	}
	
	public static ArrayList<String> union (ArrayList<String> S1, ArrayList<String> S2) {
		// Merge two arraylists without duplicates	
		Set<String> set = new LinkedHashSet<>(S1);
		set.addAll(S2);
		ArrayList<String> combinedList = new ArrayList<>(set);
		
		return combinedList;
	}
	
	public static ArrayList<String> intersection (ArrayList<String> S1, ArrayList<String> S2) {
		ArrayList<String> res = new ArrayList<>();
		for (String s: S1) {
			if (S2.contains(s)) {
				res.add(s);
			}
		}
		
		return res;
	}
	
	public static ArrayList<String> difference (ArrayList<String> S1, ArrayList<String> S2) {
		ArrayList<String> res = new ArrayList<>(S1);
		res.removeAll(S2);
		
		return res;
	}
	
	public static ArrayList<String> predecessor (HashMap<String, ArrayList<String>> graph, ArrayList<String> S, int step){
		if (step > 0) {
			Set<String> S2 = new LinkedHashSet<>();
			for (Map.Entry mapentry : graph.entrySet()) {
				String ArgAttack = (String) mapentry.getKey();
				ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
				
				for (String b: S) {
					if (listArgAttacked.contains(b)) {
						S2.add(ArgAttack);
					}
				}
			}
			
			return predecessor(graph, new ArrayList<>(S2), step-1);
		}
		else {
			return S;
		}
	}
	
	public static ArrayList<String> successor (HashMap<String, ArrayList<String>> graph, ArrayList<String> S, int step){
		if (step > 0) {
			Set<String> S2 = new LinkedHashSet<>();
			for (Map.Entry mapentry : graph.entrySet()) {
				String ArgAttack = (String) mapentry.getKey();
				ArrayList<String> listArgAttacked = (ArrayList<String>) mapentry.getValue();
				
				if (S.contains(ArgAttack)) {
					for (String b: listArgAttacked) {
						S2.add(b);
					}
				}
			}
			
			return successor(graph, new ArrayList<>(S2), step-1);
		}
		else {
			return S;
		}
	}
	
	// Vérifier si S1 est un sous-ensemble de S2
	public static boolean isSubsetOf (ArrayList<String> S1, ArrayList<String> S2) {
		boolean res = true;
		for (String s1: S1) {
			if (!S2.contains(s1)) {
				res = false;
				break;
			}
		}
		return res;
	}
}

import java.io.File; 
import java.io.FileNotFoundException; 
import java.util.Scanner;
import java.util.*;

public class ReadFile {
	
	private String nameFile;
	private int numAtk;
	
	public ReadFile(String nameFile) {
		this.nameFile = nameFile;
		this.numAtk = 0;
	}
	
	public HashMap<String, ArrayList<String>> readFile() {
		try {
			File myObj = new File(nameFile);
			Scanner myReader = new Scanner(myObj);
			HashMap<String, ArrayList<String>> graph = new HashMap<>();
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
	        	if (data.equals("ARG:")) {
	        		while (myReader.hasNextLine()) {
	        			data = myReader.nextLine();
	        			if(!data.equals(".")) {
	        				String[] words = data.substring(1).split(",");		
	        				ArrayList<String> listArg = new ArrayList<>();
	        				graph.put(words[0], listArg);
	        			}
	        			else {
	        				break;
	        			}
	        		}	
	        	}
	        	
	        	if (data.equals("ATT:")) {
	        		while (myReader.hasNextLine()) {
	        			data = myReader.nextLine();
	        			if(!data.equals(".")) {
	        				String[] words = data.substring(1).split(",");
	        				ArrayList<String> listArg = graph.get(words[1]);
	        				listArg.add(words[2]);	
	        				graph.replace(words[1], listArg);	
	        				numAtk++;
	        			}
	        			else {
	        				break;
	        			}
	        		}	
	        	}
			}
			
			/*for (Map.Entry mapentry : graph.entrySet()) {
		           System.out.println("clé: "+mapentry.getKey() 
		                              + " | valeur: " + mapentry.getValue());
		    }*/
			
			myReader.close();
			return graph;
			
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			return null;
		}	
	}
	
	public int getNumAtk() {
		return numAtk;
	}
}
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class GUI {
	private JFrame f;
	private HashMap<String, ArrayList<String>> graph1;
	private HashMap<String, ArrayList<String>> graph2;
	private String semantic;
	private int numAtk;
	private JPanel etablishRef;
	private JPanel questionRef;
	private JPanel etablishContr;
	private JPanel questionRefContr;
	private ArrayList<String> listEleChosen1;
	private ArrayList<String> listEleChosen2;
	private ArrayList<String> propChosen1;
	private ArrayList<String> propChosen2;
	private String contextualInfo1;
	private String contextualInfo2;
	private Question question;
	private Answer answer;
	private String semExtension;
	private Statement reference;
	private Statement contrast;
	
	public GUI(){
        f = new JFrame("Application");
        graph1 = new HashMap<>();
        graph2 = new HashMap<>();
        reference = new Statement(TypeStatement.REFERENCE);
        contrast = new Statement(TypeStatement.UNDEFINED);;
        question = new Question(reference, contrast);
        answer = new Answer(graph1, question);	
        semantic = "conflit-free";
		semExtension = "";
        listEleChosen1 = reference.getListEleInt();
        listEleChosen2 = contrast.getListEleInt();
        propChosen1 = new ArrayList<>();
        propChosen2 = new ArrayList<>();
        contextualInfo1 = "";
        contextualInfo2 = "";
        etablishRef = null;
        questionRef = null;
        etablishContr = null;
        questionRefContr = null;
        
        f.add(contextPage1());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1700, 950); 
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
	
	
	// Page end with buttons
	private JPanel pageEnd(JButton btnReturn, JButton btnNext) {
		// Buttons 
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		if(btnReturn != null) {
			buttons.add(btnReturn);
		}
		if(btnNext != null) {
			buttons.add(btnNext);
		}
		
		JPanel pageEnd = new JPanel(new BorderLayout());
		pageEnd.add(new JPanel(), BorderLayout.PAGE_START);
		pageEnd.add(buttons, BorderLayout.PAGE_END);
		pageEnd.setPreferredSize(new Dimension(200,100));
		return pageEnd;
	}
	
	private JPanel contextPage1() {
		JPanel viewPage = new JPanel(new BorderLayout());
		
		// Page start
		JPanel pageStart = new JPanel(new GridLayout(3,1));
		JPanel title = new JPanel();
		JLabel labelT = new JLabel("Context");
		labelT.setFont(new Font("TimesRoman", Font.PLAIN, 25));
		title.add(labelT);
		pageStart.add(new JPanel());
		pageStart.add(title);
		pageStart.setPreferredSize(new Dimension(200,100));
        viewPage.add(pageStart, BorderLayout.PAGE_START);
		
		// Center
		JPanel center = new JPanel(new GridLayout(8,1));
		JButton b1 = new JButton("Upload file of the argument framework");
		JButton b2 = new JButton("Upload file of the extension");
		JPanel panelB1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel panelB2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelB1.add(b1);
		panelB2.add(b2);
		
		JPanel cSemantic = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel choose = new JPanel();
		JLabel lChoose = new JLabel("Choose the semantic: ");
		lChoose.setFont(new Font("TimesRoman", Font.PLAIN, 18));
		choose.add(lChoose);
		JPanel box = new JPanel();
		String[] optionsToChoose = {"conflict-free", "admissible", "complete", "stable"};
		JComboBox<String> jComboBox = new JComboBox<>(optionsToChoose);
		box.add(jComboBox);
		cSemantic.add(choose);
		cSemantic.add(box);
		
		center.add(new JPanel());
		center.add(new JPanel());
		center.add(panelB1);
		center.add(panelB2);
		center.add(cSemantic);	
		viewPage.add(center, BorderLayout.CENTER);
		
		// Page left and right
		JPanel lineStart = new JPanel();
		JPanel lineEnd = new JPanel();
		lineStart.setPreferredSize(new Dimension(200,300));
		lineEnd.setPreferredSize(new Dimension(200,500));
		viewPage.add(lineStart, BorderLayout.LINE_START);
		viewPage.add(lineEnd, BorderLayout.LINE_END);
		
		// Page end	
        JButton btnNext = new JButton("Next");
		viewPage.add(pageEnd(null, btnNext), BorderLayout.PAGE_END);
		
		btnNext.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				if(graph1.isEmpty() || graph2.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Missing file !", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					f.getContentPane().removeAll();
					try {
						f.add(contextPage2());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					f.revalidate();
					f.repaint();
				}	
			}
		});
		
		b1.addActionListener(new ActionListener(){  
			JLabel patch = null;
			String filePatch = "";
			public void actionPerformed(ActionEvent e){				
				if(patch != null) {
					panelB1.remove(patch);
				}

				JFileChooser file = new JFileChooser();
				int res = file.showOpenDialog(null);
				if(res == JFileChooser.APPROVE_OPTION) {
					filePatch = file.getSelectedFile().getAbsolutePath();
					System.out.println(filePatch);
					patch = new JLabel(filePatch);
					panelB1.add(patch);
				}
				if(filePatch != "") {
					System.out.println(filePatch);
					ReadFile rf = new ReadFile(filePatch);

					graph1 = rf.readFile();
					numAtk = rf.getNumAtk();	
					answer.setGraph(graph1);
					answer.generateAF();
				}
				
				f.revalidate();
				f.repaint();
			}
		});
				
		b2.addActionListener(new ActionListener(){  
			JLabel patch = null;
			String filePatch = "";
			public void actionPerformed(ActionEvent e){				
				if(patch != null) {
					panelB2.remove(patch);
				}

				JFileChooser file = new JFileChooser();
				int res = file.showOpenDialog(null);
				if(res == JFileChooser.APPROVE_OPTION) {
					filePatch = file.getSelectedFile().getPath();
					patch = new JLabel(filePatch);
					panelB2.add(patch);
				}
				if(filePatch != null) {
					ReadFile rf = new ReadFile(filePatch);
					graph2 = rf.readFile();
				}
				
				f.revalidate();
				f.repaint();
			}
		});
		
		jComboBox.addActionListener(new ActionListener() {     
		     public void actionPerformed(ActionEvent e) {
		        semantic = jComboBox.getSelectedItem().toString();     
		        switch (semantic) {
				case "conflict-free":
					question.setSemantic(Semantic.CONFLICT_FREE);
					break;
				case "admissible":
					question.setSemantic(Semantic.ADMISSIBLE);
					break;
				case "complete":
					question.setSemantic(Semantic.COMPLETE);
					break;	
				case "stable":
					question.setSemantic(Semantic.STABLE);
					break;
				}
		     }
		});
		
		
		/*jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFruit = "You selected " + jComboBox.getItemAt(jComboBox.getSelectedIndex());
                jLabel.setText(selectedFruit);
            }
        });*/
		
		return viewPage;
	}
	
	private JPanel contextPage2() throws IOException {
		JPanel viewPage = new JPanel(new BorderLayout());
		
		// Page start
		JPanel pageStart = new JPanel(new GridLayout(3,1));
		JPanel title = new JPanel();
		JLabel labelT = new JLabel("Context");
		labelT.setFont(new Font("TimesRoman", Font.PLAIN, 25));
		title.add(labelT);
		pageStart.add(new JPanel());
		pageStart.add(title);
		pageStart.setPreferredSize(new Dimension(200,100));
        viewPage.add(pageStart, BorderLayout.PAGE_START);
		
		// Center
		JPanel center = new JPanel(new GridLayout(2,1));
		
		JPanel argGraph = new JPanel(new BorderLayout());
		JPanel paneltext1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JLabel text1 = new JLabel("Argumentation framework:");
		text1.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		paneltext1.add(text1);
		JPanel arcs = new JPanel();
		BufferedImage graph = ImageIO.read(new File("./graphviz/graph.png"));
		JLabel graphLabel = new JLabel(new ImageIcon(graph));
		arcs.add(graphLabel);

		argGraph.add(paneltext1, BorderLayout.PAGE_START);
		argGraph.add(new JPanel(), BorderLayout.LINE_START);
		argGraph.add(arcs, BorderLayout.CENTER);
		center.add(argGraph);
		
		ArrayList<String> semE = new ArrayList<>();
		JPanel paneltext2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		semExtension = "";
		for (Map.Entry mapentry : graph2.entrySet()) {
			semExtension += mapentry.getKey()+ ", ";
			semE.add((String) mapentry.getKey());
 		}
		question.setSemExtension(semE);
		semExtension = semExtension.substring(0, semExtension.length()-2);
		JLabel text2 = new JLabel("Result of the " + semantic + " extension computation: "+ "{" + semExtension + "}");	
		text2.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		paneltext2.add(text2);	
		center.add(paneltext2);
		viewPage.add(center, BorderLayout.CENTER);
		
		// Page left and right
		JPanel lineStart = new JPanel();
		JPanel lineEnd = new JPanel();
		lineStart.setPreferredSize(new Dimension(100,300));
		lineEnd.setPreferredSize(new Dimension(100,500));
		viewPage.add(lineStart, BorderLayout.LINE_START);
		viewPage.add(lineEnd, BorderLayout.LINE_END);

		// Page end
        JButton btnReturn = new JButton("Return");
        JButton btnNext = new JButton("Next");
		viewPage.add(pageEnd(btnReturn, btnNext), BorderLayout.PAGE_END);
		
		btnReturn.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				graph1.clear();
				graph2.clear();
				semExtension = "";
				f.getContentPane().removeAll();
				f.add(contextPage1());
				f.revalidate();
				f.repaint();
			}
		});
		
		btnNext.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				f.getContentPane().removeAll();
				f.add(structureQuestion());
				f.revalidate();
				f.repaint();
			}
		});
		
		return viewPage;
	}
	
	private JPanel resultE() throws IOException {
		JPanel viewPage = new JPanel(new BorderLayout());
		
		// Center
		JPanel center = new JPanel(new BorderLayout());
		center.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JPanel argGraph = new JPanel(new BorderLayout());
		JPanel paneltext1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JLabel text1 = new JLabel("Argument graph:");
		text1.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		paneltext1.add(text1);
		JPanel arcs = new JPanel();
		BufferedImage graph = ImageIO.read(new File("./graphviz/graph.png"));
		JLabel graphLabel = new JLabel(new ImageIcon(graph));
		arcs.add(graphLabel);

		argGraph.add(paneltext1, BorderLayout.PAGE_START);
		argGraph.add(new JPanel(), BorderLayout.LINE_START);
		argGraph.add(arcs, BorderLayout.CENTER);
		center.add(argGraph, BorderLayout.CENTER);
		
		JPanel paneltext2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JLabel text2 = new JLabel("Result of the " + semantic + " extension computation: "+ "{" + semExtension + "}");	
		text2.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		paneltext2.add(text2);	
		center.add(paneltext2, BorderLayout.PAGE_END);
		viewPage.add(center, BorderLayout.CENTER);
		JPanel pageEnd = new JPanel();
		pageEnd.setPreferredSize(new Dimension(0,10));
		viewPage.add(pageEnd, BorderLayout.PAGE_END);

		return viewPage;
	}
	
	
	private JPanel structureQuestion() {
		JPanel viewPage = new JPanel(new BorderLayout());
		
		// Title
		JPanel pageStart = new JPanel(new GridLayout(3,1));
        JPanel title = new JPanel();
		JLabel labelT = new JLabel("Structure of your question");
		labelT.setFont(new Font("TimesRoman", Font.PLAIN, 25));
		title.add(labelT);
		pageStart.add(new JPanel());
		pageStart.add(title);
		pageStart.setPreferredSize(new Dimension(0, 100));
		
		// Structure of question
		JPanel structureQ = new JPanel(new GridLayout(4,1));
		structureQ.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel startQ = new JPanel(new BorderLayout());	
		JPanel why = new JPanel();

		JLabel labelWhy = new JLabel("Why do we have");
		labelWhy.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		why.add(labelWhy);
		JPanel s = new JPanel();
		s.setPreferredSize(new Dimension(100,30));
		startQ.add(s, BorderLayout.PAGE_START);
		startQ.add(why, BorderLayout.CENTER);
		
		// Reference statement 
		GridLayout layoutRef = new GridLayout(1,3);
		JPanel ref = new JPanel(layoutRef);
		ref.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Reference Statement", TitledBorder.LEFT, TitledBorder.TOP));
		JPanel elementR = new JPanel();
		JPanel propertyR = new JPanel();
		JPanel contextR = new JPanel();
		elementR.setBorder(BorderFactory.createLineBorder(Color.RED));
		propertyR.setBorder(BorderFactory.createLineBorder(Color.RED));
		contextR.setBorder(BorderFactory.createDashedBorder(null, 8, 8));
		
		JLabel eR = new JLabel("Element of interest");
		JLabel cR = new JLabel("Contextual Information");
		JLabel pR = new JLabel("Property");
		eR.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		cR.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		pR.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		
		elementR.add(eR);
		propertyR.add(pR);
		contextR.add(cR);
		
		ref.add(elementR);
		ref.add(propertyR);
		ref.add(contextR);
		
		// Contrast Statement
		GridLayout layoutContr = new GridLayout(1,3);
		JPanel contr = new JPanel(layoutContr);
		contr.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Contrast Statement", TitledBorder.LEFT, TitledBorder.TOP));
		JPanel elementC = new JPanel();
		JPanel propertyC = new JPanel();
		JPanel contextC = new JPanel();
		elementC.setBorder(BorderFactory.createDashedBorder(null, 8, 8));
		propertyC.setBorder(BorderFactory.createDashedBorder(null, 8, 8));
		contextC.setBorder(BorderFactory.createDashedBorder(null, 8, 8));
		
		JLabel eC = new JLabel("Element of interest");
		JLabel cC = new JLabel("Contextual Information");
		JLabel pC = new JLabel("Property");
		eC.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		cC.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		pC.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		
		elementC.add(eC);
		propertyC.add(pC);
		contextC.add(cC);
		
		contr.add(elementC);
		contr.add(propertyC);
		contr.add(contextC);
		
		// End question	
		JPanel endQ = new JPanel();
		JPanel questionMark = new JPanel();
		JLabel lQuestionMark = new JLabel("?");
		lQuestionMark.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		questionMark.add(lQuestionMark);
		JPanel s1 = new JPanel();
		s1.setPreferredSize(new Dimension(0,80));
		endQ.add(s1, BorderLayout.PAGE_START);
		endQ.add(questionMark, BorderLayout.CENTER);
		
		// Structure of question
		structureQ.add(startQ);
		structureQ.add(ref);
		structureQ.add(contr);
		structureQ.add(endQ);
			
		// Buttons 
		JButton btnReturn = new JButton("Return");
        JButton btnNext = new JButton("Next");
		
		// Page start
		viewPage.add(pageStart, BorderLayout.PAGE_START);
		
		// Page center
		structureQ.setPreferredSize(new Dimension(400,100));
		viewPage.add(structureQ, BorderLayout.CENTER);
		
		// Page left and right
		JPanel lineStart = new JPanel();
		JPanel lineEnd = new JPanel();
		lineStart.setPreferredSize(new Dimension(100,300));
		lineEnd.setPreferredSize(new Dimension(100,500));
		viewPage.add(lineStart, BorderLayout.LINE_START);
		viewPage.add(lineEnd, BorderLayout.LINE_END);
		
		// Page end
        viewPage.add(pageEnd(btnReturn, btnNext), BorderLayout.PAGE_END);
		
		btnReturn.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				f.getContentPane().removeAll();
				try {
					f.add(contextPage2());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				f.revalidate();
				f.repaint();
			}
		});
		
		btnNext.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				f.getContentPane().removeAll();
				if (etablishRef == null) {
					try {
						f.add(etablishRef());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else {
					f.add(etablishRef);
				}
				f.revalidate();
				f.repaint();
			}
		});
			
		return viewPage;
	}
	
	// la structure de la question mise à jour à chaque fois qu'on choisit un élément
	private JPanel structureQ() {		
		// Structure of question
		JPanel structureQ = new JPanel(new BorderLayout());
		structureQ.setBorder(BorderFactory.createLineBorder(Color.black));
		
		// Why do we have
		JPanel startQ = new JPanel();
		JLabel labelWhy = new JLabel("Why do we have");
		labelWhy.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		startQ.add(labelWhy);
		
		// Reference statement 
		JPanel champ1Ref = new JPanel();
		JLabel lChamp1Ref = new JLabel("Element of interest");
		champ1Ref.add(lChamp1Ref);
		
		JPanel champ2Ref = new JPanel();
		JLabel lChamp2Ref = new JLabel("Property");
		champ2Ref.add(lChamp2Ref);
		
		JPanel champ3Ref = new JPanel();
		JLabel lChamp3Ref = new JLabel("Contextual Information");
		champ3Ref.add(lChamp3Ref);

		lChamp1Ref.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		lChamp2Ref.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		lChamp3Ref.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		
		JPanel eleIntRef = new JPanel(new BorderLayout());
		JPanel pEleRef = new JPanel();
		JLabel lEleRef = new JLabel();
		lEleRef.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		pEleRef.add(lEleRef);
		
		String eleIntTextRef = "";
    	Collections.sort(listEleChosen1);
    	for (String ele: listEleChosen1) {
    		eleIntTextRef += ele+", ";
    	}
    	if(eleIntTextRef != "") {
    		lEleRef.setText(eleIntTextRef.substring(0, eleIntTextRef.length()-2));
    	}
    	else {
    		lEleRef.setText("");
    	}
    	
		eleIntRef.add(champ1Ref, BorderLayout.PAGE_START);
		eleIntRef.add(pEleRef, BorderLayout.CENTER);
		
		JPanel propRef = new JPanel(new BorderLayout());
		JPanel pPropRef = new JPanel();
		JLabel lPropRef = new JLabel();
		lPropRef.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		pPropRef.add(lPropRef);
		
		if(!propChosen1.isEmpty()) {
			if (!reference.getNotProp()) {
	        	lPropRef.setText(propChosen1.get(0));
	        }
	        else {
	        	lPropRef.setText("not " + propChosen1.get(0));
	        }
		}
		
		propRef.add(champ2Ref, BorderLayout.PAGE_START);
		propRef.add(pPropRef, BorderLayout.CENTER);
		
		JPanel contextRef = new JPanel(new BorderLayout());
		JPanel pContextRef = new JPanel();
		JLabel lContextRef = new JLabel();
		lContextRef.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		pContextRef.add(lContextRef);
		
		if (contextualInfo1 == "in the extension S" ) {
			lContextRef.setText("in the extension " + question.semExtensionToString());
		}
		else {
			lContextRef.setText(contextualInfo1);
		}
		
		contextRef.add(champ3Ref, BorderLayout.PAGE_START);
		contextRef.add(pContextRef, BorderLayout.CENTER);
		
		GridLayout layoutRef = new GridLayout(1,3);
		JPanel ref = new JPanel(layoutRef);
		ref.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Reference Statement", TitledBorder.LEFT, TitledBorder.TOP));
		eleIntRef.setBorder(BorderFactory.createLineBorder(Color.RED));
		propRef.setBorder(BorderFactory.createLineBorder(Color.RED));
		contextRef.setBorder(BorderFactory.createDashedBorder(null, 8, 8));
	
		ref.add(eleIntRef);
		ref.add(propRef);
		ref.add(contextRef);
		
		// Contrast Statement
		JPanel champ1Contr = new JPanel();
		JLabel lChamp1Contr = new JLabel("Element of interest");
		champ1Contr.add(lChamp1Contr);
		
		JPanel champ2Contr = new JPanel();
		JLabel lChamp2Contr = new JLabel("Property");
		champ2Contr.add(lChamp2Contr);
		
		JPanel champ3Contr = new JPanel();
		JLabel lChamp3Contr = new JLabel("Contextual Information");
		champ3Contr.add(lChamp3Contr);

		lChamp1Contr.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		lChamp2Contr.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		lChamp3Contr.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		
		JPanel eleIntContr = new JPanel(new BorderLayout());
		JPanel pEleContr = new JPanel();
		JLabel lEleContr = new JLabel();
		lEleContr.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		pEleContr.add(lEleContr);
		
		String eleIntTextContr = "";
		if (listEleChosen2 != null) {
	    	Collections.sort(listEleChosen2);
	    	for (String ele: listEleChosen2) {
	    		eleIntTextContr += ele+", ";
	    	}
	    	if(eleIntTextContr != "") {
	    		lEleContr.setText(eleIntTextContr.substring(0, eleIntTextContr.length()-2));
	    	}
	    	else {
	    		lEleContr.setText("");
	    	}
		}
    	
		eleIntContr.add(champ1Contr, BorderLayout.PAGE_START);
		eleIntContr.add(pEleContr, BorderLayout.CENTER);
		
		JPanel propContr = new JPanel(new BorderLayout());
		JPanel pPropContr = new JPanel();
		JLabel lPropContr = new JLabel();
		lPropContr.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		pPropContr.add(lPropContr);
		
		if(!propChosen2.isEmpty()) {
			if (!contrast.getNotProp()) {
	        	lPropContr.setText(propChosen2.get(0));
	        }
	        else {
	        	lPropContr.setText("not " + propChosen2.get(0));
	        }
		}
		
		propContr.add(champ2Contr, BorderLayout.PAGE_START);
		propContr.add(pPropContr, BorderLayout.CENTER);
		
		JPanel contextContr = new JPanel(new BorderLayout());
		JPanel pContextContr = new JPanel();
		JLabel lContextContr = new JLabel();
		lContextContr.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		pContextContr.add(lContextContr);
		
		if (contextualInfo2 == "in the extension S'" ) {
			lContextContr.setText("in the extension " + question.semExtensionToString());
		}
		else {
			lContextContr.setText(contextualInfo2);
		}
		
		contextContr.add(champ3Contr, BorderLayout.PAGE_START);
		contextContr.add(pContextContr, BorderLayout.CENTER);
		
		GridLayout layoutContr = new GridLayout(1,3);
		JPanel contr = new JPanel(layoutContr);
		contr.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Contrast Statement", TitledBorder.LEFT, TitledBorder.TOP));
		eleIntContr.setBorder(BorderFactory.createDashedBorder(null, 8, 8));
		propContr.setBorder(BorderFactory.createDashedBorder(null, 8, 8));
		contextContr.setBorder(BorderFactory.createDashedBorder(null, 8, 8));
	
		contr.add(eleIntContr);
		contr.add(propContr);
		contr.add(contextContr);
		
		JPanel statement = new JPanel(new GridLayout(2,1));
		statement.add(ref);
		statement.add(contr);
		
		// End question	
		JPanel endQ = new JPanel();
		JLabel lQuestionMark = new JLabel("?");
		lQuestionMark.setFont(new Font("TimesRoman", Font.PLAIN, 15));
		endQ.add(lQuestionMark);
		
		// Structure of question
		structureQ.add(startQ, BorderLayout.PAGE_START);
		structureQ.add(statement, BorderLayout.CENTER);
		structureQ.add(endQ, BorderLayout.PAGE_END);
		
		return structureQ;
	}
	
	// Etablish the reference statement
	private JPanel etablishRef() throws IOException {
		JPanel viewPage = new JPanel(new BorderLayout());
		
		// Page start
		JPanel pageStart = new JPanel(new GridLayout(3,1));
		JPanel title = new JPanel();
		JLabel labelT = new JLabel("<html>Question <font color='red'>(1/4)</font></html>");
		labelT.setFont(new Font("TimesRoman", Font.PLAIN, 25));
		title.add(labelT);
		pageStart.add(new JPanel());
		pageStart.add(title);
		pageStart.setPreferredSize(new Dimension(200,100));
        viewPage.add(pageStart, BorderLayout.PAGE_START);
		
		// Center
		JPanel center = new JPanel(new BorderLayout());
		JPanel ref = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lRef = new JLabel("I. Reference statement");
		lRef.setFont(new Font("TimesRoman", Font.PLAIN, 22));
		ref.add(lRef);
		
		// Element interest
		JPanel eleInt = new JPanel(new BorderLayout());
		JPanel title1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lTitle1 = new JLabel("1. Choose your elements of interest:");
		lTitle1.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		title1.add(lTitle1);
		JPanel checkbox = new JPanel(new GridLayout(numAtk,1));
		ArrayList<JCheckBox> listCheckBox = new ArrayList<JCheckBox>();
		for (Map.Entry mapentry : graph1.entrySet()) {
			JPanel arg = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JCheckBox cb = new JCheckBox((String) mapentry.getKey());
			arg.add(cb);	
			checkbox.add(arg);
			listCheckBox.add(cb);
 		}
		
		eleInt.add(title1, BorderLayout.PAGE_START);
		eleInt.add(checkbox, BorderLayout.CENTER);
		
		// Property
		JPanel property = new JPanel(new BorderLayout());
		JPanel title2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel ltitle2 = new JLabel("2. Choose the property of your question:");
		ltitle2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		title2.add(ltitle2);
		
		JPanel bRadio = new JPanel(new GridLayout(6,1));
		ButtonGroup bg = new ButtonGroup();
		ArrayList<JRadioButton> listBtnRadio = new ArrayList<>();
		JRadioButton r1 = new JRadioButton("accepted");
		JRadioButton r2 = new JRadioButton("conflict-free");
		JRadioButton r3 = new JRadioButton("admissible");
		JRadioButton r4 = new JRadioButton("complete");
		JRadioButton r5 = new JRadioButton("stable");
		JCheckBox cbNot = new JCheckBox("add negation \"not\" of the property");
		listBtnRadio.add(r1);
		listBtnRadio.add(r2);
		listBtnRadio.add(r3);
		listBtnRadio.add(r4);
		listBtnRadio.add(r5);
		for (JRadioButton r: listBtnRadio) {
			bg.add(r);
			bRadio.add(r);
		}
		bRadio.add(cbNot);	
		
		// 3. Contextual information
		JPanel contextInfo = new JPanel(new BorderLayout());
		JPanel CI = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton btnCI = new JButton("Add the contextual information in your question");
		CI.add(btnCI);
		contextInfo.add(CI, BorderLayout.CENTER);
		
		property.add(title2, BorderLayout.PAGE_START);
		property.add(new JPanel(), BorderLayout.LINE_START);
		property.add(bRadio, BorderLayout.CENTER);
		property.add(contextInfo, BorderLayout.PAGE_END);
		
		// eleIntProp contient 4 JPanel respectivement eleInt, resultE(), property, structureQ
		JPanel eleIntProp = new JPanel(new GridLayout(2,2));
		eleIntProp.add(eleInt);
		eleIntProp.add(resultE());
		eleIntProp.add(property);
		
		// Partie struture of question à mettre à jour à chaque fois qu'on clique sur les boutons
		
	
		//JPanel structureQ = structureQ(eleIntRef, propRef, contextRef);
		ArrayList<JPanel> structureQ = new ArrayList<>();
		structureQ.add(structureQ());
		eleIntProp.add(structureQ.get(0));
		
		// Parcourir les boutons 'element of interest'
		for(JCheckBox cb: listCheckBox) {
			cb.addActionListener(new ActionListener(){
			    public void actionPerformed(ActionEvent e) {
			    	if (cb.isSelected()) {
			    		listEleChosen1.add(cb.getText());
			    	}
			    	else {
			    		if(listEleChosen1.contains(cb.getText())) {
			    			listEleChosen1.remove(cb.getText());
			    		}
			    	}
			    	
			    	// structureQ = structureQ() ne marche pas, donc il faut exécuter 3 lignes suivantes à la place
			    	eleIntProp.remove(structureQ.get(0));
			    	structureQ.set(0, structureQ());	    	
			    	eleIntProp.add(structureQ.get(0));
			        f.revalidate();
			        f.repaint();	        
			    }
			});
		}
		
		// Parcourir liste de boutons radio de 'property'		
		JButton delete = new JButton("Delete context");
		
		for (JRadioButton r: listBtnRadio) {
			r.addActionListener(new ActionListener(){
			    public void actionPerformed(ActionEvent e) {
			    	if (!propChosen1.isEmpty()) {
			    		propChosen1.remove(0);
			    	}
			    	propChosen1.add(r.getText());
			    	if (propChosen1.get(0) != "accepted") {
			    		contextualInfo1 = "";
			    	}
			        
			    	eleIntProp.remove(structureQ.get(0));
			    	structureQ.set(0, structureQ());	    	
			    	eleIntProp.add(structureQ.get(0));
			    	
			    	// Supprimer automatiquement la partie context lorsqu'on choisit une autre 'property'
			    	delete.doClick(1);
			    	
			        f.revalidate();
			        f.repaint();	
			        
			        switch (propChosen1.get(0)) {
					case "accepted":
						reference.setProperty(Property.ACCEPTED);
						break;
					case "conflict-free":
						reference.setProperty(Property.CONFLICT_FREE);
						break;
					case "admissible":
						reference.setProperty(Property.ADMISSIBLE);
						break;
					case "complete":
						reference.setProperty(Property.COMPLETE);
						break;
					case "stable":
						reference.setProperty(Property.STABLE);
						break;
					}
			    }
			});
		}
		
		cbNot.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
		    	if(reference.getProperty() != Property.UNDEFINED) {
		    		if(!propChosen1.isEmpty()) {
				        if (!cbNot.isSelected()) {
				        	reference.setNotProp(false);
		    	        }
		    	        else {    
		    	        	reference.setNotProp(true);
		    	        }
				        eleIntProp.remove(structureQ.get(0));
				    	structureQ.set(0, structureQ());	    	
				    	eleIntProp.add(structureQ.get(0));
				        f.revalidate();
				        f.repaint();	
			    	}	
		    	}
		    	else {
		    		cbNot.setSelected(false);
		    		JOptionPane.showMessageDialog(null, "No property is selected !", "Error", JOptionPane.ERROR_MESSAGE);
		    	}
		    }
    	});
			
		center.add(ref, BorderLayout.PAGE_START);
		center.add(eleIntProp, BorderLayout.CENTER);
		viewPage.add(center, BorderLayout.CENTER);
		
		// Page left and right
		JPanel lineStart = new JPanel();
		JPanel lineEnd = new JPanel();
		lineStart.setPreferredSize(new Dimension(25,300));
		lineEnd.setPreferredSize(new Dimension(25,500));
		viewPage.add(lineStart, BorderLayout.LINE_START);
		viewPage.add(lineEnd, BorderLayout.LINE_END);
		
		// Page end
        JButton btnReturn = new JButton("Return");
        JButton btnNext = new JButton("Next");
		viewPage.add(pageEnd(btnReturn, btnNext), BorderLayout.PAGE_END);
		
		btnReturn.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				f.getContentPane().removeAll();
				f.add(structureQuestion());
				f.revalidate();
				f.repaint();
			}
		});
		
		btnNext.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 			
				if (listEleChosen1.isEmpty()) {
					JOptionPane.showMessageDialog(null, "No element of interest is selected !", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (propChosen1.isEmpty()) {
					JOptionPane.showMessageDialog(null, "No property is selected !", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					f.getContentPane().removeAll();
					if (contrast.getTypeStatement() == TypeStatement.UNDEFINED) {
						try {
							f.add(etablishQuestionRef());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					else {
						try {
							f.add(etablishQuestionRefContr());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					f.revalidate();
					f.repaint();
				}		
			}
		});
		
		btnCI.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				JFrame frame = new JFrame();
				frame.setSize(600, 300);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				
				JPanel title = new JPanel(new FlowLayout(FlowLayout.LEFT));
				JLabel lTitle = new JLabel("3. Choose the contextual information:");
				lTitle.setFont(new Font("TimesRoman", Font.PLAIN, 18));
				title.add(lTitle);
				
				JPanel bRadio = new JPanel(new GridLayout(6,1));
				ButtonGroup bg = new ButtonGroup();
				JRadioButton r1 = new JRadioButton("in the extension S");
				JRadioButton r2 = new JRadioButton("conflict-free semantic");
				JRadioButton r3 = new JRadioButton("admissible semantic");
				JRadioButton r4 = new JRadioButton("complete semantic");
				JRadioButton r5 = new JRadioButton("stable semantic");
				JRadioButton r6 = new JRadioButton("in the AF(A, R)");
				if(propChosen1.isEmpty()) {
					JOptionPane.showMessageDialog(null, "No property is selected !", "Error", JOptionPane.ERROR_MESSAGE);
					frame.dispose();
				}
				else if (propChosen1.get(0) != "accepted") {
					r1.setEnabled(false);
					r2.setEnabled(false);
					r3.setEnabled(false);
					r4.setEnabled(false);
					r5.setEnabled(false);
				}
				bg.add(r1);
				bg.add(r2);
				bg.add(r3);
				bg.add(r4);
				bg.add(r5);
				bg.add(r6);
				bRadio.add(r1);
				bRadio.add(r2);
				bRadio.add(r3);
				bRadio.add(r4);
				bRadio.add(r5);
				bRadio.add(r6);
				
				JButton btnCancel = new JButton("Cancel");
		        JButton btnOK = new JButton("OK");
		        JPanel pageEnd = pageEnd(btnCancel, btnOK);
		        pageEnd.setPreferredSize(new Dimension(0,50));
				
				JPanel popup = new JPanel(new BorderLayout());
				popup.add(title, BorderLayout.PAGE_START);
				popup.add(new JPanel(), BorderLayout.LINE_START);
				popup.add(bRadio, BorderLayout.CENTER);
				popup.add(pageEnd, BorderLayout.PAGE_END);
				frame.add(popup);
				
				btnCancel.addActionListener(new ActionListener(){  
					public void actionPerformed(ActionEvent e){ 
						frame.dispose();
					}
				});
				
				btnOK.addActionListener(new ActionListener(){  	
					public void actionPerformed(ActionEvent e){
						for (Enumeration<AbstractButton> btnsRadio = bg.getElements(); btnsRadio.hasMoreElements();) {
				            AbstractButton btnRadio = btnsRadio.nextElement();
				            if (btnRadio.isSelected()) {
				            	contextualInfo1 = btnRadio.getText();
				            }
				        }
						if (contextualInfo1 == "") {
							JOptionPane.showMessageDialog(null, "No contextual information is selected !", "Error", JOptionPane.ERROR_MESSAGE);
						}
						else {
							frame.dispose();
							CI.remove(btnCI);
							
							JLabel labelCI = new JLabel("3. You added the contextual information \"" + contextualInfo1 + "\"");		
							labelCI.setFont(new Font("TimesRoman", Font.PLAIN, 19));
							CI.add(labelCI);
							
							JPanel pDelete = new JPanel(new FlowLayout(FlowLayout.LEFT));
							
							pDelete.add(delete);
							contextInfo.add(pDelete, BorderLayout.PAGE_END);

							delete.addActionListener(new ActionListener(){  
								public void actionPerformed(ActionEvent e){ 
	 								CI.remove(labelCI);
									CI.add(btnCI);
									contextInfo.remove(pDelete);
									contextualInfo1 = "";
									
									eleIntProp.remove(structureQ.get(0));
							    	structureQ.set(0, structureQ());	    	
							    	eleIntProp.add(structureQ.get(0));
									f.revalidate();
									f.repaint();
									
									reference.setContext(Context.UNDEFINED);
								}
							});
							
							eleIntProp.remove(structureQ.get(0));
					    	structureQ.set(0, structureQ());	    	
					    	eleIntProp.add(structureQ.get(0));
							f.revalidate();
							f.repaint();
						}

						switch (contextualInfo1) {
						case "in the extension S":
							reference.setContext(Context.EXTENSION_S);
							break;
						case "conflict-free semantic":
							reference.setContext(Context.CONFLICT_FREE);
							break;	
						case "admissible semantic":
							reference.setContext(Context.ADMISSIBLE);
							break;
						case "complete semantic":
							reference.setContext(Context.COMPLETE);
							break;
						case "stable semantic":
							reference.setContext(Context.STABLE);
							break;
						case "in the AF(A, R)":
							reference.setContext(Context.AF);
							break;
						}
					}
				});
				
			}
		});
		
		
		etablishRef = viewPage;
		
		return viewPage;
	}
	
	// Question with the reference statement
	private JPanel etablishQuestionRef() throws IOException {
		JPanel viewPage = new JPanel(new BorderLayout());
		
		// Page start
		JPanel pageStart = new JPanel(new GridLayout(3,1));
		JPanel title = new JPanel();
		JLabel labelT = new JLabel("<html>Question <font color='red'>(2/4)</font></html>");
		labelT.setFont(new Font("TimesRoman", Font.PLAIN, 25));
		title.add(labelT);
		pageStart.add(new JPanel());
		pageStart.add(title);
		pageStart.setPreferredSize(new Dimension(200,100));
        viewPage.add(pageStart, BorderLayout.PAGE_START);
		
		// Center
        JPanel center = new JPanel(new GridLayout(2,2));
        
		JPanel zoneQuestion = new JPanel(new BorderLayout());
		JPanel title1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lTitle1 = new JLabel("Your question is:");
		lTitle1.setFont(new Font("TimesRoman", Font.PLAIN, 19));
		title1.add(lTitle1);
		
		JPanel pQuestion = new JPanel();
		pQuestion.setBorder(BorderFactory.createLineBorder(Color.black));		
		
		question.buildQuestion();
		JLabel lQuestion = new JLabel(question.getQuestion());
		lQuestion.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		pQuestion.add(lQuestion);
		
		JPanel addContrast = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton btnAddContrast = new JButton("Add the contrast statement");
		addContrast.add(btnAddContrast);
		addContrast.setPreferredSize(new Dimension(0,180));
			
		zoneQuestion.add(title1, BorderLayout.PAGE_START);
		zoneQuestion.add(pQuestion, BorderLayout.CENTER);
		zoneQuestion.add(new JPanel(), BorderLayout.LINE_END);
		zoneQuestion.add(addContrast, BorderLayout.PAGE_END);
		center.add(zoneQuestion);
		center.add(resultE());
		center.add(new JPanel());
		
		ArrayList<JPanel> structureQ = new ArrayList<>();
		structureQ.add(structureQ());
		center.add(structureQ.get(0));
		
		viewPage.add(center, BorderLayout.CENTER);
		
		// Page left and right
		JPanel lineStart = new JPanel();
		JPanel lineEnd = new JPanel();
		lineStart.setPreferredSize(new Dimension(25,0));
		lineEnd.setPreferredSize(new Dimension(25,0));
		viewPage.add(lineStart, BorderLayout.LINE_START);
		viewPage.add(lineEnd, BorderLayout.LINE_END);
		
		// Page end
        JButton btnReturn = new JButton("Return");
        JButton btnSeeAnswer = new JButton("See the answer");
		viewPage.add(pageEnd(btnReturn, btnSeeAnswer), BorderLayout.PAGE_END);
		
		btnReturn.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				f.getContentPane().removeAll();
				f.add(etablishRef);
				f.revalidate();
				f.repaint();
			}
		});
		
		btnSeeAnswer.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				f.getContentPane().removeAll();
				try {
					f.add(AnswerPage());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				f.revalidate();
				f.repaint();
			}
		});
		
		btnAddContrast.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				f.getContentPane().removeAll();
				try {
					f.add(etablishContr());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				f.revalidate();
				f.repaint();
				
				contrast.setTypeStatement(TypeStatement.CONTRAST);
			}
		});
		
		questionRef = viewPage;
		
		return viewPage;
	}
	
	// Etablish the contrast statement
	private JPanel etablishContr() throws IOException {
		JPanel viewPage = new JPanel(new BorderLayout());
		
		// Page start
		JPanel pageStart = new JPanel(new GridLayout(3,1));
		JPanel title = new JPanel();
		JLabel labelT = new JLabel("<html>Question <font color='red'>(3/4)</font></html>");
		labelT.setFont(new Font("TimesRoman", Font.PLAIN, 25));
		title.add(labelT);
		pageStart.add(new JPanel());
		pageStart.add(title);
		pageStart.setPreferredSize(new Dimension(200,100));
        viewPage.add(pageStart, BorderLayout.PAGE_START);
		
		// Center
		JPanel center = new JPanel(new BorderLayout());
		JPanel contr = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lContr = new JLabel("II. Contrast statement");
		lContr.setFont(new Font("TimesRoman", Font.PLAIN, 22));
		contr.add(lContr);
		
		// Element interest
		JPanel eleInt = new JPanel(new BorderLayout());
		JPanel title1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lTitle1 = new JLabel("1. Choose your elements of interest:");
		lTitle1.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		title1.add(lTitle1);
		JPanel checkbox = new JPanel(new GridLayout(numAtk,1));
		ArrayList<JCheckBox> listCheckBox = new ArrayList<JCheckBox>();
		for (Map.Entry mapentry : graph1.entrySet()) {
			JPanel arg = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JCheckBox cb = new JCheckBox((String) mapentry.getKey());
			arg.add(cb);	
			checkbox.add(arg);
			listCheckBox.add(cb);
 		}
		
		eleInt.add(title1, BorderLayout.PAGE_START);
		eleInt.add(checkbox, BorderLayout.CENTER);
		
		// Property
		JPanel property = new JPanel(new BorderLayout());
		JPanel title2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel ltitle2 = new JLabel("2. Choose the property of your question:");
		ltitle2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		title2.add(ltitle2);
		
		JPanel bRadio = new JPanel(new GridLayout(7,1));
		ButtonGroup bg = new ButtonGroup();
		ArrayList<JRadioButton> listBtnRadio = new ArrayList<>();
		JRadioButton r1 = new JRadioButton("accepted");
		JRadioButton r2 = new JRadioButton("conflict-free");
		JRadioButton r3 = new JRadioButton("admissible");
		JRadioButton r4 = new JRadioButton("complete");
		JRadioButton r5 = new JRadioButton("stable");
		JCheckBox cbNot = new JCheckBox("add negation \"not\" of the property");
		JPanel pDeleteProp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton deleteProp = new JButton("Delete the property");
		pDeleteProp.add(deleteProp);
		
		listBtnRadio.add(r1);
		listBtnRadio.add(r2);
		listBtnRadio.add(r3);
		listBtnRadio.add(r4);
		listBtnRadio.add(r5);
		for (JRadioButton r: listBtnRadio) {
			bg.add(r);
			bRadio.add(r);
		}
		bRadio.add(cbNot);	
		bRadio.add(pDeleteProp);
		
		// 3. Contextual information
		JPanel contextInfo = new JPanel(new BorderLayout());
		JPanel CI = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton btnCI = new JButton("Add the contextual information in your question");
		CI.add(btnCI);
		contextInfo.add(CI, BorderLayout.CENTER);
		
		property.add(title2, BorderLayout.PAGE_START);
		property.add(new JPanel(), BorderLayout.LINE_START);
		property.add(bRadio, BorderLayout.CENTER);
		property.add(contextInfo, BorderLayout.PAGE_END);
		
		// eleIntProp contient 4 JPanel respectivement eleInt, resultE(), property, structureQ
		JPanel eleIntProp = new JPanel(new GridLayout(2,2));
		eleIntProp.add(eleInt);
		eleIntProp.add(resultE());
		eleIntProp.add(property);
		
		// Partie struture of question à mettre à jour à chaque fois qu'on clique sur les boutons
		
	
		//JPanel structureQ = structureQ(eleIntRef, propRef, contextRef);
		ArrayList<JPanel> structureQ = new ArrayList<>();
		structureQ.add(structureQ());
		eleIntProp.add(structureQ.get(0));
		
		// Parcourir les boutons element of interest
		for(JCheckBox cb: listCheckBox) {
			cb.addActionListener(new ActionListener(){
			    public void actionPerformed(ActionEvent e) {
			    	if (cb.isSelected()) {
			    		listEleChosen2.add(cb.getText());
			    	}
			    	else {
			    		if(listEleChosen2.contains(cb.getText())) {
			    			listEleChosen2.remove(cb.getText());
			    		}
			    	}
			    	
			    	// structureQ = structureQ() ne marche pas, donc il faut exécuter 3 lignes suivantes à la place
			    	eleIntProp.remove(structureQ.get(0));
			    	structureQ.set(0, structureQ());	    	
			    	eleIntProp.add(structureQ.get(0));
			        f.revalidate();
			        f.repaint();	        
			    }
			});
		}
		JButton delete = new JButton("Delete context");
		
		// Parcourir les boutons radio de 'property'
		for (JRadioButton r: listBtnRadio) {
			r.addActionListener(new ActionListener(){
			    public void actionPerformed(ActionEvent e) {
			    	if (!propChosen2.isEmpty()) {
			    		propChosen2.remove(0);
			    	}
			    	propChosen2.add(r.getText());
			        
			    	eleIntProp.remove(structureQ.get(0));
			    	structureQ.set(0, structureQ());	    	
			    	eleIntProp.add(structureQ.get(0));
			    	
			    	// Supprimer automatiquement la partie context lorsqu'on choisit un autre 'property'
			    	delete.doClick();
			    	
			        f.revalidate();
			        f.repaint();	
			        
			        switch (propChosen2.get(0)) {
					case "accepted":
						contrast.setProperty(Property.ACCEPTED);
						break;
					case "conflict-free":
						contrast.setProperty(Property.CONFLICT_FREE);
						break;
					case "admissible":
						contrast.setProperty(Property.ADMISSIBLE);
						break;
					case "complete":
						contrast.setProperty(Property.COMPLETE);
						break;
					case "stable":
						contrast.setProperty(Property.STABLE);
						break;
					}
			    }
			});
		}
		
		cbNot.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
		    	if(contrast.getProperty() != Property.UNDEFINED) {
			    	if(!propChosen2.isEmpty()) {
				        if (!cbNot.isSelected()) {
				        	contrast.setNotProp(false);
		    	        }
		    	        else {    
		    	        	contrast.setNotProp(true);
		    	        }
				        eleIntProp.remove(structureQ.get(0));
				    	structureQ.set(0, structureQ());	    	
				    	eleIntProp.add(structureQ.get(0));
				        f.revalidate();
				        f.repaint();	
			    	}	
		    	}
		    	else {
		    		cbNot.setSelected(false);
		    		JOptionPane.showMessageDialog(null, "No property is selected !", "Error", JOptionPane.ERROR_MESSAGE);
		    	}
		    }
    	});
		
		deleteProp.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
		    	bg.clearSelection();
		    	propChosen2.clear();
		    	eleIntProp.remove(structureQ.get(0));
		    	structureQ.set(0, structureQ());	    	
		    	eleIntProp.add(structureQ.get(0));
		    	cbNot.setSelected(false);
		    	f.revalidate();
		        f.repaint();
		        
		        contrast.setProperty(Property.UNDEFINED);
		        contrast.setNotProp(false);
		    }
    	});
			
		center.add(contr, BorderLayout.PAGE_START);
		center.add(eleIntProp, BorderLayout.CENTER);
		viewPage.add(center, BorderLayout.CENTER);
		
		// Page left and right
		JPanel lineStart = new JPanel();
		JPanel lineEnd = new JPanel();
		lineStart.setPreferredSize(new Dimension(25,300));
		lineEnd.setPreferredSize(new Dimension(25,500));
		viewPage.add(lineStart, BorderLayout.LINE_START);
		viewPage.add(lineEnd, BorderLayout.LINE_END);
		
		// Page end
        JButton btnReturn = new JButton("Return");
        JButton btnNext = new JButton("Next");
		viewPage.add(pageEnd(btnReturn, btnNext), BorderLayout.PAGE_END);
		
		btnReturn.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				contrast.clear();
				listEleChosen2.clear();
		        propChosen2.clear();
		        contextualInfo2 = "";
		        
				f.getContentPane().removeAll();
				if (contrast.getTypeStatement() == TypeStatement.UNDEFINED) {
					f.add(questionRef);
				}
				else {
					f.add(etablishRef);
				}
				f.revalidate();
				f.repaint();
			}
		});
		
		btnNext.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				f.getContentPane().removeAll();
				try {
					f.add(etablishQuestionRefContr());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				f.revalidate();
				f.repaint();
			}
		});
		
		btnCI.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				JFrame frame = new JFrame();
				frame.setSize(600, 300);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				
				JPanel title = new JPanel(new FlowLayout(FlowLayout.LEFT));
				JLabel lTitle = new JLabel("3. Choose the contextual information:");
				lTitle.setFont(new Font("TimesRoman", Font.PLAIN, 18));
				title.add(lTitle);
				
				JPanel bRadio = new JPanel(new GridLayout(6,1));
				ButtonGroup bg = new ButtonGroup();
				JRadioButton r1 = new JRadioButton("in the extension S'");
				JRadioButton r2 = new JRadioButton("conflict-free semantic");
				JRadioButton r3 = new JRadioButton("admissible semantic");
				JRadioButton r4 = new JRadioButton("complete semantic");
				JRadioButton r5 = new JRadioButton("stable semantic");
				JRadioButton r6 = new JRadioButton("in the AF(A', R')");
				if (!propChosen2.isEmpty() && propChosen2.get(0) != "accepted") {
					r1.setEnabled(false);
					r2.setEnabled(false);
					r3.setEnabled(false);
					r4.setEnabled(false);
					r5.setEnabled(false);
				}
				if (propChosen2.get(0) != "accepted") {
					r1.setEnabled(false);
					r2.setEnabled(false);
					r3.setEnabled(false);
					r4.setEnabled(false);
					r5.setEnabled(false);
				}
				bg.add(r1);
				bg.add(r2);
				bg.add(r3);
				bg.add(r4);
				bg.add(r5);
				bg.add(r6);
				bRadio.add(r1);
				bRadio.add(r2);
				bRadio.add(r3);
				bRadio.add(r4);
				bRadio.add(r5);
				bRadio.add(r6);
				
				JButton btnCancel = new JButton("Cancel");
		        JButton btnOK = new JButton("OK");
		        JPanel pageEnd = pageEnd(btnCancel, btnOK);
		        pageEnd.setPreferredSize(new Dimension(0,50));
				
				JPanel popup = new JPanel(new BorderLayout());
				popup.add(title, BorderLayout.PAGE_START);
				popup.add(new JPanel(), BorderLayout.LINE_START);
				popup.add(bRadio, BorderLayout.CENTER);
				popup.add(pageEnd, BorderLayout.PAGE_END);
				frame.add(popup);
				
				btnCancel.addActionListener(new ActionListener(){  
					public void actionPerformed(ActionEvent e){ 
						frame.dispose();
					}
				});
				
				btnOK.addActionListener(new ActionListener(){  	
					public void actionPerformed(ActionEvent e){
						for (Enumeration<AbstractButton> btnsRadio = bg.getElements(); btnsRadio.hasMoreElements();) {
							AbstractButton btnRadio = btnsRadio.nextElement();
				            if (btnRadio.isSelected()) {
				            	contextualInfo2 = btnRadio.getText();
				            }
				        }
						if (contextualInfo2 == "") {
							JOptionPane.showMessageDialog(null, "No contextual information is selected !", "Error", JOptionPane.ERROR_MESSAGE);
						}
						else {
							frame.dispose();
							CI.remove(btnCI);
							
							JLabel labelCI = new JLabel("3. You added the contextual information \"" + contextualInfo2 + "\"");		
							labelCI.setFont(new Font("TimesRoman", Font.PLAIN, 19));
							CI.add(labelCI);
							
							JPanel pDelete = new JPanel(new FlowLayout(FlowLayout.LEFT));
							pDelete.add(delete);
							contextInfo.add(pDelete, BorderLayout.PAGE_END);

							delete.addActionListener(new ActionListener(){  
								public void actionPerformed(ActionEvent e){ 
	 								CI.remove(labelCI);
									CI.add(btnCI);
									contextInfo.remove(pDelete);
									contextualInfo2 = "";
									
									eleIntProp.remove(structureQ.get(0));
							    	structureQ.set(0, structureQ());	    	
							    	eleIntProp.add(structureQ.get(0));
									f.revalidate();
									f.repaint();
									
									contrast.setContext(Context.UNDEFINED);
								}
							});
							
							eleIntProp.remove(structureQ.get(0));
					    	structureQ.set(0, structureQ());	    	
					    	eleIntProp.add(structureQ.get(0));
							f.revalidate();
							f.repaint();
						}
						
						switch (contextualInfo2) {
						case "in the extension S'":
							contrast.setContext(Context.EXTENSION_S2);
							break;
						case "conflict-free semantic":
							contrast.setContext(Context.CONFLICT_FREE);
							break;	
						case "admissible semantic":
							contrast.setContext(Context.ADMISSIBLE);
							break;
						case "complete semantic":
							contrast.setContext(Context.COMPLETE);
							break;
						case "stable semantic":
							contrast.setContext(Context.STABLE);
							break;
						case "in the AF(A', R')":
							contrast.setContext(Context.AF2);
							break;
						}
					}
				});
				
			}
		});
		etablishContr = viewPage;
		
		return viewPage;
	}
	
	// Question with the reference and contrast statement
	private JPanel etablishQuestionRefContr() throws IOException {
		JPanel viewPage = new JPanel(new BorderLayout());
		
		// Page start
		JPanel pageStart = new JPanel(new GridLayout(3,1));
		JPanel title = new JPanel();
		JLabel labelT = new JLabel("<html>Question <font color='red'>(4/4)</font></html>");
		labelT.setFont(new Font("TimesRoman", Font.PLAIN, 25));
		title.add(labelT);
		pageStart.add(new JPanel());
		pageStart.add(title);
		pageStart.setPreferredSize(new Dimension(200,100));
        viewPage.add(pageStart, BorderLayout.PAGE_START);
		
		// Page center
        JPanel center = new JPanel(new GridLayout(2,2));
        
		JPanel zoneQuestion = new JPanel(new BorderLayout());
		JPanel title1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lTitle1 = new JLabel("Your question is:");
		lTitle1.setFont(new Font("TimesRoman", Font.PLAIN, 19));
		title1.add(lTitle1);
		
		JPanel pQuestion = new JPanel();
		pQuestion.setBorder(BorderFactory.createLineBorder(Color.black));		
		
		question.buildQuestion();
		JLabel lQuestion = new JLabel(question.getQuestion());
		lQuestion.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		pQuestion.add(lQuestion);
		
		JPanel addContrast = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton btnDeleteContrast = new JButton("Delete the contrast statement");
		addContrast.add(btnDeleteContrast);
		addContrast.setPreferredSize(new Dimension(0,180));
			
		zoneQuestion.add(title1, BorderLayout.PAGE_START);
		zoneQuestion.add(pQuestion, BorderLayout.CENTER);
		zoneQuestion.add(new JPanel(), BorderLayout.LINE_END);
		zoneQuestion.add(addContrast, BorderLayout.PAGE_END);
		center.add(zoneQuestion);
		center.add(resultE());
		center.add(new JPanel());
		
		ArrayList<JPanel> structureQ = new ArrayList<>();
		structureQ.add(structureQ());
		center.add(structureQ.get(0));
		
		viewPage.add(center, BorderLayout.CENTER);
		
		// Page left and right
		JPanel lineStart = new JPanel();
		JPanel lineEnd = new JPanel();
		lineStart.setPreferredSize(new Dimension(25,0));
		lineEnd.setPreferredSize(new Dimension(25,0));
		viewPage.add(lineStart, BorderLayout.LINE_START);
		viewPage.add(lineEnd, BorderLayout.LINE_END);
		
		// Page end
        JButton btnReturn = new JButton("Return");
        JButton btnSeeAnswer = new JButton("See the answer");
		viewPage.add(pageEnd(btnReturn, btnSeeAnswer), BorderLayout.PAGE_END);
		
		btnReturn.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				f.getContentPane().removeAll();
				f.add(etablishContr);
				f.revalidate();
				f.repaint();
			}
		});
		
		btnDeleteContrast.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				contrast.clear();
				listEleChosen2.clear();
		        propChosen2.clear();
		        contextualInfo2 = "";
				f.getContentPane().removeAll();
				try {
					f.add(etablishQuestionRef());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				f.revalidate();
				f.repaint();
			}
		});
		
		questionRefContr = viewPage;
		
		return viewPage;
	}
	
	public JPanel AnswerPage() throws IOException {
		JPanel viewPage = new JPanel(new BorderLayout());
		
		// Page start
		JPanel pageStart = new JPanel(new GridLayout(3,1));
		JPanel title = new JPanel();
		JLabel labelT = new JLabel("Answer to your question");
		labelT.setFont(new Font("TimesRoman", Font.PLAIN, 25));
		title.add(labelT);
		pageStart.add(new JPanel());
		pageStart.add(title);
		pageStart.setPreferredSize(new Dimension(0,100));
        viewPage.add(pageStart, BorderLayout.PAGE_START);
        
        // Page center
        JPanel center = new JPanel(new BorderLayout());
        
        JPanel zoneQuestionAnswer = new JPanel(new BorderLayout());
        // Page center 1
        JPanel zoneQuestion = new JPanel(new BorderLayout());
		JPanel title1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lTitle1 = new JLabel("Your question is:");
		lTitle1.setFont(new Font("TimesRoman", Font.PLAIN, 18));
		title1.add(lTitle1);
		
		JPanel pQuestion = new JPanel();
		pQuestion.setBorder(BorderFactory.createLineBorder(Color.black));		
		
		question.buildQuestion();
		JLabel lQuestion = new JLabel(question.getQuestion());
		lQuestion.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		pQuestion.add(lQuestion);
			
		zoneQuestion.add(title1, BorderLayout.PAGE_START);
		zoneQuestion.add(pQuestion, BorderLayout.CENTER);
		zoneQuestion.add(new JPanel(), BorderLayout.LINE_END);
		
		// Page center 2
		answer.makeExplanation();
		
		JPanel zoneAnswer = new JPanel(new BorderLayout());
		JPanel title2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lTitle2 = new JLabel("Answer:");
		lTitle2.setFont(new Font("TimesRoman", Font.PLAIN, 18));
		title2.add(lTitle2);
		
		JPanel pAnswer = new JPanel();
		pAnswer.setBorder(BorderFactory.createLineBorder(Color.black));
		JLabel lAnswer = new JLabel();
		lAnswer.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		lAnswer.setText(answer.getFinalAnswer());
		pAnswer.add(lAnswer);
		zoneAnswer.add(title2, BorderLayout.PAGE_START);
		zoneAnswer.add(pAnswer, BorderLayout.CENTER);
		zoneAnswer.add(new JPanel(), BorderLayout.LINE_END);
		
		zoneQuestionAnswer.add(zoneQuestion, BorderLayout.PAGE_START);
		zoneQuestionAnswer.add(zoneAnswer, BorderLayout.CENTER);
		
		// Page center 3
		JPanel pExplanation = new JPanel(new BorderLayout());
		
		JPanel title3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lTitle3 = new JLabel("Explanation:");
		lTitle3.setFont(new Font("TimesRoman", Font.PLAIN, 18));
		title3.add(lTitle3);
		
		JPanel zoneSubgraph = new JPanel();
		
		JLabel lzoneText1 = new JLabel();
		lzoneText1.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		JLabel lzoneText2 = new JLabel();
		lzoneText2.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		JLabel lzoneText3 = new JLabel();
		lzoneText3.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		JLabel lzoneText4 = new JLabel();
		lzoneText4.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		JLabel lzoneText5 = new JLabel();
		lzoneText5.setFont(new Font("TimesRoman", Font.PLAIN, 17));

		ImageIcon iconOK = new ImageIcon("./icon/ok.png");
		iconOK.setImage(iconOK.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		ImageIcon iconNotOK = new ImageIcon("./icon/notOk.png");
		iconNotOK.setImage(iconNotOK.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		
		Property property = reference.getProperty();
		ArrayList<String> listEleInt = reference.getListEleInt();
		ArrayList<String> semExtension = question.getSemExtension();
		String listEleIntString = listArgToString(listEleInt);
		String diffS1S2 = "";
		String unionS1S2 = "";
		if (property == Property.ACCEPTED) {
			diffS1S2 = listArgToString(Operation.difference(semExtension, listEleInt));
			unionS1S2 = listArgToString(Operation.union(semExtension, listEleInt));
		}
		Boolean notPropRef = reference.getNotProp();
		Boolean cpConflictFree = answer.getCpConflictFree();
		Boolean cpDefence = answer.getCpDefence();
		Boolean cpReinstatement1 = answer.getCpReinstatement1();
		Boolean cpReinstatement2 = answer.getCpReinstatement2();
		Boolean cpComplementAttack = answer.getCpComplementAttack();
		Boolean cpTotal = answer.getcheckingProcedureTotal();
		Boolean posAcceptanceFail = answer.getPositiveAcceptanceFail();
		Boolean negAcceptanceFail = answer.getNegativeAcceptanceFail();
		
		JPanel pTitleSg1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lTitleSg1 = new JLabel("Conflict-freeness:");
		lTitleSg1.setFont(new Font("TimesRoman", Font.BOLD, 19));
		pTitleSg1.add(lTitleSg1);
		
		// green
		if (property == Property.ACCEPTED) {
			JLabel notSatisfied = new JLabel();
			if (!notPropRef && !posAcceptanceFail) {
				notSatisfied.setText("<html><font color='green'>" + diffS1S2 + " is not satisfied with conflict-freeness because of missing " + listEleIntString + "</html>");
			}
			else if (notPropRef && !negAcceptanceFail) {
				notSatisfied.setText("<html><font color='green'>" + unionS1S2 + " is not satisfied with conflict-freeness because of adding " + listEleIntString + "</html>");
			}			
			notSatisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconNotOK = new JLabel(iconNotOK);
			pTitleSg1.add(notSatisfied);
			pTitleSg1.add(lIconNotOK);
		}
		else if ((property != Property.ACCEPTED && !notPropRef && cpConflictFree)){
			JLabel satisfied = new JLabel("<html><font color='green'>" + listEleIntString + " is satisfied with conflict-freeness</html>");
			satisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconOK = new JLabel(iconOK);
			pTitleSg1.add(satisfied);
			pTitleSg1.add(lIconOK);
		}
		else if (notPropRef && !cpConflictFree){
			JLabel notSatisfied = new JLabel("<html><font color='green'>" + listEleIntString + " is not satisfied with conflict-freeness</html>");
			notSatisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconNotOK = new JLabel(iconNotOK);
			pTitleSg1.add(notSatisfied);
			pTitleSg1.add(lIconNotOK);
		}
		
		// red
		if (property != Property.ACCEPTED && !notPropRef && !cpConflictFree) {
			JLabel notSatisfied = new JLabel("<html><font color='red'>" + listEleIntString + " is not satisfied with conflict-freeness</html>");
			notSatisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconNotOK = new JLabel(iconNotOK);
			pTitleSg1.add(notSatisfied);
			pTitleSg1.add(lIconNotOK);
		}
		if (property != Property.ACCEPTED && notPropRef && cpConflictFree) {
			JLabel satisfied = new JLabel("<html><font color='red'>" + listEleIntString + " is satisfied with conflict-freeness</html>");
			satisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconOK = new JLabel(iconOK);
			pTitleSg1.add(satisfied);
			pTitleSg1.add(lIconOK);
		}
			
		JPanel pTitleSg2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lTitleSg2 = new JLabel("Defence:");
		lTitleSg2.setFont(new Font("TimesRoman", Font.BOLD, 19));
		pTitleSg2.add(lTitleSg2);
		
		// green
		if (property == Property.ACCEPTED) {
			JLabel notSatisfied = new JLabel();
			if (!notPropRef && !posAcceptanceFail) {
				notSatisfied.setText("<html><font color='green'>" + diffS1S2 + " is not satisfied with defence because of missing " + listEleIntString + "</html>");
			}
			else if (notPropRef && !negAcceptanceFail) {
				notSatisfied.setText("<html><font color='green'>" + unionS1S2 + " is not satisfied with defence because of adding " + listEleIntString + "</html>");
			}		
			notSatisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconNotOK = new JLabel(iconNotOK);
			pTitleSg2.add(notSatisfied);
			pTitleSg2.add(lIconNotOK);
		}
		else if ((property != Property.ACCEPTED && !notPropRef && cpDefence)){
			JLabel satisfied = new JLabel("<html><font color='green'>" + listEleIntString + " is satisfied with defence</html>");
			satisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconOK = new JLabel(iconOK);
			pTitleSg2.add(satisfied);
			pTitleSg2.add(lIconOK);
		}
		else if (notPropRef && !cpDefence){
			JLabel notSatisfied = new JLabel("<html><font color='green'>" + listEleIntString + " is not satisfied with defence</html>");
			notSatisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconNotOK = new JLabel(iconNotOK);
			pTitleSg2.add(notSatisfied);
			pTitleSg2.add(lIconNotOK);
		}
		
		// red
		if (property != Property.ACCEPTED && !notPropRef && !cpDefence ) {
			JLabel notSatisfied = new JLabel("<html><font color='red'>" + listEleIntString + " <font color='red'>is not satisfied with defence</html>");
			notSatisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconNotOK = new JLabel(iconNotOK);
			pTitleSg2.add(notSatisfied);
			pTitleSg2.add(lIconNotOK);
		}
		if (property != Property.ACCEPTED && notPropRef && cpDefence) {
			JLabel satisfied = new JLabel("<html><font color='red'>" + listEleIntString + " <font color='red'>is satisfied with defence</html>");
			satisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconOK = new JLabel(iconOK);
			pTitleSg2.add(satisfied);
			pTitleSg2.add(lIconOK);
		}
		
		JPanel pTitleSg3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lTitleSg3 = new JLabel("Reinstatement1:");
		lTitleSg3.setFont(new Font("TimesRoman", Font.BOLD, 19));
		pTitleSg3.add(lTitleSg3);
		
		// green
		if (property == Property.ACCEPTED) {
			JLabel notSatisfied = new JLabel();
			if (!notPropRef && !posAcceptanceFail) {
				notSatisfied.setText("<html><font color='green'>" + diffS1S2 + " is not satisfied with reinstatement1 because of missing " + listEleIntString + "</html>");
			}
			else if (notPropRef && !negAcceptanceFail) {
				notSatisfied.setText("<html><font color='green'>" + unionS1S2 + " is not satisfied with reinstatement1 because of adding " + listEleIntString + "</html>");
			}
			notSatisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconNotOK = new JLabel(iconNotOK);
			pTitleSg3.add(notSatisfied);
			pTitleSg3.add(lIconNotOK);
		}
		else if ((property != Property.ACCEPTED && !notPropRef && cpReinstatement1)){
			JLabel satisfied = new JLabel("<html><font color='green'>" + listEleIntString + " is satisfied with reinstatement1</html>");
			satisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconOK = new JLabel(iconOK);
			pTitleSg3.add(satisfied);
			pTitleSg3.add(lIconOK);
		}
		else if (notPropRef && !cpReinstatement1){
			JLabel notSatisfied = new JLabel("<html><font color='green'>" + listEleIntString + " is not satisfied with reinstatement1</html>");
			notSatisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconNotOK = new JLabel(iconNotOK);
			pTitleSg3.add(notSatisfied);
			pTitleSg3.add(lIconNotOK);
		}
		
		// red
		if (property != Property.ACCEPTED && !notPropRef && !cpReinstatement1) {
			JLabel notSatisfied = new JLabel("<html><font color='red'>" + listEleIntString + " is not satisfied with reinstatement1</html>");
			notSatisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconNotOK = new JLabel(iconNotOK);
			pTitleSg3.add(notSatisfied);
			pTitleSg3.add(lIconNotOK);
		}	
		if (property != Property.ACCEPTED && notPropRef && cpReinstatement1){
			JLabel satisfied = new JLabel("<html><font color='red'>" + listEleIntString + " is satisfied with reinstatement1</html>");
			satisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconOK = new JLabel(iconOK);
			pTitleSg3.add(satisfied);
			pTitleSg3.add(lIconOK);
		}
		
		JPanel pTitleSg4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lTitleSg4 = new JLabel("Reinstatement2:");
		lTitleSg4.setFont(new Font("TimesRoman", Font.BOLD, 19));
		pTitleSg4.add(lTitleSg4);
		
		// green
		if (property == Property.ACCEPTED) {
			JLabel notSatisfied = new JLabel();
			if (!notPropRef && !posAcceptanceFail) {
				notSatisfied.setText("<html><font color='green'>" + diffS1S2 + " is not satisfied with reinstatement2 because of missing " + listEleIntString + "</html>");
			}
			else if (notPropRef && !negAcceptanceFail) {
				notSatisfied.setText("<html><font color='green'>" + unionS1S2 + " is not satisfied with reinstatement2 because of adding " + listEleIntString + "</html>");
			}		
			notSatisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconNotOK = new JLabel(iconNotOK);
			pTitleSg4.add(notSatisfied);
			pTitleSg4.add(lIconNotOK);
		}
		else if ((property != Property.ACCEPTED && !notPropRef && cpReinstatement2)){
			JLabel satisfied = new JLabel("<html><font color='green'>" + listEleIntString + " is satisfied with reinstatement2</html>");
			satisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconOK = new JLabel(iconOK);
			pTitleSg4.add(satisfied);
			pTitleSg4.add(lIconOK);
		}
		else if (notPropRef && !cpReinstatement2){
			JLabel notSatisfied = new JLabel("<html><font color='green'>" + listEleIntString + " is not satisfied with reinstatement2</html>");
			notSatisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconNotOK = new JLabel(iconNotOK);
			pTitleSg4.add(notSatisfied);
			pTitleSg4.add(lIconNotOK);
		}
		
		// red
		if (property != Property.ACCEPTED && !notPropRef && !cpReinstatement2) {
			JLabel notSatisfied = new JLabel("<html><font color='red'>" + listEleIntString + " is not satisfied with reinstatement2</html>");
			notSatisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconNotOK = new JLabel(iconNotOK);
			pTitleSg4.add(notSatisfied);
			pTitleSg4.add(lIconNotOK);
		}	
		if (property != Property.ACCEPTED && notPropRef && cpReinstatement2) {
			JLabel satisfied = new JLabel("<html><font color='red'>" + listEleIntString + " is satisfied with reinstatement2</html>");
			satisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconOK = new JLabel(iconOK);
			pTitleSg4.add(satisfied);
			pTitleSg4.add(lIconOK);
		}
		
		JPanel pTitleSg5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lTitleSg5 = new JLabel("Complement Attack:");
		lTitleSg5.setFont(new Font("TimesRoman", Font.BOLD, 19));
		pTitleSg5.add(lTitleSg5);
		
		// green
		if (property == Property.ACCEPTED) {
			JLabel notSatisfied = new JLabel();
			if (!notPropRef && !posAcceptanceFail) {
				notSatisfied.setText("<html><font color='green'>" + diffS1S2 + " is not satisfied with complement attack because of missing " + listEleIntString + "</html>");
			}
			else if (notPropRef && !negAcceptanceFail) {
				notSatisfied.setText("<html><font color='green'>" + unionS1S2 + " is not satisfied with complement attack because of adding " + listEleIntString + "</html>");
			}
			notSatisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconNotOK = new JLabel(iconNotOK);
			pTitleSg5.add(notSatisfied);
			pTitleSg5.add(lIconNotOK);
		}
		else if ((property != Property.ACCEPTED && !notPropRef && cpComplementAttack)){
			JLabel satisfied = new JLabel("<html><font color='green'>" + listEleIntString + " is satisfied with complement attack</html>");
			satisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconOK = new JLabel(iconOK);
			pTitleSg5.add(satisfied);
			pTitleSg5.add(lIconOK);
		}
		else if (notPropRef && !cpComplementAttack){
			JLabel notSatisfied = new JLabel("<html><font color='green'>" + listEleIntString + " is not satisfied with complement attack</html>");
			notSatisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconNotOK = new JLabel(iconNotOK);
			pTitleSg5.add(notSatisfied);
			pTitleSg5.add(lIconNotOK);
		}
		
		// red
		if (property != Property.ACCEPTED && !notPropRef && !cpComplementAttack) {
			JLabel notSatisfied = new JLabel("<html><font color='red'>" + listEleIntString + " is not satisfied with complement attack</html>");
			notSatisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconNotOK = new JLabel(iconNotOK);
			pTitleSg5.add(notSatisfied);
			pTitleSg5.add(lIconNotOK);
		}	
		if (property != Property.ACCEPTED && notPropRef && cpComplementAttack) {
			JLabel satisfied = new JLabel("<html><font color='red'>" + listEleIntString + " is satisfied with complement attack</html>");
			satisfied.setFont(new Font("TimesRoman", Font.PLAIN, 17));
			JLabel lIconOK = new JLabel(iconOK);
			pTitleSg5.add(satisfied);
			pTitleSg5.add(lIconOK);
		}
		
		JPanel pSubgraph1 = new JPanel(new BorderLayout());
		pSubgraph1.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel pSubgraph2 = new JPanel(new BorderLayout());
		pSubgraph2.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel pSubgraph3 = new JPanel(new BorderLayout());
		pSubgraph3.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel pSubgraph4 = new JPanel(new BorderLayout());
		pSubgraph4.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel pSubgraph5 = new JPanel(new BorderLayout());
		pSubgraph5.setBorder(BorderFactory.createLineBorder(Color.black));
		
		BufferedImage subgraph1 = null;
		JLabel lSubgraph1 = new JLabel();
		File f1 = new File("./graphviz/conflict_free.png");
		if(f1.isFile())
		{ 
			subgraph1 = ImageIO.read(new File("./graphviz/conflict_free.png"));
			lSubgraph1 = new JLabel(new ImageIcon(subgraph1));
		}
		lzoneText1.setText(answer.getAnswerConflictFree());
		System.out.println("begin:"+pTitleSg1);
		pSubgraph1.add(pTitleSg1, BorderLayout.PAGE_START);
		pSubgraph1.add(lSubgraph1, BorderLayout.CENTER);
		pSubgraph1.add(lzoneText1, BorderLayout.PAGE_END);
		System.out.println(answer.getPositiveAcceptanceFail());
		System.out.println("end:"+lzoneText1);
		
		BufferedImage subgraph2 = null;
		JLabel lSubgraph2 = new JLabel();
		File f2 = new File("./graphviz/defence.png");
		if(f2.isFile())
		{ 
			subgraph2 = ImageIO.read(new File("./graphviz/defence.png"));
			lSubgraph2 = new JLabel(new ImageIcon(subgraph2));
		}
		lzoneText2.setText(answer.getAnswerDefence());
		pSubgraph2.add(pTitleSg2, BorderLayout.PAGE_START);
		pSubgraph2.add(lSubgraph2, BorderLayout.CENTER);
		pSubgraph2.add(lzoneText2, BorderLayout.PAGE_END);
		
		BufferedImage subgraph3 = null;
		JLabel lSubgraph3 = new JLabel();
		File f3 = new File("./graphviz/reinstatement1.png");
		if(f3.isFile())
		{ 
			subgraph3 = ImageIO.read(new File("./graphviz/reinstatement1.png"));
			lSubgraph3 = new JLabel(new ImageIcon(subgraph3));
		}
		lzoneText3.setText(answer.getAnswerReinstatement1());
		pSubgraph3.add(pTitleSg3, BorderLayout.PAGE_START);
		pSubgraph3.add(lSubgraph3, BorderLayout.CENTER);
		pSubgraph3.add(lzoneText3, BorderLayout.PAGE_END);
		
		BufferedImage subgraph4 = null;
		JLabel lSubgraph4 = new JLabel();
		File f4 = new File("./graphviz/reinstatement2.png");
		if(f4.isFile())
		{ 
			subgraph4 = ImageIO.read(new File("./graphviz/reinstatement2.png"));
			lSubgraph4 = new JLabel(new ImageIcon(subgraph4));
		}
		lzoneText4.setText(answer.getAnswerReinstatement2());
		pSubgraph4.add(pTitleSg4, BorderLayout.PAGE_START);
		pSubgraph4.add(lSubgraph4, BorderLayout.CENTER);
		pSubgraph4.add(lzoneText4, BorderLayout.PAGE_END);
		
		BufferedImage subgraph5 = null;
		JLabel lSubgraph5 = new JLabel();
		File f5 = new File("./graphviz/complement_attack.png");
		if(f5.isFile())
		{ 
			subgraph5 = ImageIO.read(new File("./graphviz/complement_attack.png"));
			lSubgraph5 = new JLabel(new ImageIcon(subgraph5));
		}
		
		lzoneText5.setText(answer.getAnswerComplementAttack());
		pSubgraph5.add(pTitleSg5, BorderLayout.PAGE_START);
		pSubgraph5.add(lSubgraph5, BorderLayout.CENTER);
		pSubgraph5.add(lzoneText5, BorderLayout.PAGE_END);
		
		switch (reference.getProperty()) {	
		case ACCEPTED:
			if (answer.getPositiveAcceptanceFail() || answer.getNegativeAcceptanceFail()) {
				JLabel text = new JLabel("No subgraph displayed !");
				text.setFont(new Font("TimesRoman", Font.PLAIN, 17));
				zoneSubgraph.add(text);
			}
			else {
				switch (question.getSemantic()) {
				case CONFLICT_FREE:	
					if (!notPropRef && !posAcceptanceFail && cpTotal) {
						JLabel text = new JLabel(listEleIntString + " is accepted but it may not be because " + diffS1S2 + " is conflict-free.");
						text.setFont(new Font("TimesRoman", Font.PLAIN, 17));
						pTitleSg1.removeAll();
						pTitleSg1.add(text);
						zoneSubgraph.add(pSubgraph1);
					}
					else if (notPropRef && !negAcceptanceFail && cpTotal) {
						JLabel text = new JLabel(listEleIntString + " is not accepted but it may be because " + unionS1S2 + " is conflict-free.");
						text.setFont(new Font("TimesRoman", Font.PLAIN, 17));
						pTitleSg1.removeAll();
						pTitleSg1.add(text);
						zoneSubgraph.add(pSubgraph1);
						
					}
					else {
						zoneSubgraph.add(pSubgraph1);
					}			
					break;
				case ADMISSIBLE:
					zoneSubgraph.setLayout(new GridLayout(1,2));	
					if (cpTotal) {					
						zoneSubgraph.add(pSubgraph1);
						zoneSubgraph.add(pSubgraph2);
					}
					else {
						if (!cpConflictFree) {
							zoneSubgraph.add(pSubgraph1);
						}
						if (!cpDefence) {
							zoneSubgraph.add(pSubgraph2);
						}
					}
					break;
				case COMPLETE:
					zoneSubgraph.setLayout(new GridLayout(2,2));
					if (cpTotal) {		
						zoneSubgraph.add(pSubgraph1);
						zoneSubgraph.add(pSubgraph2);
						zoneSubgraph.add(pSubgraph3);
						zoneSubgraph.add(pSubgraph4);
					}
					else {
						if (!cpConflictFree) {
							zoneSubgraph.add(pSubgraph1);
						}
						if (!cpDefence) {
							zoneSubgraph.add(pSubgraph2);
						}
						if (!cpReinstatement1) {
							zoneSubgraph.add(pSubgraph3);
						}
						if (!cpReinstatement2) {
							zoneSubgraph.add(pSubgraph4);
						}
					}
					break;
				case STABLE:
					zoneSubgraph.setLayout(new GridLayout(1,2));
					if (cpTotal) {
						zoneSubgraph.add(pSubgraph1);
						zoneSubgraph.add(pSubgraph5);
					}
					else {
						if (!cpConflictFree) {
							zoneSubgraph.add(pSubgraph1);
						}
						if (!cpComplementAttack) {
							zoneSubgraph.add(pSubgraph5);
						}
					}
					break;		
				default:
					break;
				}
			}
			break;
		case CONFLICT_FREE:		
			zoneSubgraph.add(pSubgraph1);
			break;
		case ADMISSIBLE:
			zoneSubgraph.setLayout(new GridLayout(1,2));	
			if (cpTotal) {					
				zoneSubgraph.add(pSubgraph1);
				zoneSubgraph.add(pSubgraph2);
			}
			else {
				if (!cpConflictFree) {
					zoneSubgraph.add(pSubgraph1);
				}
				if (!cpDefence) {
					zoneSubgraph.add(pSubgraph2);
				}
			}
			break;
		case COMPLETE:
			zoneSubgraph.setLayout(new GridLayout(2,2));
			if (cpTotal) {		
				zoneSubgraph.add(pSubgraph1);
				zoneSubgraph.add(pSubgraph2);
				zoneSubgraph.add(pSubgraph3);
				zoneSubgraph.add(pSubgraph4);
			}
			else {
				if (!cpConflictFree) {
					zoneSubgraph.add(pSubgraph1);
				}
				if (!cpDefence) {
					zoneSubgraph.add(pSubgraph2);
				}
				if (!cpReinstatement1) {
					zoneSubgraph.add(pSubgraph3);
				}
				if (!cpReinstatement2) {
					zoneSubgraph.add(pSubgraph4);
				}
			}
			break;
		case STABLE:
			zoneSubgraph.setLayout(new GridLayout(1,2));
			if (cpTotal) {
				zoneSubgraph.add(pSubgraph1);
				zoneSubgraph.add(pSubgraph5);
			}
			else {
				if (!cpConflictFree) {
					zoneSubgraph.add(pSubgraph1);
				}
				if (!cpComplementAttack) {
					zoneSubgraph.add(pSubgraph5);
				}
			}
			break;		
		default:
			break;
		}

		pExplanation.add(title3, BorderLayout.PAGE_START);
		pExplanation.add(zoneSubgraph, BorderLayout.CENTER);
		
		center.add(zoneQuestionAnswer, BorderLayout.PAGE_START);
		center.add(pExplanation, BorderLayout.CENTER);
		viewPage.add(center, BorderLayout.CENTER);
		
        // Page left and right
        JPanel lineStart = new JPanel();
		JPanel lineEnd = new JPanel();
		lineStart.setPreferredSize(new Dimension(25,0));
		lineEnd.setPreferredSize(new Dimension(25,0));
		viewPage.add(lineStart, BorderLayout.LINE_START);
		viewPage.add(lineEnd, BorderLayout.LINE_END);
		
		// Page end
        JButton btnReturn = new JButton("Return");
        JPanel pageEnd = pageEnd(btnReturn, null);
        JPanel legend = new JPanel(new GridLayout(1,4));
        legend.setBorder(BorderFactory.createLineBorder(Color.black));
                
        JPanel pBlue = new JPanel();
        ImageIcon iconBlue = new ImageIcon("./icon/blue.png");
        iconBlue.setImage(iconBlue.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		JLabel lIconBlue = new JLabel(iconBlue);
        JLabel lblue = new JLabel("<html><font color = 'blue'>: Argument of interest</font></html>");
        lblue.setFont(new Font("TimesRoman", Font.PLAIN, 14));
        pBlue.add(lIconBlue);
        pBlue.add(lblue);
        legend.add(pBlue);
        
        JPanel pRed = new JPanel();
        ImageIcon iconRed = new ImageIcon("./icon/red.png");
        iconRed.setImage(iconRed.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		JLabel lIconRed = new JLabel(iconRed);
        JLabel lred = new JLabel("<html><font color = 'red'>: Argument/arc makes checking procedure fail</font></html>");
        lred.setFont(new Font("TimesRoman", Font.PLAIN, 14));
        pRed.add(lIconRed);
        pRed.add(lred);
        legend.add(pRed);
        
        JPanel pGrey = new JPanel();
        ImageIcon iconGrey = new ImageIcon("./icon/grey.png");
        iconGrey.setImage(iconGrey.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		JLabel lIconGrey = new JLabel(iconGrey);
        JLabel lGrey = new JLabel("<html><font color = 'gray'>: Argument/arc not concerned</font></htmlk>");
        lGrey.setFont(new Font("TimesRoman", Font.PLAIN, 14));
        pGrey.add(lIconGrey);
        pGrey.add(lGrey);
        legend.add(pGrey);
        
        JPanel pBlack = new JPanel();
        ImageIcon iconBlack = new ImageIcon("./icon/black.png");
        iconBlack.setImage(iconBlack.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		JLabel lIconBlack = new JLabel(iconBlack);
        JLabel lblack = new JLabel(": Others");
        lblack.setFont(new Font("TimesRoman", Font.PLAIN, 14));
        pBlack.add(lIconBlack);
        pBlack.add(lblack);
        legend.add(pBlack);
        
        JPanel lineStart2 = new JPanel();
		JPanel lineEnd2 = new JPanel();
		lineStart2.setPreferredSize(new Dimension(25,0));
		lineEnd2.setPreferredSize(new Dimension(100,0));
		pageEnd.add(lineStart2, BorderLayout.LINE_START);
		pageEnd.add(lineEnd2, BorderLayout.LINE_END);
        pageEnd.add(legend, BorderLayout.CENTER);
        
		viewPage.add(pageEnd, BorderLayout.PAGE_END);
				
		btnReturn.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				f.getContentPane().removeAll();
				f.add(questionRef);
				f.revalidate();
				f.repaint();
			}
		});
        
		return viewPage;
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
}

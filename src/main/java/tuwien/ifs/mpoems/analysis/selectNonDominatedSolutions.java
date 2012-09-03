package tuwien.ifs.mpoems.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;

import tuwien.ifs.mpoems.Solution;
import tuwien.ifs.mpoems.SolutionSet;

public class selectNonDominatedSolutions {

	private static int nObjectives;
	private static int [] objectivesMaxMin;
	private static String inputFile;
	private static String outputFile;
	private static PrintWriter writer;
	
	private static SolutionSet printedSolutions = new SolutionSet();
	/**
	 * initialize / read the properties for the framework
	 */
	private static void readConfiguration(Properties properties) throws Exception{
		Enumeration<Object> keys = properties.keys();
	    while (keys.hasMoreElements()) {
	      String key = (String)keys.nextElement();
	      String value = (String)properties.get(key);
	      System.out.println(key + ": " + value);
	    }
	    
	    System.out.println("***configuration***");
	    
	    nObjectives = Integer.parseInt( (String)properties.get( "nObjectives" ) );
	    objectivesMaxMin = new int [nObjectives];
	    try{
		    for(int i = 1; i <= nObjectives; i++){
		    	int value = Integer.parseInt( (String)properties.get( "objective" + i ) );
		    	if(value > 1 | value < 0 ) throw new Exception("check configuration: configvalue: objective"+ i +" must be either 0 or 1");
		    	else objectivesMaxMin[i-1] = value;	
		    }
	    }
	    catch(NumberFormatException e){
	    	throw new Exception("check configuration: configvalue: please state objectiveN where N is in [1 till nObjectives], for example: nObjective is 2.. config value objective1 has to be 0 or 1 and 0 is min and 1 is max");
	    }
	    
	    inputFile = (String)properties.get( "inputFile" );
	    outputFile = (String)properties.get( "outputFile" ); 
	    
	  //erase next two lines after testing...
		inputFile="mpoems_output_run_merge.txt";
		outputFile="mpoems_output_run_thomas.frt";
		
	    if(nObjectives < 1)throw new Exception("check configuration: configvalue: nObjectives must be greater than 0");
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			Properties properties = new Properties();
		    properties.load(selectNonDominatedSolutions.class.getResourceAsStream("selectNonDominatedSolutionsConf.cfg"));
		    readConfiguration(properties);
		    
		    
			SolutionSet set = readSolutionSetFromFileScanner();
			set.assignFront(null);
			System.out.println("Read SolutionSet has a size of " + set.size());
			
			Iterator<Solution> itr = set.iterator();
			int ndCounter = 0;
			int duplicates = 0;
			writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)), true);
			for (int i = 0; i < objectivesMaxMin.length; i++) {
				writer.print(objectivesMaxMin[i]);
				if(i != (objectivesMaxMin.length-1))writer.print(" ");
			}
			
			/*System.out.println("size of set is" + set.size());
			double [] arr = set.get(14997).getObjectives();
			for (int i = 0; i < nObjectives; i++) {
				System.out.println(arr[i]);
				
			}
			System.exit(0);*/
			int s = 0;
			while (itr.hasNext()) {
				GenericSolution solution = (GenericSolution)itr.next();
				
				//print only non-dominated front
				if(solution.getFront()==1){
					//to not print duplicates
					if(NotWrittenAlready(solution)){
						writer.println("");
						ndCounter++;
						writeSolutionToFile(solution);
					}
					else{
						duplicates++;
					}
				}
				s++;
			}
			writer.close();	
			System.out.println("Read SolutionSet has " + ndCounter + " non-dominated solutions");
			System.out.println("the whole set had " + (ndCounter + duplicates ) + " solutions");
			System.out.println("whereas "+ duplicates + " duplicates were removed");
			System.out.println("wrote output file " + outputFile + " with " + (ndCounter - duplicates ) + " solutions");
		}
		catch(Exception e){
			System.out.println("Exception was catched");
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
	
	private static SolutionSet readSolutionSetFromFileScanner() {
		
		SolutionSet set = new SolutionSet();
		try{			
			FileReader fin = new FileReader(inputFile);
			Scanner src = new Scanner(fin);
		
			while (src.hasNext()) {
				
				int i = 0;
				GenericSolution solution = new GenericSolution();
				solution.setConfig(nObjectives, objectivesMaxMin);
				System.out.println("------------------------");
				while(i < nObjectives){
					//double value = src.nextDouble();
					double value = Double.parseDouble(src.next());
					solution.setObjectives(i,value);
					i++;
				}
				if(i==nObjectives){
					/*double [] values = solution.getObjectives();
					for(int z=0;z<values.length;z++){
						System.out.println(values[z]);
					}*/
					if(solution.objectivesSetOK)set.add(solution);	
				}
				else throw new Exception("Check Input File - Not " + nObjectives + " Objectives in a line");	
			}
			return set;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	// streamtokenizer is buggy. does not read the value 0.9624999999999999 correctly. does round it up
	//to 0.9625
	private static SolutionSet readSolutionSetFromFile() {
		
		SolutionSet set = new SolutionSet();
		try{			
			FileReader reader = new FileReader(new File(inputFile));
			StreamTokenizer tokenStream = new StreamTokenizer(reader);
			//recognize end of lines as separated tokens. ttype will be set to TT_EOL
			tokenStream.eolIsSignificant(true);
			tokenStream.nextToken(); // get the first token
			String flag = (String) tokenStream.sval;
			
			int testcounter = 0;
			while(flag.equals("START")){
				GenericSolution solution = new GenericSolution();
				solution.setConfig(nObjectives, objectivesMaxMin);
				System.out.println("------------------------");
				for (int i = 0; i < nObjectives; i++) {
					
					if(tokenStream.nextToken() == tokenStream.TT_NUMBER){
						solution.setObjectives(i,(Double)tokenStream.nval);
						System.out.println((Double)tokenStream.nval);
					}
					else throw new Exception("Check Input File - Not " + nObjectives + " Objectives in line " + tokenStream.lineno());	
					
				}
				
				
				
				if(solution.objectivesSetOK)set.add(solution);	
				
				testcounter++;
				if(testcounter==15000){
					System.out.println("you got me");
					System.out.println("size of set is" + set.size());
					double [] arr = set.get(14999).getObjectives();
					for (int i = 0; i < nObjectives; i++) {
						System.out.println(arr[i]);
						
					}
					System.exit(0);
				}
				// check for END token...
				if(tokenStream.nextToken() == tokenStream.TT_WORD){
					if (tokenStream.sval.equals("END")) {
						flag = (String)tokenStream.sval;
					}
				}
			}
			
			return set;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void readFromFileTestNumberProblem() {
		
		try{			
			FileReader reader = new FileReader(new File("test.txt"));
			StreamTokenizer tokenStream = new StreamTokenizer(reader);
			//recognize end of lines as separated tokens. ttype will be set to TT_EOL
			tokenStream.eolIsSignificant(true);
			tokenStream.nextToken(); // get the first token
			String flag = (String) tokenStream.sval;
			tokenStream.wordChars('0', '9');
			while(flag.equals("START")){
				System.out.println("------------------------");
				for (int i = 0; i < 5; i++) {
					tokenStream.nextToken();
					/*if(tokenStream.nextToken() == tokenStream.TT_NUMBER){
						System.out.println(tokenStream.nval);
					}
					
					else throw new Exception("Check Input File - Not " + nObjectives + " Objectives in line " + tokenStream.lineno());	
					*/
					System.out.println(tokenStream.nval);
					//System.out.println(tokenStream.toString());
					
				}
				// check for END token...
				if(tokenStream.nextToken() == tokenStream.TT_WORD){
					if (tokenStream.sval.equals("END")) {
						flag = (String)tokenStream.sval;
					}
				}
			}
			
			float test1 = 0.9624999999999999F;
			double test2 = 0.9624999999999999;
			System.out.println("float: " + test1 + " double: " + test2); 
			
			double test3 = Double.parseDouble("0.9624999999999999");
			System.out.println(test3); 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void writeSolutionToFile(GenericSolution solution) throws Exception {
		double[] objectives = solution.getObjectives();
		for (int i = 0; i < nObjectives; i++) {
			writer.print(objectives[i]);
			if(i != (nObjectives-1))writer.print(" ");
		}
		rememberSolution(solution);
	}
	
	private static void rememberSolution(Solution s){
		printedSolutions.add(s);
	}
	
	private static boolean NotWrittenAlready(Solution s){
		Iterator<Solution> itr = printedSolutions.iterator();
		double[] s_objectives = s.getObjectives();
		boolean check = true;
		while (itr.hasNext() && check==true) {
			GenericSolution solution = (GenericSolution)itr.next();
			
			double[] solution_objectives = solution.getObjectives();
			boolean checkObjectives = true;
			int i = 0;
			while(i<solution_objectives.length && checkObjectives==true){
				if(s_objectives[i]!=solution_objectives[i])checkObjectives=false;
				i++;
			}
			if(checkObjectives)check=false;
		}
		return check;
	}
}

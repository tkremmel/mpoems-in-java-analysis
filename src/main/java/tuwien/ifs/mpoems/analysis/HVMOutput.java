package tuwien.ifs.mpoems.analysis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Vector;

import tuwien.ifs.mpoems.Solution;
import tuwien.ifs.mpoems.SolutionSet;
import tuwien.ifs.mpoems.ppds.dataStructure.*;
import tuwien.ifs.mpoems.ppds.ProjectSelectionSolution;

public class HVMOutput {
	
	public HVMOutput(){}
	
	public void writeHVMFile(SolutionSet Population,String hvm_output_file) throws Exception{
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(hvm_output_file)), true);
		Iterator<Solution> itr = Population.iterator();
		while (itr.hasNext()) {
			ProjectSelectionSolution element = (ProjectSelectionSolution)itr.next();
			
			//print only non-dominated front
			if(element.getFront()==1){
				double [] objectives = element.getObjectives();
				for (int j = 0; j < objectives.length; j++) {
                   // writer.printf("%10.10f ",objectives[j]);
					//add 160000 to the synergy objective in order that we do not have negative synergy objectives
					//important for hvm calculation
					if(j==(objectives.length-1)){
						writer.print((objectives[j]+160000));
						writer.print(" ");
					}
					else{
						writer.print(objectives[j]);
						writer.print(" ");
					}
                }
				writer.println();
			}
		}
		writer.close();
	}
}

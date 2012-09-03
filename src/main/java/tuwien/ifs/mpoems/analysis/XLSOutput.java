package tuwien.ifs.mpoems.analysis;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.io.*;
import jxl.*;
import java.util.*;
import jxl.Workbook;
import jxl.write.Number;

import jxl.write.*;

import tuwien.ifs.mpoems.Solution;
import tuwien.ifs.mpoems.SolutionSet;
import tuwien.ifs.mpoems.ppds.dataStructure.*;
import tuwien.ifs.mpoems.ppds.ProjectSelectionSolution;

/**
 * @author Thomas
 *
 */
public class XLSOutput {
	
		private WorkbookSettings ws;
		private WritableWorkbook workbook;
		private WritableSheet s;
		private int sheetNumber = 0;
		
		public XLSOutput(){
		}
		
		//public void createXLS(SolutionSet result, String filename)
		public void createXLS(String filename){
		    try{
		      ws = new WorkbookSettings();
		      ws.setLocale(new Locale("en", "EN"));
		      workbook = 
		        Workbook.createWorkbook(new File(filename), ws);
		     
		      //writeDataSheet(s);     
		    }
		    catch (Exception e){
		      e.printStackTrace();
		    }
		}
	
		public void createSheet(String sheetname){
			 s = workbook.createSheet(sheetname, sheetNumber);
			 
			 for(int i = 1; i < 22; i++){
				 s.setColumnView(i, 16); 
			 }
			 
			 sheetNumber++;
		}
		
		public void writeWorkbook() throws Exception{
			workbook.write();
		}
		
		public void closeWorkbook() throws Exception{
			workbook.close(); 
		}
	
		 
		public void writeDataSheet(Vector<SolutionSet> allBases) throws Exception{
			//set cell formats

			WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
			WritableFont wf_white = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,  jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.WHITE);
			WritableCellFormat cf_Arial = new WritableCellFormat(wf);
			WritableCellFormat cf_Arial_dark_blue = new WritableCellFormat(wf_white);
			cf_Arial_dark_blue.setBackground(jxl.format.Colour.DARK_BLUE);
			cf_Arial.setWrap(false);
			
			//WritableCellFormat cf_NumberFloat = new WritableCellFormat(NumberFormats.FLOAT);
			NumberFormat nf_3digits = new NumberFormat("#.#########");
		    WritableCellFormat cf_3digits = new WritableCellFormat(nf_3digits);
		    Number n;
			int numberObjectives = 0;
			int numberCategorys = 0;
			for(int i = 0; i < allBases.size(); i++){
				SolutionSet sS = allBases.get(i);
				Iterator<Solution> itr = sS.iterator();
				createSheet("SolutionBase" + i);
				
				int row = 1;
				int ndCounter = 0;
				while (itr.hasNext()) {
					ProjectSelectionSolution element = (ProjectSelectionSolution)itr.next();
					
					//print only non-dominated front
					if(element.getFront()==1 | i == 0){
						ndCounter++;
						double [] timeframeResourceConsumptionFinal = element.getTimeframeResourceConsumptionFinal();
						double [] objectives = element.getObjectives();
						int [] categoryCountFinal = element.getCategoryCountFinal();
						numberCategorys = categoryCountFinal.length;
						double [] objectivesArray = element.getObjectivesArray();
						//print labels
						if(row == 1){
							for(int e = 0; e < timeframeResourceConsumptionFinal.length; e++ ){
								Label l = new Label(e,row-1,"Timeframe " + (e+1) +" RC",cf_Arial_dark_blue);
							    s.addCell(l);
							}
							for(int e = 0; e < objectives.length; e++) {
								Label l = null;
								if(e==0){
									l = new Label((timeframeResourceConsumptionFinal.length + 1 + e),row-1,"Potential Revenue",cf_Arial_dark_blue);
								} 
								if(e==1){
									l = new Label((timeframeResourceConsumptionFinal.length + 1 + e),row-1,"SAV",cf_Arial_dark_blue);
								} 
								if(e==2){
									l = new Label((timeframeResourceConsumptionFinal.length + 1 + e),row-1,"RUDM",cf_Arial_dark_blue);
								} 
								if(e==3){
									l = new Label((timeframeResourceConsumptionFinal.length + 1 + e),row-1,"Risk",cf_Arial_dark_blue);
								} 
								if(e==4){
									l = new Label((timeframeResourceConsumptionFinal.length + 1 + e),row-1,"SumSynergy",cf_Arial_dark_blue);
								} 
								if(e==5){
									l = new Label((timeframeResourceConsumptionFinal.length + 1 + e),row-1,"sumCategoryDeviation",cf_Arial_dark_blue);
								}
							    s.addCell(l);  
							}
							for(int e = 0; e < categoryCountFinal.length; e++) {
								Label l = new Label((timeframeResourceConsumptionFinal.length + objectives.length + 2 + e),row-1,"Category" + (e+1) + " Count",cf_Arial_dark_blue);
							    s.addCell(l);  
							}
							for(int e = 0; e < objectivesArray.length; e++) {
								Label l = null;
								if(e==0){
									l = new Label((timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectives.length + 3 + e),row-1,"objectivePotentialRevenue",cf_Arial_dark_blue);
								}
								if(e==1){
									l = new Label((timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectives.length + 3 + e),row-1,"objectiveSAV",cf_Arial_dark_blue);
								}
								if(e==2){
									l = new Label((timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectives.length + 3 + e),row-1,"rudm",cf_Arial_dark_blue);
								}
								if(e==3){
									l = new Label((timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectives.length + 3 + e),row-1,"objectiveRisk",cf_Arial_dark_blue);
								}
								if(e==4){
									l = new Label((timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectives.length + 3 + e),row-1,"objectivePositiveSynergy",cf_Arial_dark_blue);
								}
								if(e==5){
									l = new Label((timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectives.length + 3 + e),row-1,"objectiveNegativeSynergy",cf_Arial_dark_blue);
								}
								if(e==6){
									l = new Label((timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectives.length + 3 + e),row-1,"sumCategoryDeviation",cf_Arial_dark_blue);
								}
								if(e==7){
									l = new Label((timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectives.length + 3 + e),row-1,"Number Selected Projects",cf_Arial_dark_blue);
								}
							    s.addCell(l);  
							}
						}
						//print timeframeresource consumption
						for(int e = 0; e < timeframeResourceConsumptionFinal.length; e++ ){
						    n = new Number(e,row,timeframeResourceConsumptionFinal[e],cf_3digits);
						    s.addCell(n);
						}
						
						//print objectives
						numberObjectives = objectives.length;
						for(int e = 0; e < objectives.length; e++) {
							n = new Number((timeframeResourceConsumptionFinal.length + 1 + e),row,objectives[e],cf_3digits);
						    s.addCell(n);  
						}
						
						//print categorycount
						for(int e = 0; e < categoryCountFinal.length; e++) {
							n = new Number((timeframeResourceConsumptionFinal.length + objectives.length + 2 + e),row,categoryCountFinal[e],cf_3digits);
						    s.addCell(n);  
						}
						
						//print objectivesArray
						for(int e = 0; e < objectivesArray.length; e++) {
							n = new Number((timeframeResourceConsumptionFinal.length + objectives.length + categoryCountFinal.length + 3 + e),row,objectivesArray[e],cf_3digits);
						    s.addCell(n);  
						}
						
						
						int position = objectives.length + timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectivesArray.length + 5;
						//print the solutionvector for each solution
						int[] solutionVector = element.getSolutionVector();
						for(int e = 0; e < solutionVector.length; e++) {
							int m = solutionVector[e];
							//if(m>0)m = 1; //only show if project is selected or not
							n = new Number((position + e),row,m);
						    s.addCell(n);  
						    
						    if(row == 1){
							    Label l = new Label((position + e),row-1,"P" + (e+1),cf_Arial_dark_blue);
							    s.addCell(l); 
						    }
						   /* if(e == 0){
						    	Formula f = new Formula(objectives.length + timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectivesArray.length + 4 + e ,row, "SUM(AA" + (row+1) + ":BD" + (row+1) + ")");
						    	s.addCell(f);
						    }*/
						}
						
						position = objectives.length + timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectivesArray.length + 6 + solutionVector.length;
						
						//print the solutionvector for each solution
						//print only if project is selected or not
						solutionVector = element.getSolutionVector();
						for(int e = 0; e < solutionVector.length; e++) {
							int m = solutionVector[e];
							if(m>0)m = 1; //only show if project is selected or not
							n = new Number((position + e),row,m);
						    s.addCell(n);  
						    
						    if(row == 1){
							    Label l = new Label((position + e),row-1,"P" + (e+1),cf_Arial_dark_blue);
							    s.addCell(l); 
						    }
						   /* if(e == 0){
						    	Formula f = new Formula(objectives.length + timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectivesArray.length + 4 + e ,row, "SUM(AA" + (row+1) + ":BD" + (row+1) + ")");
						    	s.addCell(f);
						    }*/
						}
						
						//if(row==sS.size() && i==1){
						if(row==sS.size()){
							ProjectPool pp = element.getProjectPool();
						
							for(int e = 0; e < pp.getProjectPoolSize(); e++) {
								Project p = pp.getProject(e);
								n = new Number((objectives.length + timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectivesArray.length + 5 + e),row+2,p.getRisk());
							    s.addCell(n);  
							    n = new Number((objectives.length + timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectivesArray.length + 5 + e),row+4,p.getSav());
							    s.addCell(n); 
							    n = new Number((objectives.length + timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectivesArray.length + 5 + e),row+6,p.getPotentialRevenue());
							    s.addCell(n);  
							    
							    Hashtable synergy = p.getSynergy_positive();
								if(synergy.size() > 0){
									Enumeration keys = synergy.keys();
									while ( keys.hasMoreElements() )
									   {
									   int key = (Integer)keys.nextElement();
									   
									   n = new Number((objectives.length + timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectivesArray.length + 5 + e),row+8,(key+1));
									   s.addCell(n);  
									   
									   int synergy_value = (Integer)synergy.get( key );
									   n = new Number((objectives.length + timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectivesArray.length + 5 + e),row+9,synergy_value);
									   s.addCell(n);  
									 }
								}
								
								synergy = p.getSynergy_negative();
								if(synergy.size() > 0){
									Enumeration keys = synergy.keys();
									while ( keys.hasMoreElements() )
									   {
									   int key = (Integer)keys.nextElement();
									   
									   n = new Number((objectives.length + timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectivesArray.length + 5 + e),row+11,(key+1));
									   s.addCell(n);  
									   
									   int synergy_value = (Integer)synergy.get( key );
									   n = new Number((objectives.length + timeframeResourceConsumptionFinal.length + categoryCountFinal.length + objectivesArray.length + 5 + e),row+12,synergy_value);
									   s.addCell(n);  
									 }
								}
								
							    
							}
						}
						row++;
					}
				}
				
				Formula nd_Count = new Formula(0,row+8, "" + (ndCounter));
				s.addCell(nd_Count);
				System.out.println(i + ";" + ndCounter);
				
				for(int e = 0; e < (numberObjectives+ 6 + numberCategorys + 8); e++ ){
					Formula f = null;
					Formula g = null;
					Formula h = null;
					Formula j = null;
					//System.out.println("XLSOutput: numberObjectives is = " + numberObjectives);
					//System.out.println("XLSOutput: e is = " + e);
					if(e==0){
						f = new Formula(e,row+1, "SUM(A2:A" + (row));
						g = new Formula(e,row+2, "A"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(A2:A" + (row));
						j = new Formula(e,row+5, "MIN(A2:A" + (row));
					}
					if(e==1){
						f = new Formula(e,row+1, "SUM(B2:B" + (row));
						g = new Formula(e,row+2, "B"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(B2:B" + (row));
						j = new Formula(e,row+5, "MIN(B2:B" + (row));
					}
					if(e==2){
						f = new Formula(e,row+1, "SUM(C2:C" + (row));
						g = new Formula(e,row+2, "C"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(C2:C" + (row));
						j = new Formula(e,row+5, "MIN(C2:C" + (row));
					}
					if(e==4){
						f = new Formula(e,row+1, "SUM(E2:E" + (row));
						g = new Formula(e,row+2, "E"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(E2:E" + (row));
						j = new Formula(e,row+5, "MIN(E2:E" + (row));
					}
					if(e==5){
						f = new Formula(e,row+1, "SUM(F2:F" + (row));
						g = new Formula(e,row+2, "F"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(F2:F" + (row));
						j = new Formula(e,row+5, "MIN(F2:F" + (row));
					}
					if(e==6){
						f = new Formula(e,row+1, "SUM(G2:G" + (row));
						g = new Formula(e,row+2, "G"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(G2:G" + (row));
						j = new Formula(e,row+5, "MIN(G2:G" + (row));
					}
					if(e==7){
						f = new Formula(e,row+1, "SUM(H2:H" + (row));
						g = new Formula(e,row+2, "H"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(H2:H" + (row));
						j = new Formula(e,row+5, "MIN(H2:H" + (row));
					}
					if(e==8){
						f = new Formula(e,row+1, "SUM(I2:I" + (row));
						g = new Formula(e,row+2, "I"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(I2:I" + (row));
						j = new Formula(e,row+5, "MIN(I2:I" + (row));
					}
					if(e==9){
						f = new Formula(e,row+1, "SUM(J2:J" + (row));
						g = new Formula(e,row+2, "J"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(J2:J" + (row));
						j = new Formula(e,row+5, "MIN(J2:J" + (row));
					}
					if(e==10){
						f = new Formula(e,row+1, "SUM(K2:K" + (row));
						g = new Formula(e,row+2, "K"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(K2:K" + (row));
						j = new Formula(e,row+5, "MIN(K2:K" + (row));
					}
					if(e==11){
						f = new Formula(e,row+1, "SUM(L2:L" + (row));
						g = new Formula(e,row+2, "L"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(L2:L" + (row));
						j = new Formula(e,row+5, "MIN(L2:L" + (row));
					}
					if(e==12){
						f = new Formula(e,row+1, "SUM(M2:M" + (row));
						g = new Formula(e,row+2, "M"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(M2:M" + (row));
						j = new Formula(e,row+5, "MIN(M2:M" + (row));
					}
					if(e==13){
						f = new Formula(e,row+1, "SUM(N2:N" + (row));
						g = new Formula(e,row+2, "N"+ (row+2)+ "/" + (row-1));
						//h = new Formula(e,row+4, "MAX(N2:N" + (row));
						//j = new Formula(e,row+5, "MIN(N2:N" + (row));
					}
					if(e==14){
						f = new Formula(e,row+1, "SUM(O2:O" + (row));
						g = new Formula(e,row+2, "O"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(O2:O" + (row));
						j = new Formula(e,row+5, "MIN(O2:O" + (row));
					}
					if(e==15){
						f = new Formula(e,row+1, "SUM(P2:P" + (row));
						g = new Formula(e,row+2, "P"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(P2:P" + (row));
						j = new Formula(e,row+5, "MIN(P2:P" + (row));
					}
					if(e==16){
						f = new Formula(e,row+1, "SUM(Q2:Q" + (row));
						g = new Formula(e,row+2, "Q"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(Q2:Q" + (row));
						j = new Formula(e,row+5, "MIN(Q2:Q" + (row));
					}
					if(e==17){
						f = new Formula(e,row+1, "SUM(R2:R" + (row));
						g = new Formula(e,row+2, "R"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(R2:R" + (row));
						j = new Formula(e,row+5, "MIN(R2:R" + (row));
					}
					if(e==18){
						f = new Formula(e,row+1, "SUM(S2:S" + (row));
						g = new Formula(e,row+2, "S"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(S2:S" + (row));
						j = new Formula(e,row+5, "MIN(S2:S" + (row));
					}
					if(e==19){
						f = new Formula(e,row+1, "SUM(T2:T" + (row));
						g = new Formula(e,row+2, "T"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(T2:T" + (row));
						j = new Formula(e,row+5, "MIN(T2:T" + (row));
					}
					if(e==20){
						f = new Formula(e,row+1, "SUM(U2:U" + (row));
						g = new Formula(e,row+2, "U"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(U2:U" + (row));
						j = new Formula(e,row+5, "MIN(U2:U" + (row));
					}
					if(e==21){
						f = new Formula(e,row+1, "SUM(V2:V" + (row));
						g = new Formula(e,row+2, "V"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(V2:V" + (row));
						j = new Formula(e,row+5, "MIN(V2:V" + (row));
					}
					if(e==22){
						f = new Formula(e,row+1, "SUM(W2:W" + (row));
						g = new Formula(e,row+2, "W"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(W2:W" + (row));
					}
					if(e==23){
						f = new Formula(e,row+1, "SUM(X2:X" + (row));
						g = new Formula(e,row+2, "X"+ (row+2)+ "/" + (row-1));
						h = new Formula(e,row+4, "MAX(X2:X" + (row));
					}
					
					if(e!=3 && e != (numberObjectives+ numberCategorys + 1)){
						s.addCell(f);
					    s.addCell(g);
					    if(h!=null){
					    	s.addCell(h);
					    }
					    if(j!=null){
					    	s.addCell(j);
					    }
					}
				    
				}
			}
		}
		
		
		public void writeDataSheetKnapsack(Vector<SolutionSet> allBases) throws Exception{
			//set cell formats
			WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
			WritableCellFormat cf_Arial = new WritableCellFormat(wf);
			cf_Arial.setWrap(true);
			
			WritableCellFormat cf_NumberFloat = new WritableCellFormat(NumberFormats.FLOAT);
			NumberFormat nf_3digits = new NumberFormat("#.###");
		    WritableCellFormat cf_3digits = new WritableCellFormat(nf_3digits);
		    Number n;
			int numberObjectives = 0;
			for(int i = 0; i < allBases.size(); i++){
				SolutionSet sS = allBases.get(i);
				Iterator<Solution> itr = sS.iterator();
				createSheet("SolutionBase" + i);
				
				int row = 1;
				while (itr.hasNext()) {
					Solution element = itr.next();
					if(element.getFront()==1 | i == 0){
						double [] objectives = element.getObjectives();
						numberObjectives = objectives.length;
						for(int e = 0; e < objectives.length; e++) {
							n = new Number((1 + e),row,objectives[e],cf_3digits);
						    s.addCell(n);  
						  }

						row++;
					}
					
				}
				
				for(int e = 0; e < (numberObjectives+4); e++ ){
					Formula f = null;
					Formula g = null;
					Formula h = null;
					Formula j = null;
					//System.out.println("XLSOutput: numberObjectives is = " + numberObjectives);
					//System.out.println("XLSOutput: e is = " + e);
					if(e==0){
						f = new Formula(e,row+1, "SUM(A2:A" + (row-1));
						g = new Formula(e,row+2, "A103/100");
					}
					if(e==1){
						f = new Formula(e,row+1, "SUM(B2:B" + (row-1));
						g = new Formula(e,row+2, "B103/100");
					}
					if(e==2){
						f = new Formula(e,row+1, "SUM(C2:C" + (row-1));
						g = new Formula(e,row+2, "C103/100");
					}
					if(e==4){
						f = new Formula(e,row+1, "SUM(E2:E" + (row-1));
						g = new Formula(e,row+2, "E103/100");
						h = new Formula(e,row+4, "MAX(E2:E101)");
						//j = new Formula(e,row+2, "E103/100");
					}
					if(e==5){
						f = new Formula(e,row+1, "SUM(F2:F102)");
						g = new Formula(e,row+2, "F103/100");
						h = new Formula(e,row+4, "MAX(F2:F101)");
					}
					if(e==6){
						f = new Formula(e,row+1, "SUM(G2:G102)");
						g = new Formula(e,row+2, "G103/100");
					}
					if(e==7){
						f = new Formula(e,row+1, "SUM(H2:H102)");
						g = new Formula(e,row+2, "H103/100");
					}
					if(e==8){
						f = new Formula(e,row+1, "SUM(I2:I102)");
						g = new Formula(e,row+2, "I103/100");
					}
					if(e==9){
						f = new Formula(e,row+1, "SUM(J2:J102)");
						g = new Formula(e,row+2, "J103/100");
					}
					
					if(e!=3){
						s.addCell(f);
					    s.addCell(g);
					    if(h!=null){
					    	s.addCell(h);
					    }
					}
				    
				}
				
			    
			
		
			
			 /* int y = 1;
			  logger.trace("Solution Nr. " + z);
			  z++;
			  //only for testing purposes
			  if(element.getFront()==1)cdd++;
			  logger.trace("       solution has front value " + element.getFront() );
			  for(double obj: element.getObjectives()) {
				  logger.trace("       Objective Nr." + y + " is " + round(obj,3));
				  y++;
			  }*/
			}
		}
		
		
		public void writeDataSheet() throws Exception{
	
	    /* Format the Font */
	    WritableFont wf = new WritableFont(WritableFont.ARIAL, 
	      10, WritableFont.BOLD);
	    WritableCellFormat cf = new WritableCellFormat(wf);
	    cf.setWrap(true);
	
	    /* Creates Label and writes date to one cell of sheet*/
	    Label l = new Label(0,0,"Date",cf);
	    s.addCell(l);
	    WritableCellFormat cf1 = 
	      new WritableCellFormat(DateFormats.FORMAT9);
	
	    DateTime dt = 
	      new DateTime(0,1,new Date(), cf1, DateTime.GMT);

	    s.addCell(dt);
	    
	    /* Creates Label and writes float number to one cell of sheet*/
	    l = new Label(2,0,"Float", cf);
	    s.addCell(l);
	    WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.FLOAT);
	    Number n = new Number(2,1,3.1415926535,cf2);
	    s.addCell(n);
	
	    n = new Number(2,2,-3.1415926535, cf2);
	    s.addCell(n);
	
	    /* Creates Label and writes float number upto 3 
	       decimal to one cell of sheet */
	    l = new Label(3,0,"3dps",cf);
	    s.addCell(l);
	    NumberFormat dp3 = new NumberFormat("#.###");
	    WritableCellFormat dp3cell = new WritableCellFormat(dp3);
	    n = new Number(3,1,3.1415926535,dp3cell);
	    s.addCell(n);
	
	    /* Creates Label and adds 2 cells of sheet*/
	    l = new Label(4, 0, "Add 2 cells",cf);
	    s.addCell(l);
	    n = new Number(4,1,10);
	    s.addCell(n);
	    n = new Number(4,2,16);
	    s.addCell(n);
	    Formula f = new Formula(4,3, "E1+E2");
	    s.addCell(f);
	
	    /* Creates Label and multipies value of one cell of sheet by 2*/
	    l = new Label(5,0, "Multipy by 2",cf);
	    s.addCell(l);
	    n = new Number(5,1,10);
	    s.addCell(n);
	    f = new Formula(5,2, "F1 * 3");
	    s.addCell(f);
	
	    /* Creates Label and divide value of one cell of sheet by 2.5 */
	    l = new Label(6,0, "Divide",cf);
	    s.addCell(l);
	    n = new Number(6,1, 12);
	    s.addCell(n);
	    f = new Formula(6,2, "F1/2.5");
	    s.addCell(f);
	  }	
}

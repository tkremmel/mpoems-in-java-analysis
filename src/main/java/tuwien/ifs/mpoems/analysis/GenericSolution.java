package tuwien.ifs.mpoems.analysis;

import tuwien.ifs.mpoems.ASPopulation;
import tuwien.ifs.mpoems.Solution;
import tuwien.ifs.mpoems.SolutionSet;

/**
 * @author Thomas Kremmel
 *
 */
public class GenericSolution extends Solution {
	protected boolean objectivesSetOK = false;
	
	protected void setObjectives(int i, double value){
		this.objectives[i]=value;
		if(i == (nObjectives-1))objectivesSetOK = true;
	}
	
	protected void setConfig(int nObjectives, int [] objectivesMaxMin){
		this.nObjectives = nObjectives;
		objectives = new double[nObjectives];
		this.objectivesMaxMin = objectivesMaxMin;
	}
	
	/* (non-Javadoc)
	 * @see at.fisn.mta.mpoems.Solution#applyActions(at.fisn.mta.mpoems.ASPopulation)
	 */
	@Override
	public SolutionSet applyActions(ASPopulation ASPop) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see at.fisn.mta.mpoems.Solution#calcObjectives()
	 */
	@Override
	public void calcObjectives() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see at.fisn.mta.mpoems.Solution#deepCopy()
	 */
	@Override
	public void deepCopy() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see at.fisn.mta.mpoems.Solution#initSubClass()
	 */
	@Override
	public void initSubClass() {
		// TODO Auto-generated method stub

	}

}

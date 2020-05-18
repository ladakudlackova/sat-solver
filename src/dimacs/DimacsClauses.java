package dimacs;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import dpll.Clauses;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;


public class DimacsClauses extends Clauses{

	private HashSet<DimacsClause> clausesSet = new HashSet<DimacsClause>();
	private HashSet<DimacsClause> unitClausesSet = new HashSet<DimacsClause>();
	private ArrayList<DimacsClause>[] variableClausesEdges;
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(TseitinVariableToken[] variables) {
		
		variableClausesEdges= new ArrayList[variables.length+1];
		for (int i=1;i<variables.length;i++) {
			TseitinVariableToken var=variables[i];
			variableClausesEdges[var.getIndex()] = new ArrayList<DimacsClause>();
		}
		for (DimacsClause clause : clausesSet) {
			for (TseitinVariableToken var : clause.getPosLiterals()) 
				(variableClausesEdges[var.getIndex()]).add(clause);
			for (TseitinVariableToken var : clause.getNegLiterals()) 
				(variableClausesEdges[var.getIndex()]).add(clause);
		}
	}
	
	public void addClause(DimacsClause c) {
		clausesSet.add(c);
	    unsatisfiedCount++;
	}	
	
	@Override
	public void addClause(Object[] clause, TseitinVariableToken[] variables) {
		addClause(new DimacsClause(clause, variables));
	}
	
	@Override
	public DimacsClause getFirstUnitClause() {
		Iterator<DimacsClause> iterator = unitClausesSet.iterator();
		if (iterator.hasNext())
			return iterator.next();
		return null;
	}
	
	@Override
	public void updateClauses(Assignment a){
		
		for (DimacsClause clause : variableClausesEdges[a.getVariable().getIndex()]) {
			boolean wasSatisfied = clause.isSatisfied();
			clause.setValue(a.getVariable(), a.getValue());
			if (clause.getUnassignedCount()==0)
				unitClausesSet.remove(clause);
			if (!wasSatisfied && clause.isSatisfied()) {
				unsatisfiedCount--;
			}
			else if (clause.failed()) {
				failed=true;
				continue;
			}
			if (clause.getUnassignedCount()==1 && !clause.isSatisfied())
				unitClausesSet.add(clause);
		}
	}
	
	@Override
	public void resetValues(List<Integer> last, TseitinVariableToken[] variables) {
		
		for (Integer i : last) {
			TseitinVariableToken var=variables[i];
			resetValue(var);
		}
		failed=false;
	}
	
	@Override
	public void resetValue(TseitinVariableToken var) {
		
		for (DimacsClause clause:variableClausesEdges[var.getIndex()]) {
			boolean wasSatisfied = clause.isSatisfied();
			clause.setValue(var, null);
			if (wasSatisfied && !clause.isSatisfied())
				unsatisfiedCount++;
			if (clause.getUnassignedCount()==1 && !clause.isSatisfied()) 
				unitClausesSet.add(clause);
			else if (clause.getUnassignedCount()==2) 
				unitClausesSet.remove(clause);
		}
		var.setValue(null);
	}


	public Iterator<DimacsClause> getAllClauses() {
		return clausesSet.iterator();
	}
	
	
}

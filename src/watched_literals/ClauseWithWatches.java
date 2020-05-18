package watched_literals;

import java.util.ArrayList;
import java.util.List;

import dpll.Clause;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;

public class ClauseWithWatches extends Clause{

	private List<Assignment> literals = new ArrayList<Assignment>();
	private WatchedLiteral[] watchedLiterals= new WatchedLiteral[2];
	private boolean isUnit = false;
	private boolean failed = false;
	
	public ClauseWithWatches(Object[] clause, TseitinVariableToken[] variables) {
		
		for (Object literal : clause) 
			addLiteral(literal, variables);
		if (clause.length==1) 
			isUnit=true;
		
	}
	
	@Override
	protected void addLiteral(Object literal, TseitinVariableToken[] variables) {
		
		Assignment a = new Assignment(literal, variables);
		literals.add(a);
	}

	@Override
	protected Assignment getUnitAssignment() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public WatchedLiteral addWatchedLiteral(int index) {
		
		if (0<=index && index<watchedLiterals.length) {
			WatchedLiteral lit = new WatchedLiteral(literals.get(index), this, index);
			watchedLiterals[index]=lit;
			return lit;
		}
		return null;
	}
	
	protected void updateWatchedLiteral(WatchedLiteral watchedLit) {
		
		WatchedLiteral other=watchedLiterals[0];
		if (other.equals(watchedLit))
			other=watchedLiterals[1];
		Boolean otherSatisfied = other.getValue()==other.getVariable().getValue();
		if (isUnit && !otherSatisfied) {
			failed=true;
			return;
		}
	    WatchedLiteral freeLit = getFreeLiteral();
		if (freeLit==null) {
			if (!otherSatisfied && other.getValue()==null)
				isUnit=true;
		}
		else {
			watchedLit=freeLit;
		}
	}
	
	private WatchedLiteral getFreeLiteral() {
		
		int watchedIndex0=watchedLiterals[0].getIndex();
		int watchedIndex1=watchedLiterals[1].getIndex();
		for (int i=0; i<literals.size();i++) {
			if (i==watchedIndex0||i==watchedIndex1)
				continue;
			Assignment a = literals.get(i);
			Boolean value = a.getVariable().getValue();
			if (value==null || value.equals(a.getValue())) 
				return new WatchedLiteral(a, this, i);
		}
		return null;
	}
	
	protected boolean isUnit() {		
		return isUnit;
	}

	@Override
	public boolean isSatisfied() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean failed() {
		return failed;
	}
	
}

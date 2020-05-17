package watched_literals;

import java.util.ArrayList;
import java.util.List;

import dpll.Clause;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;

public class ClauseWithWatches extends Clause{

	private List<Assignment> literals = new ArrayList<Assignment>();
	private WatchedLiteral[] watchedLiterals = new WatchedLiteral[2];
	boolean isUnit = false;
	
	public ClauseWithWatches(Object[] clause, TseitinVariableToken[] variables) {
		
		for (Object literal : clause) 
			addLiteral(literal, variables);
		if (clause.length>0) {
			watchedLiterals[0]=new WatchedLiteral(literals.get(0), this, 0);
			if (clause.length==1) 
				isUnit=true;
			else
				watchedLiterals[1]=new WatchedLiteral(literals.get(1), this, 1);
		}
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
	
	public WatchedLiteral getWatchedLiteral(int index) {
		
		if (index>=0 && index<watchedLiterals.length)
			return watchedLiterals[index];
		return null;
	}
	
	public boolean isUnit() {
		
		return isUnit;
	}
	
}

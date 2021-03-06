package watched_literals;

import tseitin.Assignment;
import tseitin.TseitinVariableToken;

public class WatchedLiteral {

	private Assignment literal;
	private ClauseWithWatches clause;
	private int index;
	
	protected WatchedLiteral(Assignment a, ClauseWithWatches c, int i) {
		
		literal = a;
		clause = c;
		index = i;
	}
	
	protected Assignment getLiteral() {
		return literal;
	}
	
	protected TseitinVariableToken getVariable() {
		return literal.getVariable();
	}
	
	protected Boolean getValue() {
		return literal.getValue();
	}
	
	protected Boolean getVariableValue() {
		return literal.getVariable().getValue();
	}

	public int getIndex() {
		return index;
	}

	public ClauseWithWatches getClause() {
		return clause;
	}
	
	protected boolean isSatisfied() {
		return getValue()==getVariable().getValue();
	}
	
	protected void update(WatchedLiteral lit) {
		literal = lit.literal;
		index = lit.index;
	}
	
	@Override
	public boolean equals(Object other) { 
		
		if (other instanceof WatchedLiteral) 
			if (index==((WatchedLiteral)other).index)
				return true;  		// index in same clause
		return false;
	}
}

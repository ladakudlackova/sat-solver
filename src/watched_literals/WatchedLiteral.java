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
	
	protected TseitinVariableToken getVariable() {
		
		return literal.getVariable();
	}
	
	protected Boolean getValue() {
		
		return literal.getValue();
	}
}

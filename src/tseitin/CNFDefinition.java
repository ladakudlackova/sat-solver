package tseitin;

import java.util.ArrayList;
import java.util.Collection;

import simple_nnf_tree.DerivationNode;
import simple_nnf_tree.SimpleNNFVariableToken;
import simple_nnf_tree.SimpleNNFOperatorToken.operators;
import tseitin.CNFDefinition.implicationType;

public class CNFDefinition extends Definition {

	public static enum implicationType {
		EQUIVALENCE,
		LEFT_TO_RIGHT
	}
	
	private implicationType implType = implicationType.LEFT_TO_RIGHT;
	
	private Collection<Assignment[]> clauses= new ArrayList<Assignment[]>();
	
	
	public CNFDefinition(implicationType implType) {
		this.implType = implType;
	}

	@Override
	public void define(DerivationNode node) {
		if (node.getToken() instanceof SimpleNNFVariableToken)
			return;
		addClauses(node);
	}
	
	private void addClauses(DerivationNode node) {
		String token = node.getToken().getToken();
		
		TseitinVariableToken v = node.getTseitinVar(); 				
		TseitinVariableToken vLeft = node.getLeftChild().getTseitinVar();
		if (operators.NOT.equalsToken(token))
			addNOTClauses(new TseitinVariableToken[] {v, vLeft});
		else {
			TseitinVariableToken vRight = node.getRightChild().getTseitinVar();
			TseitinVariableToken[] vars = new TseitinVariableToken[] {v, vLeft, vRight};
			if (operators.AND.equalsToken(token))
				addANDClauses(vars);
			else if (operators.OR.equalsToken(token))
				addORClauses(vars); 
		}
	}
	
	private void addNOTClauses(TseitinVariableToken[] vars) {				
		
		addClause(vars, new boolean[] {false, false});					// v => not x
		if (implType.equals(implicationType.EQUIVALENCE))
			addClause(vars, new boolean[] {true, true});				// not x => v
	}
	
	private void addANDClauses(TseitinVariableToken[] vars) {		
																		// v => v1 and v2
		addClause(new TseitinVariableToken[] {vars[0], vars[1]}, new boolean[] {false, true});
		addClause(new TseitinVariableToken[] {vars[0], vars[2]}, new boolean[] {false, true});
		if (implType.equals(implicationType.EQUIVALENCE))	
				addClause(vars, new boolean[] {true, false, false});	// v1 and v2 => v
	}
	
	private void addORClauses(TseitinVariableToken[] vars) {		
		
		addClause(vars, new boolean[] {false, true, true});				// v => v1 or v2
		if (implType.equals(implicationType.EQUIVALENCE)) {				// v1 or v2 => v
			addClause(new TseitinVariableToken[] {vars[0], vars[1]}, new boolean[] {true, false});
			addClause(new TseitinVariableToken[] {vars[0], vars[2]}, new boolean[] {true, false});
		}
	}
	
	private void addClause(TseitinVariableToken[] vars, boolean[] values) {
		Assignment[] clause=new Assignment[vars.length];
		for (int i=0;i<vars.length;i++)
			clause[i]=new Assignment(vars[i], values[i]);
		getClauses().add(clause);
	}

	protected void addRootClause(DerivationNode root) {
		Assignment[] clause=new Assignment[1];
		clause[0] = new Assignment(root.getTseitinVar(), true);
		getClauses().add(clause);
	}

	public Collection<Assignment[]> getClauses() {
		return clauses;
	}
}
	
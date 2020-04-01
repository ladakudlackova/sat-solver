package tseitin;

import java.util.ArrayList;
import java.util.Collection;

import simple_nnf_tree.DerivationNode;
import simple_nnf_tree.SimpleNNFVariableToken;
import simple_nnf_tree.SimpleNNFOperatorToken.operators;

public abstract class Definition {
	public abstract void define(DerivationNode node);
}


class VaribleNameDefinition extends Definition {

	@Override
	public void define(DerivationNode node) {
		node.getToken().setTseitinVariable(node);		
	}
	
}

class CNFDefinition extends Definition {

	Collection<Assignment[]> clauses= new ArrayList<Assignment[]>();
	
	@Override
	public void define(DerivationNode node) {
		if (node.getToken() instanceof SimpleNNFVariableToken)
			return;
		if (operators.NOT.equalsToken(node.getToken().getToken()))
			addNOTClauses(node);
		else if (operators.AND.equalsToken(node.getToken().getToken()))
			addANDClauses(node);
		else 
			addORClauses(node);
	}
	
	private void addNOTClauses(DerivationNode node) {
		TseitinVariableToken v = node.getTseitinVar(); 				// v <=> not x
		TseitinVariableToken x = node.getLeftChild().getTseitinVar();
		Assignment[] clause=new Assignment[2];
		clause[0]=new Assignment(v, false);
		clause[1]=new Assignment(x, false);
		clauses.add(clause);
		clause[0]=new Assignment(v, true);
		clause[1]=new Assignment(x, true);
		clauses.add(clause);
	}
	
	private void addANDClauses(DerivationNode node) {
		TseitinVariableToken v = node.getTseitinVar(); 				// v <=> v1 and v2
		TseitinVariableToken v1 = node.getLeftChild().getTseitinVar();
		TseitinVariableToken v2 = node.getRightChild().getTseitinVar();
		Assignment[] clause=new Assignment[2];
		clause[0]=new Assignment(v, false);
		clause[1]=new Assignment(v1, true);
		clauses.add(clause);
		clause[0]=new Assignment(v, false);
		clause[1]=new Assignment(v2, true);
		clauses.add(clause);
		clause=new Assignment[3];
		clause[0]=new Assignment(v, true);
		clause[1]=new Assignment(v1, false);
		clause[2]=new Assignment(v2, false);
		clauses.add(clause);
	}
	
	private void addORClauses(DerivationNode node) {
		TseitinVariableToken v = node.getTseitinVar(); 				// v <=> v1 or v2
		TseitinVariableToken v1 = node.getLeftChild().getTseitinVar();
		TseitinVariableToken v2 = node.getRightChild().getTseitinVar();
		Assignment[] clause=new Assignment[2];
		clause[0]=new Assignment(v, true);
		clause[1]=new Assignment(v1, false);
		clauses.add(clause);
		clause[0]=new Assignment(v, true);
		clause[1]=new Assignment(v2, false);
		clauses.add(clause);
		clause=new Assignment[3];
		clause[0]=new Assignment(v, false);
		clause[1]=new Assignment(v1, true);
		clause[2]=new Assignment(v2, true);
		clauses.add(clause);
	}
	
}

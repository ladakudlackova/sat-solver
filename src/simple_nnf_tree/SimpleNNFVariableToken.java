package simple_nnf_tree;

import tseitin.TseitinVariableToken;

public class SimpleNNFVariableToken extends SimpleNNFToken {
	
	TseitinVariableToken tseitinVar;
	public SimpleNNFVariableToken(String operator) {
		super(operator);
	}
	
	public void setTseitinVar(TseitinVariableToken tseitinVar) {
		this.tseitinVar = tseitinVar;
	}

	@Override
	public void setTseitinVariable(DerivationNode node) {
		node.tseitinVar=getTseitinVar();		
	}

	public TseitinVariableToken getTseitinVar() {
		return tseitinVar;
	}
}


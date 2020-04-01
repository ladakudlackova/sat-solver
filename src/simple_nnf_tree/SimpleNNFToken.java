package simple_nnf_tree;

import tseitin_to_dimacs.FormulaToken;

public abstract class SimpleNNFToken extends FormulaToken {	
	
	public SimpleNNFToken(String operator) {
		token=operator;
	}
	
	@Override
	public String getToken() {
		return token.toString();
	}
	
	public abstract void setTseitinVariable(DerivationNode node);
}



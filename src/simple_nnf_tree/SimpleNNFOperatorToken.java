package simple_nnf_tree;

import tseitin.TseitinVariableToken;

public class SimpleNNFOperatorToken extends SimpleNNFToken {

	public enum operators 
	{
	    AND("and"), 
	    OR("or"), 
	    NOT("not"), 
	    LEFT_PAR("("),
	    RIGHT_PAR(")");
	 
	    SimpleNNFOperatorToken value;
	 
	    operators (String value) {
	        this.value = new SimpleNNFOperatorToken(value);
	    }	    
	    
	    public boolean equalsToken(String token) {
	    	return value.getToken().equals(token);
	    }
	}
	
	public SimpleNNFOperatorToken(String operator) {
		super(operator);
	}

	@Override
	public void setTseitinVariable(DerivationNode node) {
		node.tseitinVar=new TseitinVariableToken();		
	}	
}

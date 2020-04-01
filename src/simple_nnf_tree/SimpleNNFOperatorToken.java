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
	    
	    public static boolean equalsParenthesisToken(String token) {
	    	return (LEFT_PAR.equalsToken(token)|RIGHT_PAR.equalsToken(token));
	    }
	}
	
	public SimpleNNFOperatorToken(String operator) {
		super(operator);
	}

	public SimpleNNFOperatorToken() {
	}

	@Override
	public void setTseitinVariable(DerivationNode node) {
		node.tseitinVar=new TseitinVariableToken();		
	}	
}

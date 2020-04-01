package simple_nnf_tree;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simple_nnf_tree.SimpleNNFOperatorToken.operators;
import tseitin.Definition;


public class DerivationTree {

	private DerivationNode root;
	private boolean validNNF=true;
	private Map<String, SimpleNNFVariableToken> variables = new HashMap<String, SimpleNNFVariableToken>();
	
	
	
	public DerivationTree(Reader r) {
		build(r);
	}
	
	private void build(Reader r) {
		List<Object> tokens=null;
		try {
			tokens = Tokenizer.getTokens(r);
		} catch (IOException e) {
			validNNF = false;
			return;
		}		
		processTokens(tokens);
	}
	
	private void processTokens(List<Object> tokens){
		List<DerivationNode> stack = new ArrayList<DerivationNode>();
		int i=tokens.size();
		while (i>0 && validNNF) {
			i--;
			processToken(tokens.get(i).toString(), stack);
		}
		root=stack.get(0);
	}
	
	private void processToken(String token, List<DerivationNode> stack){
		if (operators.equalsParenthesisToken(token))
			return;
		else if (operators.NOT.equalsToken(token)) {
			DerivationNode current = new DerivationNode(operators.NOT.value);
			current.leftChild=stack.remove(0);
			stack.add(0,current);
		}
		else if (operators.AND.equalsToken(token)) {
			DerivationNode current = new DerivationNode(operators.AND.value);
			current.leftChild=stack.remove(0);
			current.rightChild=stack.remove(0);
			stack.add(0,current);
		}
		else if (operators.OR.equalsToken(token)) {
			DerivationNode current = new DerivationNode(operators.OR.value);
			current.leftChild=stack.remove(0);
			current.rightChild=stack.remove(0);
			stack.add(0,current);
		}
		else if (token.matches("^[A-Za-z][A-Za-z0-9]*"))
			processVariableToken(token, stack);
		else
			validNNF=false;
	}
	
	private void processVariableToken(String token, List<DerivationNode> stack){
		SimpleNNFVariableToken varToken;
		if (variables.containsKey(token))
			varToken=variables.get(token);
		else {
			varToken = new SimpleNNFVariableToken(token);
			variables.put(token, varToken);
		}
		stack.add(0,new DerivationNode(varToken));
	}
	
	public void traverse(Definition def) {
		List<DerivationNode> q = new ArrayList<DerivationNode>();
		q.add(root);
		DerivationNode current;
		while (!q.isEmpty()) {
			current=q.remove(0);
			def.define(current);
			if (current.getLeftChild()!=null) {
				q.add(current.getLeftChild());
				if (current.getRightChild()!=null)
					q.add(current.getRightChild());
			}
		}
	}
	
	public DerivationNode getRoot() {
		return root;
	}
	
	public boolean validNNF() {
		return validNNF;
	}
	
	public Collection<SimpleNNFVariableToken> getNNFVariables(){
		
		return variables.values();
	}	
}



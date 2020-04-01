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

// TODO: git + inp/out + pamet-stream

public class DerivationTree {

	private DerivationNode root;
	private boolean validNNF=true;
	private Map<String, SimpleNNFVariableToken> variables = new HashMap<String, SimpleNNFVariableToken>();
	
	
	
	public DerivationTree(Reader r) {
		build(r);
	}
	
	private void build(String nnf) {
		root = new DerivationNode(null);
		DerivationNode current = root;
		String[] tokens = nnf.split("\\s");
		int i=0;
		while (i<tokens.length && validNNF) {
			current=processToken(tokens[i], current);
			i++;
		}
	}
	
	private void build(Reader r) {
		root = new DerivationNode(null);
		DerivationNode current = root;
		List<Object> tokens;
		try {
			tokens = Tokenizer.getTokens(r);
			int i=0;
			while (i<tokens.size() && validNNF) {
				current=processToken(tokens.get(i).toString(), current);
				i++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private DerivationNode processToken(String token, DerivationNode current){
		if (operators.LEFT_PAR.equalsToken(token)) {
			current.leftChild=new DerivationNode(current);
			return current.getLeftChild();
		}
		if (operators.RIGHT_PAR.equalsToken(token)) {
			return current.parent;
		}
		if (operators.NOT.equalsToken(token)) {
			current.token=operators.NOT.value;
			current.leftChild=new DerivationNode(current);
			return current.getLeftChild();
		}
		if (operators.AND.equalsToken(token)) {
			current.token=operators.AND.value;
			current.rightChild=new DerivationNode(current);
			return current.getRightChild();
		}
		if (operators.OR.equalsToken(token)) {
			current.token=operators.OR.value;
			current.rightChild=new DerivationNode(current);
			return current.getRightChild();
		}
		if (token.matches("^[A-Za-z][A-Za-z0-9]*"))
			return processVariableToken(token, current);
		validNNF=false;
		return null;
	}
	
	private DerivationNode processVariableToken(String token, DerivationNode current){
		if (variables.containsKey(token))
			current.token=variables.get(token);
		else {
			SimpleNNFVariableToken varToken = new SimpleNNFVariableToken(token);
			current.token=varToken;
			variables.put(token, varToken);
		}
		return current.parent;
	}
	
	public void traverse(Definition def) {
		List<DerivationNode> q = new ArrayList<DerivationNode>();
		q.add(root.leftChild);
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
		
		return
				variables.values();
	}	
}



package simple_nnf_tree;

import tseitin.TseitinVariableToken;

public class DerivationNode {
	SimpleNNFToken token;
	TseitinVariableToken tseitinVar;
	DerivationNode leftChild, rightChild, parent;
	
	DerivationNode(DerivationNode parent){
		this.parent=parent;
	}

	public SimpleNNFToken getToken() {
		return token;
	}

	public TseitinVariableToken getTseitinVar() {
		return tseitinVar;
	}

	public DerivationNode getLeftChild() {
		return leftChild;
	}

	public DerivationNode getRightChild() {
		return rightChild;
	}
}
package simple_nnf_tree;

import tseitin.TseitinVariableToken;

public class DerivationNode {
	protected SimpleNNFToken token;
	protected TseitinVariableToken tseitinVar;
	protected DerivationNode leftChild, rightChild, parent;
	
	DerivationNode(DerivationNode parent){
		this.parent=parent;
	}

	public DerivationNode(SimpleNNFToken token) {
		this.token=token;
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
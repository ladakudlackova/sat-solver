package tseitin;

import simple_nnf_tree.DerivationNode;

public abstract class Definition {
	public abstract void define(DerivationNode node);
}


class VariableNameDefinition extends Definition {

	@Override
	public void define(DerivationNode node) {
		node.getToken().setTseitinVariable(node);		
	}
}

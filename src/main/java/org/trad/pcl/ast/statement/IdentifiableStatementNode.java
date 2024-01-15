package org.trad.pcl.ast.statement;

import org.trad.pcl.ast.expression.VariableReferenceNode;

public class IdentifiableStatementNode extends StatementNode {
    private VariableReferenceNode identifier;

    public IdentifiableStatementNode() {
        identifier = new VariableReferenceNode();
    }

    public void setIdentifier(String identifier) {
        this.identifier.setVariableName(identifier);
    }

    public void setNextIdentifier(VariableReferenceNode nextIdentifier) {
        this.identifier.setNextExpression(nextIdentifier);
    }

}

package org.trad.pcl.ast.expression;

import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.statement.StatementNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

public class VariableReferenceNode extends ASTNode implements ExpressionNode, StatementNode {
    private String identifier;

    private VariableReferenceNode nextExpression;

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setNextExpression(VariableReferenceNode nextExpression) {
        this.nextExpression = nextExpression;
    }

    public String getIdentifier() {
        return identifier;
    }

    public VariableReferenceNode getNextExpression() {
        return nextExpression;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}

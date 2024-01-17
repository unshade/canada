package org.trad.pcl.ast.expression;

public class VariableReferenceNode extends ExpressionNode {
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
}

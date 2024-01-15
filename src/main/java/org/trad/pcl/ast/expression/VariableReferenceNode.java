package org.trad.pcl.ast.expression;

public class VariableReferenceNode extends ExpressionNode {
    private String variableName;

    private VariableReferenceNode nextExpression;

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public void setNextExpression(VariableReferenceNode nextExpression) {
        this.nextExpression = nextExpression;
    }
}

package org.trad.pcl.ast.statement;


import org.trad.pcl.ast.expression.ExpressionNode;

public final class AssignmentNode extends IdentifiableStatementNode {
    private ExpressionNode expression;

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }


}

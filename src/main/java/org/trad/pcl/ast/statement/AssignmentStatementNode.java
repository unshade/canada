package org.trad.pcl.ast.statement;


import org.trad.pcl.ast.expression.ExpressionNode;

public final class AssignmentStatementNode extends IdentifiableStatementNode {
    private ExpressionNode expression;

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }


}

package org.trad.pcl.ast.statement;


import org.trad.pcl.ast.expression.ExpressionNode;

public class AssignmentNode extends IdentifiableStatementNode {
    private ExpressionNode expression;

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }


}

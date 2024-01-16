package org.trad.pcl.ast.expression;

import org.trad.pcl.ast.OperatorNode;

public final class UnaryExpressionNode extends ExpressionNode {
    private ExpressionNode operand;
    private OperatorNode operator;

    public void setOperand(ExpressionNode operand) {
        this.operand = operand;
    }

    public void setOperator(OperatorNode operator) {
        this.operator = operator;
    }
}

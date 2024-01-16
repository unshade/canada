package org.trad.pcl.ast.expression;

import org.trad.pcl.ast.OperatorNode;

public final class BinaryExpressionNode extends ExpressionNode {
    private ExpressionNode left;
    private OperatorNode operator;
    private ExpressionNode right;

    public void setLeft(ExpressionNode left) {
        this.left = left;
    }

    public void setRight(ExpressionNode right) {
        this.right = right;
    }

    public void setOperator(OperatorNode operator) {
        this.operator = operator;
    }

}
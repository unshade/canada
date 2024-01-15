package org.trad.pcl.ast.expression;

import org.trad.pcl.ast.OperatorNode;

public class BinaryExpressionNode extends ExpressionNode {
    private ExpressionNode left;
    private ExpressionNode right;
    private OperatorNode operator;

    public void setLeft(ExpressionNode left) {
        this.left = left;
        left.setParent(this);
    }

    public void setRight(ExpressionNode right) {
        this.right = right;
        right.setParent(this);
    }

    public void setOperator(OperatorNode operator) {
        this.operator = operator;
        operator.setParent(this);
    }

}
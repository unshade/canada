package org.trad.pcl.ast.expression;

import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.OperatorNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

public final class BinaryExpressionNode extends ASTNode implements ExpressionNode {
    private ExpressionNode left;
    private OperatorNode operator;
    private ExpressionNode right;



    public void setLeft(ExpressionNode left) {
        this.left = left;
    }

    public void setRight(ExpressionNode right) {
        this.right = right;
    }

    public void setRight(ExpressionNode first, BinaryExpressionNode second) {
        if (second != null) {
            second.setLeft(first);
            this.right = second;
        } else {
            this.right = first;
        }
    }

    public void setOperator(OperatorNode operator) {
        this.operator = operator;
    }

    public void setOperator(String operator) {
        this.operator = new OperatorNode();
        this.operator.setOperator(operator);
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public ExpressionNode getRight() {
        return right;
    }

    public OperatorNode getOperatorNode() {
        return operator;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
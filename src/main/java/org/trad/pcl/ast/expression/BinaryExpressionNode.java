package org.trad.pcl.ast.expression;

import org.trad.pcl.Exceptions.Semantic.BinaryTypeMismatchException;
import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.Helpers.OperatorEnum;
import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.Services.ErrorService;
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

    public void setMostLeft(ExpressionNode left) {
        if (this.left == null) {
            this.left = left;
        } else {
            ((BinaryExpressionNode) this.left).setMostLeft(left);
        }
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

    public void setOperator(OperatorEnum operator) {
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

    public void rotateLeft() {
        if (this.right == null) {
            throw new IllegalStateException("Right node must not be null for left rotation");
        }

        // Create a copy of the current node
        BinaryExpressionNode copy = new BinaryExpressionNode();
        copy.setLeft(this.left);
        copy.setRight(this.right);
        copy.setOperator(this.operator);

        // Perform the rotation on the copy
        BinaryExpressionNode newParent = (BinaryExpressionNode) copy.right;
        copy.right = newParent.left;
        newParent.left = copy;

        // Set actual tree
        this.left = newParent.left;
        this.right = newParent.right;
        this.operator = newParent.operator;
    }

    public void definePriority(String MostPriorityOperator, String LeastPriorityOperator) {
        BinaryExpressionNode copy = this;
        while (copy.getOperatorNode().getOperator().equals(MostPriorityOperator) && (((BinaryExpressionNode) copy.getRight()).getOperatorNode().getOperator().equals(LeastPriorityOperator) || ((BinaryExpressionNode) copy.getRight()).getOperatorNode().getOperator().equals(MostPriorityOperator))) {
            copy.rotateLeft();
            copy = (BinaryExpressionNode) copy.getLeft();
            if (copy == null || copy.getRight() instanceof LiteralNode) {
                break;
            }
        }
    }

    @Override
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    public void checkType() throws UndefinedVariableException {

        if (!left.getType().equals(right.getType()) || !left.getType().equals(operator.getType())) {
            ErrorService.getInstance().registerSemanticError(new BinaryTypeMismatchException(left.getType(), right.getType(), operator.getType(), this.getConcernedLine()));
        }
    }
    @Override
    public String getType() {
        return operator.getType();
    }
}
package org.trad.pcl.ast.expression;

import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.OperatorNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.StackTDS;

public final class UnaryExpressionNode extends ASTNode implements ExpressionNode {
    private ExpressionNode operand;
    private OperatorNode operator;

    public void setOperand(ExpressionNode operand) {
        this.operand = operand;
    }

    public void setOperator(OperatorNode operator) {
        this.operator = operator;
    }

    public ExpressionNode getOperand() {
        return operand;
    }

    public OperatorNode getOperator() {
        return operator;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    @Override
    public String getType(StackTDS stack) throws UndefinedVariableException {
        return operand.getType(stack);
    }

    public OperatorNode getOperatorNode() {
        return operator;
    }

}

package org.trad.pcl.ast.expression;

import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.OperatorNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

public final class UnaryExpressionNode extends ASTNode implements ExpressionNode {
    private ExpressionNode operand;
    private OperatorNode operator;

    public void setOperand(ExpressionNode operand) {
        this.operand = operand;
    }

    public void setOperator(OperatorNode operator) {
        this.operator = operator;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getType() {
        return "unknown";
    }
}

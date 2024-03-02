package org.trad.pcl.ast.statement;


import org.trad.pcl.ast.expression.ExpressionNode;
import org.trad.pcl.ast.expression.VariableReferenceNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

public final class AssignmentStatementNode extends VariableReferenceNode {
    private ExpressionNode expression;

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }

}

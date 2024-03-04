package org.trad.pcl.ast.statement;

import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.expression.ExpressionNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

import java.util.ArrayList;
import java.util.List;

public final class ReturnStatementNode extends ASTNode implements StatementNode {
    private ExpressionNode expression;

    public ReturnStatementNode() {
        this.expression = null;
    }

    public void addExpression(ExpressionNode expression) {
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

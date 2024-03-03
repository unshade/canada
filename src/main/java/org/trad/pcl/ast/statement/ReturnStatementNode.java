package org.trad.pcl.ast.statement;

import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.expression.ExpressionNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

import java.util.ArrayList;
import java.util.List;

public final class ReturnStatementNode extends ASTNode implements StatementNode {
    private final List<ExpressionNode> expressions;

    public ReturnStatementNode() {
        this.expressions = new ArrayList<>();
    }

    public void addExpressions(List<ExpressionNode> expressions) {
        this.expressions.addAll(expressions);
    }

    public List<ExpressionNode> getExpressions() {
        return expressions;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}

package org.trad.pcl.ast.statement;

import org.trad.pcl.ast.expression.ExpressionNode;

import java.util.ArrayList;
import java.util.List;

public class ReturnStatementNode extends StatementNode {
    private List<ExpressionNode> expressions;

    public ReturnStatementNode() {
        this.expressions = new ArrayList<>();
    }

    public void addExpression(ExpressionNode expression) {
        this.expressions.add(expression);
        expression.setParent(this);
    }

    public void addExpressions(List<ExpressionNode> expressions) {
        this.expressions.addAll(expressions);
        expressions.forEach(expression -> expression.setParent(this));
    }

}

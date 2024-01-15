package org.trad.pcl.ast.statement;

import org.trad.pcl.ast.expression.ExpressionNode;

public class ReturnStatementNode extends StatementNode {
    private ExpressionNode expression;

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

}

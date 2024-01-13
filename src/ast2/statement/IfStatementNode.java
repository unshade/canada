package ast2.statement;

import ast2.expression.ExpressionNode;

public class IfStatementNode extends StatementNode {
    private ExpressionNode condition;
    private BlockNode thenBranch;
    private BlockNode elseBranch;

}

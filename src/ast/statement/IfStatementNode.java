package ast.statement;

import ast.expression.ExpressionNode;

public class IfStatementNode extends StatementNode {
    private ExpressionNode condition;
    private BlockNode thenBranch;
    private BlockNode elseBranch;

}

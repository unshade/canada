package org.trad.pcl.ast.statement;


import org.trad.pcl.ast.expression.ExpressionNode;

public class IfStatementNode extends StatementNode {
    private ExpressionNode condition;
    private BlockNode thenBranch;
    private BlockNode elseBranch;

}

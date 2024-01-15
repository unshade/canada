package org.trad.pcl.ast.statement;


import org.trad.pcl.ast.expression.ExpressionNode;

public class IfStatementNode extends StatementNode {
    private ExpressionNode condition;
    private BlockNode thenBranch;
    private BlockNode elseBranch;
    private IfStatementNode elseIfBranch;

    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    public void setThenBranch(BlockNode thenBranch) {
        this.thenBranch = thenBranch;
        thenBranch.setParent(this);
    }

    public void setElseBranch(BlockNode elseBranch) {
        this.elseBranch = elseBranch;
        elseBranch.setParent(this);
    }

    public void setElseIfBranch(IfStatementNode elseIfBranch) {
        this.elseIfBranch = elseIfBranch;
        if (elseIfBranch != null) {
            elseIfBranch.setParent(this);
        }
    }

}

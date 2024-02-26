package org.trad.pcl.ast.statement;


import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.expression.ExpressionNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

public final class IfStatementNode extends ASTNode implements StatementNode {
    private ExpressionNode condition;
    private BlockNode thenBranch;
    private BlockNode elseBranch;
    private IfStatementNode elseIfBranch;

    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    public void setThenBranch(BlockNode thenBranch) {
        this.thenBranch = thenBranch;
    }

    public void setElseBranch(BlockNode elseBranch) {
        this.elseBranch = elseBranch;
    }

    public void setElseIfBranch(IfStatementNode elseIfBranch) {
        this.elseIfBranch = elseIfBranch;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public BlockNode getThenBranch() {
        return thenBranch;
    }

    public BlockNode getElseBranch() {
        return elseBranch;
    }

    public IfStatementNode getElseIfBranch() {
        return elseIfBranch;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}

package org.trad.pcl.ast.statement;

import org.trad.pcl.Exceptions.Semantic.InvalidConditionTypeException;
import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.expression.ExpressionNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

import java.beans.Statement;

public class ElseIfStatementNode extends ASTNode implements StatementNode {
    private ExpressionNode condition;
    private BlockNode thenBranch;

    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    public void setThenBranch(BlockNode thenBranch) {
        this.thenBranch = thenBranch;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public BlockNode getThenBranch() {
        return thenBranch;
    }

    public void checkConditionType() throws UndefinedVariableException {
        if (!condition.getType().equals(TypeEnum.BOOL.toString())) {
            ErrorService.getInstance().registerSemanticError(new InvalidConditionTypeException(condition.getType(), this.getConcernedLine()));
        }
    }

    @Override
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    public boolean hasReturn() {
        return thenBranch.hasReturn();
    }

    // Getters et setters pour les attributs
}

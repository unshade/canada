package org.trad.pcl.ast.statement;


import org.trad.pcl.Exceptions.Semantic.InvalidConditionTypeException;
import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.expression.ExpressionNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;

import java.util.ArrayList;
import java.util.List;

public final class IfStatementNode extends ASTNode implements StatementNode {
    private ExpressionNode condition;
    private BlockNode thenBranch;
    private BlockNode elseBranch;
    private List<ElseIfStatementNode> elseIfBranch;

    public IfStatementNode() {
        this.elseIfBranch = new ArrayList<>();
    }

    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    public void setThenBranch(BlockNode thenBranch) {
        this.thenBranch = thenBranch;
    }

    public void setElseBranch(BlockNode elseBranch) {
        this.elseBranch = elseBranch;
    }

    public void setElseIfBranch(List<ElseIfStatementNode> elseIfBranch) {
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

    public List<ElseIfStatementNode> getElseIfBranches() {
        return elseIfBranch;
    }

    public void checkConditionType() throws UndefinedVariableException {
        if (!condition.getType(SemanticAnalysisVisitor.scopeStack).equals(TypeEnum.BOOL.toString())) {
            ErrorService.getInstance().registerSemanticError(new InvalidConditionTypeException(condition.getType(SemanticAnalysisVisitor.scopeStack), this.getConcernedLine()));
        }
    }

    public boolean hasReturn() {
        boolean hasReturn = false;


        hasReturn = thenBranch.hasReturn();

        // Vérifier si la branche else a un return
        if (!hasReturn && elseBranch != null) {
            hasReturn = elseBranch.hasReturn();
        }

        // Vérifier si la branche elseif a un return
        if (!hasReturn && !elseIfBranch.isEmpty()) {
            for (ElseIfStatementNode elseIfStatementNode : elseIfBranch) {
                hasReturn = elseIfStatementNode.hasReturn();
                if (!hasReturn) {
                    break;
                }
            }
        }

        return hasReturn;
    }


    @Override
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }
}

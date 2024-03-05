package org.trad.pcl.ast.statement;


import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.expression.ExpressionNode;
import org.trad.pcl.ast.expression.VariableReferenceNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;
import org.trad.pcl.semantic.symbol.Function;
import org.trad.pcl.semantic.symbol.Variable;

public final class AssignmentStatementNode extends VariableReferenceNode {
    private ExpressionNode expression;

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }

    public void checkIfAssignable() {
        Variable reference = (Variable) SemanticAnalysisVisitor.findSymbolInScopes(this.getIdentifier());
        if (reference == null) {
            ErrorService.getInstance().registerSemanticError(new Exception("The variable " + this.getIdentifier() + " has not been declared"));
            return;
        }
        if (!reference.getType().equals(expression.getType())) {
            ErrorService.getInstance().registerSemanticError(new Exception("The type of the expression does not match the type of the variable (expected " + reference.getType() + " but got " + expression.getType() + ")"));
        }
    }
}

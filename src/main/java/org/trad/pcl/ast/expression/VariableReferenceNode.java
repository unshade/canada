package org.trad.pcl.ast.expression;

import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.statement.StatementNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;
import org.trad.pcl.semantic.symbol.Variable;

public class VariableReferenceNode extends ASTNode implements ExpressionNode, StatementNode {
    private String identifier;

    private VariableReferenceNode nextExpression;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public VariableReferenceNode getNextExpression() {
        return nextExpression;
    }

    public void setNextExpression(VariableReferenceNode nextExpression) {
        this.nextExpression = nextExpression;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getType() {
        Variable variableExpression = (Variable) SemanticAnalysisVisitor.findSymbolInScopes(this.getIdentifier());
        if (variableExpression == null) {
            ErrorService.getInstance().registerSemanticError(new Exception("The variable " + this.getIdentifier() + " has not been declared"));
            return "unknown";
        }
        return variableExpression.getType();
    }
}

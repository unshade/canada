package org.trad.pcl.ast.statement;


import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.expression.ExpressionNode;
import org.trad.pcl.ast.expression.VariableReferenceNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;
import org.trad.pcl.semantic.symbol.Function;
import org.trad.pcl.semantic.symbol.Parameter;
import org.trad.pcl.semantic.symbol.Symbol;
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
        Symbol reference = SemanticAnalysisVisitor.findSymbolInScopes(this.getIdentifier());
        if (reference == null) {
            return;
        }
        switch (reference.getClass().getSimpleName()) {
            case "Parameter" -> {
                Parameter parameter = (Parameter) reference;
                if (parameter.getMode().equals("in")) {
                    ErrorService.getInstance().registerSemanticError(new Exception("The mode of the parameter " + parameter.getIdentifier() + " is in, it cannot be assigned"));
                } else if (parameter.getType().equals(expression.getType())) {
                    ErrorService.getInstance().registerSemanticError(new Exception("The type of the expression does not match the type of the parameter (expected " + parameter.getType() + " but got " + expression.getType() + ")"));
                }
            }
            case "Variable" -> {
                Variable variable = (Variable) reference;
                if (variable.getType().equals(expression.getType())) {
                    ErrorService.getInstance().registerSemanticError(new Exception("The type of the expression does not match the type of the variable (expected " + variable.getType() + " but got " + expression.getType() + ")"));
                }
            }
        }


    }
}

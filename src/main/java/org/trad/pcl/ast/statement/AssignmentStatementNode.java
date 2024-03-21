package org.trad.pcl.ast.statement;


import org.trad.pcl.Exceptions.Semantic.InParameterModificationException;
import org.trad.pcl.Exceptions.Semantic.TypeMismatchException;
import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.Helpers.ParameterMode;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.ParameterNode;
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
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    public void checkIfAssignable() {
        Variable reference = (Variable) SemanticAnalysisVisitor.findSymbolInScopes(this.getIdentifier());
        if (reference == null) {
            //ErrorService.getInstance().registerSemanticError(new UndefinedVariableException(this.getIdentifier()));
            return;
        }
        if (reference instanceof Parameter) {
            if (((Parameter) reference).getMode().equals(ParameterMode.IN)) {
                ErrorService.getInstance().registerSemanticError(new InParameterModificationException(this.getIdentifier()));
                return;
            }
        }

        if (!reference.getType().equals(expression.getType())) {
            ErrorService.getInstance().registerSemanticError(new TypeMismatchException(reference.getType(), expression.getType()));
        }

    }
}

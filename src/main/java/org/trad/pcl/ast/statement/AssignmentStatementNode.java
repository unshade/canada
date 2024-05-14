package org.trad.pcl.ast.statement;


import org.trad.pcl.Exceptions.Semantic.InParameterModificationException;
import org.trad.pcl.Exceptions.Semantic.TypeMismatchException;
import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.Helpers.ParameterMode;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.expression.ExpressionNode;
import org.trad.pcl.ast.expression.VariableReferenceNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;
import org.trad.pcl.semantic.symbol.Parameter;
import org.trad.pcl.semantic.symbol.Symbol;
import org.trad.pcl.semantic.symbol.Variable;

public final class AssignmentStatementNode extends ASTNode implements IdentifiableStatement{

    private VariableReferenceNode variableReference;
    private ExpressionNode expression;

    public AssignmentStatementNode() {
        this.variableReference = new VariableReferenceNode();
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    public void setIdentifier(String variableReference) {
        this.variableReference.setIdentifier(variableReference);
    }

    public VariableReferenceNode getVariableReference() {
        return variableReference;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    public void checkIfAssignable() throws UndefinedVariableException {
        Symbol symbol = SemanticAnalysisVisitor.scopeStack.findSymbolInScopes(this.variableReference.getIdentifier(), this.variableReference.getConcernedLine());
        if(!(symbol instanceof Variable)){
            ErrorService.getInstance().registerSemanticError(new TypeMismatchException("Variable", symbol.getClass().getSimpleName(), this.variableReference.getConcernedLine()));
            return;
        }
        System.out.println(symbol.getIdentifier());

        if (symbol instanceof Parameter param) {
            if (param.getMode().equals(ParameterMode.IN)) {
                ErrorService.getInstance().registerSemanticError(new InParameterModificationException(this.getVariableReference().getIdentifier(), this.getVariableReference().getConcernedLine()));
                return;
            }
        }

        if (!this.variableReference.getType(SemanticAnalysisVisitor.scopeStack).equals(expression.getType(SemanticAnalysisVisitor.scopeStack))) {
            ErrorService.getInstance().registerSemanticError(new TypeMismatchException(((Variable) symbol).getType(), expression.getType(SemanticAnalysisVisitor.scopeStack), this.variableReference.getConcernedLine()));
        }

    }
}

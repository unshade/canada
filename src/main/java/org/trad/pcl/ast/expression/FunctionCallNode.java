package org.trad.pcl.ast.expression;

import org.trad.pcl.Exceptions.Semantic.ArgumentTypeMismatchException;
import org.trad.pcl.Exceptions.Semantic.IncorrectNumberOfArgumentsException;
import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.statement.IdentifiableStatement;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;
import org.trad.pcl.semantic.symbol.Function;
import org.trad.pcl.semantic.symbol.Record;

import java.util.List;
import java.util.Locale;

public final class FunctionCallNode extends ASTNode implements IdentifiableExpression, IdentifiableStatement {

    private VariableReferenceNode variableReferenceNode;
    private List<ExpressionNode> arguments;

    public FunctionCallNode() {
        this.variableReferenceNode = new VariableReferenceNode();
    }

    public void setArguments(List<ExpressionNode> arguments) {
        this.arguments = arguments;
    }
    public List<ExpressionNode> getArguments() {
        return arguments;
    }

    public void setIdentifier(String variableReference) {
        this.variableReferenceNode.setIdentifier(variableReference);
    }

    public VariableReferenceNode getVariableReference() {
        return variableReferenceNode;
    }


    @Override
    public String getType() throws UndefinedVariableException {
        Function function = (Function) SemanticAnalysisVisitor.findSymbolInScopes(this.variableReferenceNode.getIdentifier());
        String type = function.getType();

        if (this.variableReferenceNode.getNextExpression() != null) {
            VariableReferenceNode next = this.variableReferenceNode.getNextExpression();
            while (next != null) {
                Record recordType = (Record) SemanticAnalysisVisitor.findSymbolInScopes(type);
                type = recordType.getField(next.getIdentifier()).getType();
                next = next.getNextExpression();
            }
        }
        return type;
    }

    public void checkParametersSize() throws Exception {
        Function correspondingDeclaration = (Function) SemanticAnalysisVisitor.findSymbolInScopes(this.variableReferenceNode.getIdentifier());

        // Check if the number of arguments match the number of declared parameters
        if (this.getArguments().size() != correspondingDeclaration.getIndexedParametersTypes().size()) {
            throw new IncorrectNumberOfArgumentsException(this.variableReferenceNode.getIdentifier(), correspondingDeclaration.getIndexedParametersTypes().size(), this.getArguments().size());
        }
    }

    public void checkParametersTypes() throws Exception {
        // Check if the types of the arguments match the types of the declared parameters
        Function correspondingDeclaration = (Function) SemanticAnalysisVisitor.findSymbolInScopes(this.variableReferenceNode.getIdentifier());

        for (int i = 0; i < this.getArguments().size(); i++) {
            String argumentType = this.getArguments().get(i).getType();
            String parameterType = correspondingDeclaration.getIndexedParametersTypes().get(i).toLowerCase(Locale.ROOT);
            if (!argumentType.equals(parameterType)) {
                throw new ArgumentTypeMismatchException(correspondingDeclaration.getIndexedParametersTypes().get(i), argumentType);
            }
        }
    }
    @Override
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }

}
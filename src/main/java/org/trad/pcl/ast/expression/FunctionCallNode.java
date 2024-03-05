package org.trad.pcl.ast.expression;

import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;
import org.trad.pcl.semantic.symbol.Function;

import java.util.List;
import java.util.Locale;

public final class FunctionCallNode extends VariableReferenceNode {

    private List<ExpressionNode> arguments;

    public void setArguments(List<ExpressionNode> arguments) {
        this.arguments = arguments;
    }
    public List<ExpressionNode> getArguments() {
        return arguments;
    }


    @Override
    public String getType() {
        Function correspondingDeclaration = (Function) SemanticAnalysisVisitor.findSymbolInScopes(this.getIdentifier());
        if (correspondingDeclaration == null) {
            ErrorService.getInstance().registerSemanticError(new Exception("The function " + this.getIdentifier() + " has not been declared"));
            return "undefined";
        }
        return correspondingDeclaration.getReturnType();
    }

    public void checkParametersSize() {
        Function correspondingDeclaration = (Function) SemanticAnalysisVisitor.findSymbolInScopes(this.getIdentifier());
        if (correspondingDeclaration == null) {
            ErrorService.getInstance().registerSemanticError(new Exception("The function " + this.getIdentifier() + " has not been declared"));
            return;
        }
        // Check if the number of arguments match the number of declared parameters
        if (this.getArguments().size() != correspondingDeclaration.getIndexedParametersTypes().size()) {
            ErrorService.getInstance().registerSemanticError(new Exception("The number of arguments does not match the number of parameters (expected " + correspondingDeclaration.getIndexedParametersTypes().size() + " but got " + this.getArguments().size() + ")" + " for function " + this.getIdentifier()));
        }
    }

    public void checkParametersTypes() {
        // Check if the types of the arguments match the types of the declared parameters
        Function correspondingDeclaration = (Function) SemanticAnalysisVisitor.findSymbolInScopes(this.getIdentifier());
        if (correspondingDeclaration == null) {
            ErrorService.getInstance().registerSemanticError(new Exception("The function " + this.getIdentifier() + " has not been declared"));
            return;
        }
        for (int i = 0; i < this.getArguments().size(); i++) {
            String argumentType = this.getArguments().get(i).getType().toLowerCase(Locale.ROOT);
            String parameterType = correspondingDeclaration.getIndexedParametersTypes().get(i).toLowerCase(Locale.ROOT);
            if (!argumentType.equals(parameterType)) {
                ErrorService.getInstance().registerSemanticError(new Exception("The type of the argument " + i + " does not match the type of the parameter (expected " + correspondingDeclaration.getIndexedParametersTypes().get(i) + " but got " + this.getArguments().get(i).getType() + ")" + " for function " + this.getIdentifier()));
            }
        }
    }
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }

}
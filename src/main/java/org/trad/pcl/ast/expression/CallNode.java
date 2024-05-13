package org.trad.pcl.ast.expression;

import org.trad.pcl.Exceptions.Semantic.ArgumentTypeMismatchException;
import org.trad.pcl.Exceptions.Semantic.IncorrectNumberOfArgumentsException;
import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;
import org.trad.pcl.semantic.symbol.Function;
import org.trad.pcl.semantic.symbol.Procedure;
import org.trad.pcl.semantic.symbol.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class CallNode extends VariableReferenceNode {

    private boolean isExpression = false;

    private List<ExpressionNode> arguments;

    public CallNode() {
        arguments = new ArrayList<>();
    }

    public void setIsExpression(boolean isExpression) {
        this.isExpression = isExpression;
    }

    public boolean getIsExpression() {
        return isExpression;
    }


    public void setArguments(List<ExpressionNode> arguments) {
        this.arguments = arguments;
    }

    public List<ExpressionNode> getArguments() {
        return arguments;
    }

    @Override
    public String getType() throws UndefinedVariableException {
        System.out.println(this.getIdentifier());
        Function function = (Function) SemanticAnalysisVisitor.findSymbolInScopes(this.getIdentifier(), this.getConcernedLine());
        String type = function.getReturnType();

        if (this.getNextExpression() != null) {
            VariableReferenceNode next = this.getNextExpression();
            while (next != null) {
                Record recordType = (Record) SemanticAnalysisVisitor.findSymbolInScopes(type, this.getConcernedLine());
                type = recordType.getField(next.getIdentifier()).getType();
                next = next.getNextExpression();
            }
        }
        return type;
    }

    public void checkParametersSize() throws Exception {
        Procedure correspondingDeclaration = (Procedure) SemanticAnalysisVisitor.findSymbolInScopes(this.getIdentifier(), this.getConcernedLine());

        // Check if the number of arguments match the number of declared parameters
        // Check if arguments are null
        if (this.getArguments() != null) {
            if (this.getArguments().size() != correspondingDeclaration.getIndexedParametersTypes().size()) {
                throw new IncorrectNumberOfArgumentsException(this.getIdentifier(), correspondingDeclaration.getIndexedParametersTypes().size(), this.getArguments().size(), this.getConcernedLine());
            }
        }
    }

    public void checkParametersTypes() throws Exception {
        // Check if the types of the arguments match the types of the declared parameters
        Procedure correspondingDeclaration = (Procedure) SemanticAnalysisVisitor.findSymbolInScopes(this.getIdentifier(), this.getConcernedLine());
        if (this.getArguments() != null) {
            for (int i = 0; i < this.getArguments().size(); i++) {
                String argumentType = this.getArguments().get(i).getType();
                String parameterType = correspondingDeclaration.getIndexedParametersTypes().get(i);
                if (!argumentType.equals(parameterType)) {
                    throw new ArgumentTypeMismatchException(correspondingDeclaration.getIndexedParametersTypes().get(i), argumentType, this.getConcernedLine());
                }
            }
        }
    }
    @Override
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }

}
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

public class CallNode extends VariableReferenceNode {

    private List<ExpressionNode> arguments;


    public void setArguments(List<ExpressionNode> arguments) {
        this.arguments = arguments;
    }

    public List<ExpressionNode> getArguments() {
        return arguments;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }
}
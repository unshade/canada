package org.trad.pcl.ast.expression;

import org.trad.pcl.semantic.ASTNodeVisitor;

import java.util.List;

public final class FunctionCallNode extends VariableReferenceNode {

    private List<ExpressionNode> arguments;

    public void setArguments(List<ExpressionNode> arguments) {
        this.arguments = arguments;
    }
    public List<ExpressionNode> getArguments() {
        return arguments;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }

}
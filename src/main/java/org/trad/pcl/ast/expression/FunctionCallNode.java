package org.trad.pcl.ast.expression;

import java.util.List;

public final class FunctionCallNode extends VariableReferenceNode {

    private List<ExpressionNode> arguments;

    public void setArguments(List<ExpressionNode> arguments) {
        this.arguments = arguments;
    }

}
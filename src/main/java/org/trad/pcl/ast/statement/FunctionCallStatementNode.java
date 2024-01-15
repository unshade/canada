package org.trad.pcl.ast.statement;

import org.trad.pcl.ast.expression.ExpressionNode;

import java.util.List;

public class FunctionCallStatementNode extends IdentifiableStatementNode {

    private List<ExpressionNode> arguments;

    public void setArguments(List<ExpressionNode> arguments) {
        this.arguments = arguments;
        arguments.forEach(argument -> argument.setParent(this));
    }

}
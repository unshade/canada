package ast.expression;

import ast.OperatorNode;

public class BinaryExpressionNode extends ExpressionNode {
    private ExpressionNode left;
    private ExpressionNode right;
    private OperatorNode operator;

}
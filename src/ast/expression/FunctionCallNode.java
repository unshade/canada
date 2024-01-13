package ast.expression;

import java.util.List;

public class FunctionCallNode extends ExpressionNode {
    private String functionName;
    private List<ExpressionNode> arguments;

}
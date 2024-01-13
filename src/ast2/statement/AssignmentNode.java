package ast2.statement;

import ast2.expression.ExpressionNode;
import ast2.expression.VariableReferenceNode;

public class AssignmentNode extends StatementNode {
    private VariableReferenceNode variable;
    private ExpressionNode expression;

}

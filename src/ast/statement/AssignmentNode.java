package ast.statement;

import ast.expression.ExpressionNode;
import ast.expression.VariableReferenceNode;

public class AssignmentNode extends StatementNode {
    private VariableReferenceNode variable;
    private ExpressionNode expression;

}

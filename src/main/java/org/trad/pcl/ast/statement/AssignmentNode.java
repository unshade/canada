package org.trad.pcl.ast.statement;


import org.trad.pcl.ast.expression.ExpressionNode;
import org.trad.pcl.ast.expression.VariableReferenceNode;

public class AssignmentNode extends StatementNode {
    private VariableReferenceNode variable;
    private ExpressionNode expression;

}

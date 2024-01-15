package org.trad.pcl.ast.statement;


import org.trad.pcl.ast.AccessReferenceNode;
import org.trad.pcl.ast.expression.ExpressionNode;

public class AssignmentNode extends IdentifiableStatementNode {
    private AccessReferenceNode variable;
    private ExpressionNode expression;

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
        expression.setParent(this);
    }

    public void setVariableReference(AccessReferenceNode variable) {
        this.variable = variable;
        if (variable != null) {
            variable.setParent(this);
        }
    }

}

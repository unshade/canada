package org.trad.pcl.ast.statement;


import org.trad.pcl.ast.AccessReferenceNode;
import org.trad.pcl.ast.expression.ExpressionNode;
import org.trad.pcl.ast.expression.VariableReferenceNode;

public class AssignmentNode extends IdentifiableStatementNode {
    private ExpressionNode expression;

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
        expression.setParent(this);
    }


}

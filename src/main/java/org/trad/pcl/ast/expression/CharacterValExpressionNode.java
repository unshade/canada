package org.trad.pcl.ast.expression;

import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.StackTDS;

public class CharacterValExpressionNode extends ASTNode implements ExpressionNode {
    private ExpressionNode expression;

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    @Override
    public String getType(StackTDS stack) {
        return TypeEnum.CHAR.toString();
    }
}

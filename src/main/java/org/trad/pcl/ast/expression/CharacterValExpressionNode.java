package org.trad.pcl.ast.expression;

import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

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
    public String getType() {
        return TypeEnum.CHAR.toString();
    }
}

package org.trad.pcl.ast.expression;

import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

public class CharacterValExpressionNode extends ASTNode implements ExpressionNode {
    private ExpressionNode expression;

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }


    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}

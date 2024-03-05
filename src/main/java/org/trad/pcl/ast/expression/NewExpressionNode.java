package org.trad.pcl.ast.expression;

import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

public class NewExpressionNode extends ASTNode implements ExpressionNode {
    private String type;

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getType() {
        return "unknown";
    }
}

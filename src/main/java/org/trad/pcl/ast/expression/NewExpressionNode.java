package org.trad.pcl.ast.expression;

import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

public class NewExpressionNode extends ASTNode implements ExpressionNode {
    private String identifier;

    public void setIdentifier(String type) {
        this.identifier = type;
    }

    public String getIdentifier() {
        return identifier;
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

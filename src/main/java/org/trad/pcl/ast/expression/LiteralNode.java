package org.trad.pcl.ast.expression;

import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

public final class LiteralNode extends ASTNode implements ExpressionNode {
    private Object value;

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getType() {
        return switch (value.getClass().getSimpleName()) {
            case "Integer", "Long" -> "integer";
            case "Character" -> "character";
            case "Boolean" -> "boolean";
            default -> "unknown";
        };
    }
}

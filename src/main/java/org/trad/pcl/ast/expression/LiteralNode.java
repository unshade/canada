package org.trad.pcl.ast.expression;

import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.StackTDS;

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
    public String getType(StackTDS stack) {
        return switch (value.getClass().getSimpleName()) {
            case "Integer", "Long" -> TypeEnum.INT.toString();
            case "Character" -> TypeEnum.CHAR.toString();
            case "Boolean" -> TypeEnum.BOOL.toString();
            default -> TypeEnum.UNKNOWN.toString();
        };
    }
}

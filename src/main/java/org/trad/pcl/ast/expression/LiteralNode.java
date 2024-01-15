package org.trad.pcl.ast.expression;

public class LiteralNode extends ExpressionNode {
    private Object value;

    public void setValue(Object value) {
        this.value = value;
    }
}

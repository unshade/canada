package org.trad.pcl.ast;

import org.trad.pcl.semantic.ASTNodeVisitor;

public final class OperatorNode extends ASTNode {
    private String operator;

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {

    }
}

package org.trad.pcl.ast;

import org.trad.pcl.Helpers.OperatorEnum;
import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.semantic.ASTNodeVisitor;

public final class OperatorNode extends ASTNode {
    private OperatorEnum operator;

    public void setOperator(OperatorEnum operator) {
        this.operator = operator;
    }


    public OperatorEnum getOperator() {
        return operator;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
    }

    public String getEnterType() {
        return switch (operator) {
            case  AND, ANDTHEN, OR, ORELSE -> TypeEnum.BOOL.toString();
            default -> TypeEnum.INT.toString();
        };
    }

    public String getType() {
        return switch (operator) {
            case  NOT, EQUALS, NOT_EQUALS, LESS_THAN, GREATER_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN_OR_EQUAL, AND, ANDTHEN, OR, ORELSE -> TypeEnum.BOOL.toString();
            default -> TypeEnum.INT.toString();
        };
    }
}

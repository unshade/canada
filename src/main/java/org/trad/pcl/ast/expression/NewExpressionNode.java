package org.trad.pcl.ast.expression;

import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.Helpers.TypeEnum;
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
    public void accept(ASTNodeVisitor visitor) throws UndefinedVariableException {
        visitor.visit(this);
    }

    @Override
    public String getType() {
        return TypeEnum.UNKNOWN.toString();
    }
}

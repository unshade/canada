package org.trad.pcl.ast.type;

import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.symbol.Symbol;
import org.trad.pcl.semantic.symbol.Type;

public class TypeNode extends ASTNode {
    private String identifier;

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    public Symbol toSymbol() {
        return new Type(identifier, 0);
    }
}
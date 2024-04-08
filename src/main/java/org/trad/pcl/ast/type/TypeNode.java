package org.trad.pcl.ast.type;

import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
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

    public int getSize() {
        return 4;
    }

    public Symbol toSymbol() throws UndefinedVariableException {
        Type t=  new Type(identifier, 0);
        t.setSize(getSize());
        return t;
    }
}
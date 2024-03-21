package org.trad.pcl.ast.type;

import org.trad.pcl.semantic.symbol.Access;
import org.trad.pcl.semantic.symbol.Symbol;

public final class AccessTypeNode extends TypeNode {

    private TypeNode baseType;


    public void setBaseType(TypeNode baseType) {
        this.baseType = baseType;
    }


    public TypeNode getBaseType() {
        return baseType;
    }

    public Symbol toSymbol() {
        Access access = new Access(getIdentifier(), 0);
        access.setTypeAccess(baseType.getIdentifier());
        return access;
    }

}

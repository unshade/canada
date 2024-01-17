package org.trad.pcl.ast.type;

public final class AccessTypeNode extends TypeNode {
    private TypeNode baseType;


    public void setBaseType(TypeNode baseType) {
        this.baseType = baseType;
    }

    public TypeNode getBaseType() {
        return baseType;
    }

}

package org.trad.pcl.ast.type;

public class AccessTypeNode extends TypeNode {
    private TypeNode baseType;

    public void setBaseType(TypeNode baseType) {
        this.baseType = baseType;
    }

}

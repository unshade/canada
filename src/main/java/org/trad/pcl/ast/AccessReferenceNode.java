package org.trad.pcl.ast;

public class AccessReferenceNode extends ASTNode {

    private String variableName;

    private AccessReferenceNode nextVariable;

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public void setNextVariable(AccessReferenceNode nextVariable) {
        this.nextVariable = nextVariable;
        this.nextVariable.setParent(this);
    }
}

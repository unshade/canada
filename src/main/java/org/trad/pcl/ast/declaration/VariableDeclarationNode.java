package org.trad.pcl.ast.declaration;

import org.trad.pcl.ast.statement.AssignmentNode;
import org.trad.pcl.ast.type.TypeNode;

public final class VariableDeclarationNode extends DeclarationNode {
    private TypeNode type;

    private AssignmentNode assignment;

    public void setType(TypeNode type) {
        this.type = type;
    }

    public void setAssignment(AssignmentNode assignment) {
        this.assignment = assignment;
    }

    public TypeNode getType() {
        return type;
    }

    public AssignmentNode getAssignment() {
        return assignment;
    }
}

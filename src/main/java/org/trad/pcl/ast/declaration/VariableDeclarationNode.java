package org.trad.pcl.ast.declaration;

import org.trad.pcl.ast.statement.AssignmentStatementNode;
import org.trad.pcl.ast.type.TypeNode;

public final class VariableDeclarationNode extends DeclarationNode {
    private TypeNode type;

    private AssignmentStatementNode assignment;

    public void setType(TypeNode type) {
        this.type = type;
    }

    public void setAssignment(AssignmentStatementNode assignment) {
        this.assignment = assignment;
    }

    public TypeNode getType() {
        return type;
    }

    public AssignmentStatementNode getAssignment() {
        return assignment;
    }
}

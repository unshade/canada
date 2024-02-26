package org.trad.pcl.ast;

import org.trad.pcl.ast.declaration.ProcedureDeclarationNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

public final class ProgramNode extends ASTNode {
    private ProcedureDeclarationNode rootProcedure;

    public void setRootProcedure(ProcedureDeclarationNode rootProcedure) {
        this.rootProcedure = rootProcedure;
    }

    public ProcedureDeclarationNode getRootProcedure() {
        return rootProcedure;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}

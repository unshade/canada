package org.trad.pcl.ast;

import org.trad.pcl.ast.declaration.ProcedureDeclarationNode;

public final class ProgramNode extends ASTNode {
    private ProcedureDeclarationNode rootProcedure;


    public void setRootProcedure(ProcedureDeclarationNode rootProcedure) {
        this.rootProcedure = rootProcedure;
    }

}

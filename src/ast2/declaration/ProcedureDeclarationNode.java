package ast2.declaration;

import ast2.ParameterNode;
import ast2.statement.BlockNode;

import java.util.List;

public class ProcedureDeclarationNode extends DeclarationNode {
    private List<ParameterNode> parameters;
    private BlockNode body;

    public ProcedureDeclarationNode(String name) {
        super(name);
    }

    public void addDeclaration(DeclarationNode declaration) {
        body.addDeclaration(declaration);
    }

}

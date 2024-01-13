package ast.declaration;

import ast.ParameterNode;
import ast.statement.BlockNode;

import java.util.List;

public class ProcedureDeclarationNode extends DeclarationNode {
    private List<ParameterNode> parameters;
    private BlockNode body;

    public ProcedureDeclarationNode(String name) {
        super(name);
    }

    public void setBody(BlockNode body) {
        this.body = body;
    }

}

package ast.declaration;

import ast.ParameterNode;
import ast.statement.BlockNode;
import ast.type.TypeNode;

import java.util.List;

public class FunctionDeclarationNode extends DeclarationNode {
    private List<ParameterNode> parameters;
    private TypeNode returnType;
    private BlockNode body;

    public FunctionDeclarationNode(String name) {
        super(name);
    }

}

package ast2.declaration;

import ast2.ParameterNode;
import ast2.statement.BlockNode;
import ast2.type.TypeNode;

import java.util.List;

public class FunctionDeclarationNode extends DeclarationNode {
    private List<ParameterNode> parameters;
    private TypeNode returnType;
    private BlockNode body;

    public FunctionDeclarationNode(String name) {
        super(name);
    }

}

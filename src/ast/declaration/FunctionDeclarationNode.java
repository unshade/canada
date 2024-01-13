package ast.declaration;

import ast.ParameterNode;
import ast.statement.BlockNode;
import ast.type.TypeNode;

import java.util.ArrayList;
import java.util.List;

public class FunctionDeclarationNode extends DeclarationNode {
    private List<ParameterNode> parameters;
    private TypeNode returnType;
    private BlockNode body;

    public FunctionDeclarationNode(String name) {
        super(name);
        this.parameters = new ArrayList<>();
    }

    public void addParameter(ParameterNode parameter) {
        parameters.add(parameter);
        parameter.setParent(this);
    }

    public void addParameters(List<ParameterNode> parameters) {
        for (ParameterNode parameter : parameters) {
            addParameter(parameter);
        }
    }

}

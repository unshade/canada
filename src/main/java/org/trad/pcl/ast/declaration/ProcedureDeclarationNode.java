package org.trad.pcl.ast.declaration;

import org.trad.pcl.ast.ParameterNode;
import org.trad.pcl.ast.statement.BlockNode;

import java.util.ArrayList;
import java.util.List;

public final class ProcedureDeclarationNode extends DeclarationNode {
    private List<ParameterNode> parameters;
    private BlockNode body;

    public ProcedureDeclarationNode() {
        this.parameters = new ArrayList<>();
    }

    public void setBody(BlockNode body) {
        this.body = body;
    }

    public void addParameter(ParameterNode parameter) {
        parameters.add(parameter);
    }

    public void addParameters(List<ParameterNode> parameters) {
        for (ParameterNode parameter : parameters) {
            addParameter(parameter);
        }
    }

    public List<ParameterNode> getParameters() {
        return parameters;
    }

    public BlockNode getBody() {
        return body;
    }

}

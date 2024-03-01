package org.trad.pcl.ast.declaration;


import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.ParameterNode;
import org.trad.pcl.ast.statement.BlockNode;
import org.trad.pcl.ast.type.TypeNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.symbol.Function;
import org.trad.pcl.semantic.symbol.Symbol;
import org.trad.pcl.semantic.symbol.Type;

import java.util.ArrayList;
import java.util.List;

public final class FunctionDeclarationNode extends ASTNode implements DeclarationNode{
    private List<ParameterNode> parameters;

    private String identifier;
    private TypeNode returnType;
    private BlockNode body;

    public FunctionDeclarationNode() {
        this.parameters = new ArrayList<>();
    }

    public void addParameter(ParameterNode parameter) {
        parameters.add(parameter);
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void addParameters(List<ParameterNode> parameters) {
        for (ParameterNode parameter : parameters) {
            addParameter(parameter);
        }
    }



    public void setReturnType(TypeNode returnType) {
        this.returnType = returnType;
    }

    public void setBody(BlockNode body) {
        this.body = body;
    }

    public List<ParameterNode> getParameters() {
        return parameters;
    }

    public TypeNode getReturnType() {
        return returnType;
    }

    public BlockNode getBody() {
        return body;
    }

   /* public void initTDS(SymbolTable tdsBefore) {
        body.initTDS(tdsBefore);
        SymbolTable tds = body.getTDS();
        for (ParameterNode parameter : parameters) {
            tds.addSymbol(parameter.toSymbol());
        }
    }

    public void displayTDS() {
        System.out.println("TDS pour la fonction : " + this.identifier);
        this.body.displayTDS();
    }*/

    public Symbol toSymbol() {
        Function f = new Function(this.identifier, 0);
        for (ParameterNode parameter : parameters) {
            f.addParameter(parameter.getVariable().getType().getIdentifier());
        }
        return f;
    }

    @Override
    public String toString() {
        return "FunctionDeclarationNode{" +
                "parameters=" + parameters +
                ", identifier='" + identifier + '\'' +
                ", returnType=" + returnType +
                ", body=" + body +
                '}';
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}

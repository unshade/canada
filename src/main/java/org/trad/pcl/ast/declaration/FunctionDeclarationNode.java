package org.trad.pcl.ast.declaration;


import org.trad.pcl.Exceptions.Semantic.MissingReturnStatementException;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.ParameterNode;
import org.trad.pcl.ast.statement.BlockNode;
import org.trad.pcl.ast.statement.ReturnStatementNode;
import org.trad.pcl.ast.statement.StatementNode;
import org.trad.pcl.ast.type.TypeNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.symbol.Function;
import org.trad.pcl.semantic.symbol.Symbol;

import java.util.ArrayList;
import java.util.List;

public final class FunctionDeclarationNode extends ASTNode implements DeclarationNode{
    private final List<ParameterNode> parameters;

    private String identifier;
    private TypeNode returnType;

    private String endIdentifier;
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

    public void setEndIdentifier(String identifier) {
        this.endIdentifier = identifier;
    }

    public String getEndIdentifier(String identifier){ return this.endIdentifier;}


    public String getIdentifier() {
        return identifier;
    }

    public void addParameters(List<ParameterNode> parameters) {
        for (ParameterNode parameter : parameters) {
            addParameter(parameter);
        }
    }

    public void checkHasReturn() {
        for (StatementNode statement : body.getStatements()) {
            if (statement instanceof ReturnStatementNode) {
                return;
            }
        }
        ErrorService.getInstance().registerSemanticError(new MissingReturnStatementException(this.identifier));
    }

    public void checkEndIdentifier(){
        if (this.endIdentifier == null){
            return;
        }
        if(!this.identifier.equals(this.endIdentifier)){
            ErrorService.getInstance().registerSemanticError(new Exception("End identifier does not match the function identifier : " + this.identifier + " != " + this.endIdentifier));
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
        f.setType(this.returnType.getIdentifier());
        for (ParameterNode parameter : parameters) {
            f.addParameter(parameter.getType().getIdentifier());
        }
        return f;
    }

    public String stringify() {
        return "FunctionDeclarationNode{" +
                "parameters=" + parameters +
                ", identifier='" + identifier + '\'' +
                ", returnType=" + returnType +
                ", body=" + body +
                '}';
    }

    @Override
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }
}

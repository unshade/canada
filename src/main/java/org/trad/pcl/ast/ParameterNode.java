package org.trad.pcl.ast;

import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.Helpers.ParameterMode;
import org.trad.pcl.ast.declaration.VariableDeclarationNode;
import org.trad.pcl.ast.type.TypeNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;
import org.trad.pcl.semantic.StackTDS;
import org.trad.pcl.semantic.symbol.Parameter;
import org.trad.pcl.semantic.symbol.Symbol;
import org.trad.pcl.semantic.symbol.Type;

import java.util.List;
import java.util.Objects;

public final class ParameterNode extends ASTNode {
    private TypeNode type;
    private String identifier;
    private ParameterMode mode;

    public void setMode(ParameterMode mode) {
        this.mode = Objects.requireNonNullElse(mode, ParameterMode.IN);
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setType(TypeNode type) {
        this.type = type;
    }

    public TypeNode getType() {
        return type;
    }

    public Symbol toSymbol() throws UndefinedVariableException {
        Type type = (Type) SemanticAnalysisVisitor.scopeStack.findSymbolInScopes(this.type.getIdentifier(), getConcernedLine());
        Parameter parem = new Parameter(this.identifier, type.getSize());
        parem.setMode(this.mode);
        parem.setType(this.type.getIdentifier());
        return parem;
    }

    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }


}

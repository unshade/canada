package org.trad.pcl.ast;

import org.trad.pcl.ast.declaration.VariableDeclarationNode;
import org.trad.pcl.ast.type.TypeNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.symbol.Parameter;
import org.trad.pcl.semantic.symbol.Symbol;

import java.util.List;

public final class ParameterNode extends ASTNode {
    private TypeNode type;
    private String identifier;
    private String mode;

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setType(TypeNode type) {
        this.type = type;
    }

    public TypeNode getType() {
        return type;
    }

    public Symbol toSymbol() {
        int shift;
        if (this.type.getIdentifier().equals("integer") || this.type.getIdentifier().equals("character")) {
            shift = 4;
        } else {
            // TODO case of a structure, hardcode to 8 for now
            shift = 8;
        }
        Parameter parem = new Parameter(this.identifier, shift);
        parem.setMode(this.mode);
        parem.setType(this.type.getIdentifier());
        return parem;
    }

    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }


}

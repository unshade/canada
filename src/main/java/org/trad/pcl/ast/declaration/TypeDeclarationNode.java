package org.trad.pcl.ast.declaration;

import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.type.TypeNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SymbolTable;
import org.trad.pcl.semantic.symbol.Symbol;
import org.trad.pcl.semantic.symbol.Type;

public final class TypeDeclarationNode extends ASTNode implements DeclarationNode {
    private TypeNode type;

    public void setType(TypeNode type) {
        this.type = type;
    }

    public TypeNode getType() {
        return type;
    }

    public Symbol toSymbol() {
        return new Type(type.getIdentifier(),0);
    }

    public void initTDS(SymbolTable tdsBefore) {
        // Nothing to do
    }

    public void displayTDS() {
        // Nothing to do
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
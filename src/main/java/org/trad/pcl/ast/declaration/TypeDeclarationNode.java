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
        return type.toSymbol();
    }


    @Override
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }
}
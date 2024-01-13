package ast.declaration;

import ast.Ident;

public class Variable implements IDeclaration {
    /**
     * Variable name (identifier)
     */
    private Ident ident;
    /**
     * Variable type
     */
    private Ident type;
    /**
     * Variable expression (initialization)
     */
    private IExpr expr;
}

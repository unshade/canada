package ast.declaration;

import ast.Ident;

import java.util.List;

public class Struct implements IDeclaration {
    /**
     * Struct name (identifier)
     */
    private Ident ident;

    /**
     * Struct variables (fields)
     */
    private List<Variable> variables;
}

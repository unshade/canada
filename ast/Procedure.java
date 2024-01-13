package ast;

import ast.declaration.IDeclaration;

import java.util.List;

public class Procedure {
    /**
     * Procedure name
     */
    private Ident ident;
    /**
     * Procedure declarations
     */
    private List<IDeclaration> decls;

    /**
     * Procedure instructions (body)
     */
    private List<IInstruction> insts;

    /**
     * Procedure parameters
     */
    private List<Param> params;
    /**
     * Procedure end name
     */
    private Ident endIdent;

}

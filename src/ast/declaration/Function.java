package ast.declaration;

import ast.Ident;
import ast.Param;
import ast.declaration.IDeclaration;

import java.util.List;

public class Function implements IDeclaration {
    /**
     * Function name
     */
    private Ident ident;
    /**
     * Function return type
     */
    private Ident type;
    /**
     * Function parameters
     */
    private List<IDeclaration> decls;
    /**
     * Function instructions (body)
     */
    private List<IInstruction> insts;

    /**
     * Function parameters
     */
    private List<Param> params;
    /**
     * Function end name
     */
    private Ident endIdent;
}

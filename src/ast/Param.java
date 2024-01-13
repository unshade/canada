package ast;

public class Param {
    /**
     * Param name (identifier)
     */
    private Ident ident;
    /**
     * Param type
     */
    private Ident type;
    /**
     * Parameter mode (0 = None, 1 = In, 2 = inOut)
     */
    private int mode;
}

package ast2;

public class ASTNode {
    protected ASTNode parent;

    public ASTNode getParent() {
        return parent;
    }

    public void setParent(ASTNode parent) {
        this.parent = parent;
    }
}

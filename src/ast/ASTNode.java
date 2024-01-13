package ast;

public abstract class ASTNode {
    protected ASTNode parent;
    protected int depth;

    public ASTNode getParent() {
        return parent;
    }

    public int getDepth() {
        return parent == null ? 0 : parent.getDepth() + 1;
    }

    public String format(String toFormat) {
        String tab = this.getTab();
        return toFormat.replaceAll("\n", "\n" + tab);
    }
    public String getTab() {
        return "\t".repeat(this.getDepth());
    }
    public void setParent(ASTNode parent) {
        this.parent = parent;
    }
}

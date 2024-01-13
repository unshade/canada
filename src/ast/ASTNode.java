package ast;

import java.lang.reflect.Field;

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

    @Override
    public String toString() {
        Field[] fields = this.getClass().getDeclaredFields();
        String className = this.getClass().getSimpleName();
        StringBuilder res = new StringBuilder(className + " { \n");
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object attributeValue = field.get(this);
                res.append("\t").append(field.getName()).append(" = ").append(attributeValue).append(", \n");
            } catch (IllegalAccessException e) {
                System.err.println("Erreur lors de l'accès au champ " + field.getName());
            }
        }
        res.append("}");
        return format(res.toString());
    }


    public void setParent(ASTNode parent) {
        this.parent = parent;
    }
}

package org.trad.pcl.ast;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;
import java.lang.reflect.Field;

public abstract class ASTNode {
    protected ASTNode parent;
    protected int depth;

    private static boolean isJson = false;

    public ASTNode getParent() {
        return parent;
    }

    public void setParent(ASTNode parent) {
        this.parent = parent;
    }

    public int getDepth() {
        return parent == null ? 0 : parent.getDepth() + 1;
    }

    public String format(String toFormat) {
        String tab = this.getTab();
        return toFormat.replaceAll("\n", "\n" + tab);
    }

    public void setJson(boolean status) {
        isJson = status;
    }

    public String getTab() {
        return "\t".repeat(this.getDepth());
    }

    @Override
    public String toString() {
        Field[] fields = this.getClass().getDeclaredFields();
        String className = this.getClass().getSimpleName();
        StringBuilder res = new StringBuilder(colorize(className, Attribute.YELLOW_TEXT()) + " : { \n");
        if (isJson) {
            res = new StringBuilder("{ \n");
        }
        int lastIndex = fields.length - 1;

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                Object attributeValue = field.get(this);
                if (attributeValue instanceof String) {
                    attributeValue = colorize("\"" + attributeValue + "\"", Attribute.GREEN_TEXT());
                }
                if (attributeValue == null) {
                    attributeValue = colorize("null", Attribute.BRIGHT_MAGENTA_TEXT());
                }
                res.append("\t").append("\"").append(colorize(field.getName(), Attribute.RED_TEXT())).append("\"").append(" : ").append(attributeValue);
                if (i < lastIndex || !isJson) {
                    res.append(",");
                }
                res.append(" \n");

            } catch (IllegalAccessException e) {
                System.err.println("Erreur lors de l'accès au champ " + field.getName());
            }
        }
        res.append("}");
        return format(res.toString());
    }

}

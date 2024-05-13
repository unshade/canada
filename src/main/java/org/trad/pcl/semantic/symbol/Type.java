package org.trad.pcl.semantic.symbol;

import com.diogonunes.jcolor.Attribute;
import org.trad.pcl.Helpers.TypeEnum;

import java.util.Objects;

import static com.diogonunes.jcolor.Ansi.colorize;

public class Type extends Symbol {

    private int size;

    private TypeEnum typeEnum;

    public Type(String identifier, int shift) {
        super(identifier, shift);
    }

    public void setTypeEnum(TypeEnum type) {
        this.typeEnum = type;
    }

    public TypeEnum getTypeEnum() {
        return typeEnum;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Type { " +
                "identifier = '" + colorize(identifier, Attribute.YELLOW_TEXT()) + '\'' +
                ", typeEnum = '" + colorize(typeEnum.toString(), Attribute.YELLOW_TEXT()) + '\'' +
                ", size = " + colorize(Integer.toString(size), Attribute.RED_TEXT()) +
                ", shift = " + colorize(Integer.toString(shift), Attribute.RED_TEXT()) +
                " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return size == type.size && typeEnum == type.typeEnum;
    }
}

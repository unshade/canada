package org.trad.pcl.Helpers;

public enum TypeEnum {

    INT("integer"),
    BOOL("boolean"),
    CHAR("character"),
    RECORD("record"),
    VOID("void"),
    UNKNOWN("unknown")
    ;

    private final String type;

    TypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}

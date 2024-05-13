package org.trad.pcl.asm;

import org.trad.pcl.semantic.symbol.Record;

public class ASMUtils {

    public static String saveRecordInStack(Record record) {
        return "sub esp, " + record.getSize();
    }
}

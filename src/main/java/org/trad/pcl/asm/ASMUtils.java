package org.trad.pcl.asm;

import org.trad.pcl.semantic.symbol.Record;
import org.trad.pcl.semantic.symbol.Variable;

public class ASMUtils {

    /**
     * Copy a record from the source(R1) to the destination(R2)
     * @param record
     * @return
     */
    public static String saveRecordInStack(Record record) {
        StringBuilder sb = new StringBuilder();
        for (Variable var : record.getFields()) {
            sb.append("""
                            \t LDR     R0, [R1, #%s] ; Load field in R0
                            \t STR     R0, [R2, #%s] ; Store return value for in stack-frame
                            """.formatted(var.getShift(), 2 * 4 + record.getSize() - var.getShift()));
        }
        return sb.toString();

    }
}

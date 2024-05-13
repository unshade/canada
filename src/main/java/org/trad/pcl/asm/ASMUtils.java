package org.trad.pcl.asm;

import org.trad.pcl.semantic.symbol.Record;
import org.trad.pcl.semantic.symbol.Type;
import org.trad.pcl.semantic.symbol.Variable;

public class ASMUtils {

    /**
     * Copy a record from the source(R1) to the destination(R2)
     * @param record
     * @return
     */
    public static String saveRecordInStack(Record record,int shift) {
        StringBuilder sb = new StringBuilder();
        for (Variable var : record.getFields()) {
            if((Type) ASMGenerator.scopeStack.findSymbolInScopes(var.getType()) instanceof Record recordType) {
                sb.append(saveRecordInStack(recordType, shift + var.getShift()));
            } else {
                sb.append("""
                        \t LDR     R0, [R1, #-%s] ; Load field in R0
                        \t STR     R0, [R2, #-%s] ; Store return value for in stack-frame
                        """.formatted(shift + var.getShift(), shift + var.getShift()));
            }
        }
        return sb.toString();

    }
}

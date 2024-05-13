package org.trad.pcl.asm;

import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.ast.expression.VariableReferenceNode;
import org.trad.pcl.semantic.symbol.Record;
import org.trad.pcl.semantic.symbol.Symbol;
import org.trad.pcl.semantic.symbol.Type;
import org.trad.pcl.semantic.symbol.Variable;

import static org.trad.pcl.asm.ASMGenerator.scopeStack;

public class ASMUtils {

    /**
     * Copy a record from the source(R1) to the destination(R2)
     * @param record
     * @return
     */
    public static String saveRecordInStack(Record record,int shift) {
        StringBuilder sb = new StringBuilder();
        for (Variable var : record.getFields()) {
            if((Type) scopeStack.findSymbolInScopes(var.getType()) instanceof Record recordType) {
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

    /**
     * Find the address of a variable in the scope and put it in R10
     *
     * @param identifier
     */
    public static String findAddress(String identifier) {
        StringBuilder sb = new StringBuilder();
        int depth = findDepthInScopes(identifier);
        if (depth == 0) {
            sb.append("""
                    \t MOV     R9, R10
                    """);
            return sb.toString();
        }

        sb.append("""
                \t LDR     R9, [R11, #4]
                """);

        // tkt c'est un for i < depth-1
        sb.append("""
                \t LDR     R9, [R9]
                """.repeat(Math.max(0, depth - 1)));
        return sb.toString();
    }

    public static String findVariableAddress(String identifier, VariableReferenceNode nextExpression) {
        StringBuilder sb = new StringBuilder();
        int depth = findDepthInScopes(identifier);
        // go to the variable address
        if (depth == 0) {
            sb.append("""
                    \t MOV     R9, R11
                    """);

        } else {

            sb.append("""
                    \t LDR     R9, [R11, #4]
                    """);

            // tkt c'est un for i < depth-1
            sb.append("""
                    \t LDR     R9, [R9]
                    """.repeat(Math.max(0, depth - 1)));

            sb.append("""
                     \t SUB     R9, R9, #4
                    """);
        }
        Variable variable = (Variable) scopeStack.findSymbolInScopes(identifier);
        assert variable != null;
        int shift = variable.getShift();
        String typeIdent = variable.getType();
        while (nextExpression != null) {
            Record record = (Record) scopeStack.findSymbolInScopes(typeIdent);
            assert record != null;
            Variable field = record.getField(nextExpression.getIdentifier());
            shift += field.getShift();
            typeIdent = field.getType();
            nextExpression = nextExpression.getNextExpression();
        }
        sb.append("""
                \t SUB     R9, R9, #%s
                """.formatted(shift));
        return sb.toString();
    }

    public static int findDepthInScopes(String identifier) {
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Symbol s = scopeStack.get(i).findSymbol(identifier);
            if (s != null) {
                return scopeStack.size() - i - 1;
            }
        }

        assert false : "Variable " + identifier + " not found in any scope";
        return -1;
    }

    /**
     * Pour chaque champs du record, on test si les deux sont égaux en ASM
     * @param record
     * @param left
     * @param right
     * @param shift
     * @return
     */
    public static String equalsRecord(Record record, String left, String right, int shift) {
        StringBuilder sb = new StringBuilder();
        for (Variable field : record.getFields()) {
            Type type = (Type) scopeStack.findSymbolInScopes(field.getType());
            if (type instanceof Record subRecord) {
                sb.append(equalsRecord(subRecord, left, right, field.getShift() + shift));
            } else {
                // On va chercher le champ pour left dans la pile avec findVariableAddress
                sb.append(findVariableAddress(left, null));
                sb.append("""
                        \t LDR     R1, [R9, #-%s] ; Load variable %s in R0
                        """.formatted(field.getShift() + shift, left));
                // On va chercher le champ pour right dans la pile avec findVariableAddress
                sb.append(findVariableAddress(right, null));
                sb.append("""
                        \t LDR     R2, [R9, #-%s] ; Load variable %s in R1
                        """.formatted(field.getShift() + shift, right));
                // On compare les deux champs
                sb.append("""
                        \t CMP     R1, R2 ; Compare operands
                        \t MOVNE   R0, #0 ; Set R2 to 1 if operands are not equal
                        """);
            }
        }
        return sb.toString();
    }
}

package org.trad.pcl.asm;

import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.ast.ParameterNode;
import org.trad.pcl.ast.ProgramNode;
import org.trad.pcl.ast.declaration.*;
import org.trad.pcl.ast.expression.*;
import org.trad.pcl.ast.statement.*;
import org.trad.pcl.ast.type.TypeNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SymbolTable;
import org.trad.pcl.semantic.symbol.Symbol;

import java.util.ArrayList;
import java.util.List;

public final class ASMGenerator implements ASTNodeVisitor {

    private final List<SymbolTable> symbolTables;

    private final StringBuilder output;

    public ASMGenerator(List<SymbolTable> symbolTables) {
        this.symbolTables = symbolTables;
        output = new StringBuilder();
    }

    public String getOutput() {
        return this.output.toString();
    }

    public Symbol findSymbolInScopes(String identifier) {
        for (SymbolTable symbolTable : this.symbolTables) {
            Symbol symbol = symbolTable.findSymbol(identifier);
            if (symbol != null) {
                return symbol;
            }
        }
        return null;
    }

    public Integer findSymbolInScopes(String FunctionOrProcedureName, String identifier) {
        SymbolTable symbolTableFunction = this.findSymbolTable(FunctionOrProcedureName);
        return findSymbolInScopesRecursive(symbolTableFunction, identifier, 0);
    }

    public Integer findSymbolInScopesRecursive(SymbolTable symbolTableFunction, String identifier, int shift) {
        Symbol symbol = symbolTableFunction.findSymbol(identifier);
        if (symbol == null) {
            String scopeIdentifier = symbolTableFunction.getScopeIdentifier();
            SymbolTable parentSymbolTable = findTableSymbol(scopeIdentifier);
            shift += parentSymbolTable.findSymbol(scopeIdentifier).getShift();
            shift = findSymbolInScopesRecursive(parentSymbolTable, identifier, shift);
            return shift;
        } else {
            return shift - symbol.getShift();
        }
    }

    private SymbolTable findSymbolTable(String functionOrProcedureName) {
        for (SymbolTable symbolTable : this.symbolTables) {
            if (symbolTable.getScopeIdentifier().equals(functionOrProcedureName)) {
                return symbolTable;
            }
        }
        return null;
    }

    private SymbolTable findTableSymbol(String SymbolIdentifier) {
        for (SymbolTable symbolTable : this.symbolTables) {
            if (symbolTable.findSymbol(SymbolIdentifier) != null) {
                return symbolTable;
            }
        }
        return null;
    }

    @Override
    public void visit(FunctionDeclarationNode node) throws Exception {
        Symbol symbol = this.findSymbolInScopes(node.getIdentifier());
        this.output.append(symbol.getIdentifier()).append("\n").append("""
                \t STMFD   R13!, {R11, LR} ; Save caller's (%s) frame pointer and return ASM address
                \t MOV     R11, R13 ; Set up new frame pointer
                """.formatted(Context.background().getCallerName()));
        Context.background().setCounter(node.getParameters().size());
        node.getParameters().forEach(param -> {
            try {
                param.accept(this);
                Context.background().setCounter(Context.background().getCounter() - 1);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        Context.background().setCallerName(node.getIdentifier());
        node.getBody().accept(this);
    }

    @Override
    public void visit(ProcedureDeclarationNode node) throws Exception {
        Symbol symbol = this.findSymbolInScopes(node.getIdentifier());
        this.output.append(symbol.getIdentifier()).append("\n").append("""
                \t STMFD   R13!, {R11, LR} ; Save caller's (%s) frame pointer and return ASM address
                \t MOV     R11, R13 ; Set up new frame pointer
                """.formatted(Context.background().getCallerName()));
        Context.background().setCounter(node.getParameters().size());
        node.getParameters().forEach(param -> {
            try {
                param.accept(this);
                Context.background().setCounter(Context.background().getCounter() - 1);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        Context.background().setCallerName(node.getIdentifier());
        node.getBody().accept(this);
    }

    @Override
    public void visit(TypeDeclarationNode node) throws Exception {

    }

    @Override
    public void visit(VariableDeclarationNode node) throws Exception {
        /*int lineToWrite = Context.background().getNonCallableDeclarationWriteLine();

        String[] outputLines = this.output.toString().split("\\r?\\n");
        StringBuilder newOutput = new StringBuilder();

        for (int i = 0; i < outputLines.length; i++) {
            newOutput.append(outputLines[i]).append("\n");

            if (i == lineToWrite - 1) {
                String formattedCode = String.format("\t SUB     R13, R13, #4 ; Save space for %s in stack-frame", node.getIdentifier());
                newOutput.append(formattedCode).append("\n");
            }
        }*/
        String formattedCode = String.format("\t SUB     R13, R13, #4 ; Save space for %s in stack-frame", node.getIdentifier());
        this.output.append(formattedCode).append("\n");

        //TODO : assignement


        //this.output = newOutput;
    }


    @Override
    public void visit(AssignmentStatementNode node) throws Exception {
        node.getExpression().accept(this);
        this.output.append("""
                \t STR     R0, [R11, #%s] ; Assign right expression (assuming result is in R0) to left variable %s
                """.formatted(findSymbolInScopes(Context.background().getCallerName(), node.getVariableReference().getIdentifier()), node.getVariableReference().getIdentifier()));
    }

    @Override
    public void visit(BlockNode node) {
        List<DeclarationNode> tempDeclarations = new ArrayList<>();
        node.getDeclarations().forEach(declaration -> {
            try {
                if (declaration instanceof ProcedureDeclarationNode || declaration instanceof FunctionDeclarationNode) {
                    tempDeclarations.add(declaration);
                } else {
                    declaration.accept(this);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        node.getStatements().forEach(statement -> {
            try {
                statement.accept(this);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        tempDeclarations.forEach(declaration -> {
            try {
                declaration.accept(this);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void visit(CallNode node) throws Exception {
        Symbol symbol = this.findSymbolInScopes(node.getIdentifier());
        node.getArguments().forEach(arg -> {
            try {
                arg.accept(this);
                this.output.append(""" 
                        \t STMFD   R13!, {R0} ; Save argument
                        """);

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        this.output.append("""
                \t SUB     R13, R13, #4 ; Save space for return value
                \t BL      %s ; Branch link to %s (it will save the return address in LR)
                \t LDR     R0, [R13], #4 ; Load return value
                """.formatted(symbol.getIdentifier(), symbol.getIdentifier()));
    }

    @Override
    public void visit(IfStatementNode node) throws Exception {
        String ifTrueLabel = "if_true_" + Context.background().getUniqueLabelId();
        String ifEndLabel = "if_end_" + Context.background().getUniqueLabelId();

        node.getCondition().accept(this);

        this.output.append("""
                \t CMP     R0, #0 ; Compare condition
                \t BEQ     %s ; Branch if condition is false
                """.formatted(ifEndLabel));

        if (node.getElseBranch() != null) {
            node.getElseBranch().accept(this);
        }

        output.append("\t B       ").append(ifEndLabel).append("\n");

        output.append(ifTrueLabel).append("\n");

        node.getThenBranch().accept(this);

        output.append(ifEndLabel).append("\n");
    }

    @Override
    public void visit(LoopStatementNode node) throws Exception {

    }

    @Override
    public void visit(ReturnStatementNode node) throws Exception {
        this.output.append("""
                \t MOV     R0, #0 ; Clear R0
                \t MOV     R5, #0 ; Clear R5
                """);
        node.getExpression().accept(this);
        this.output.append("""
                \t STR     R0, [R11, #4 * 2] ; Store return value for (%s) in stack-frame
                \t MOV     R13, R11 ; Restore frame pointer
                \t LDMFD   R13!, {R11, PC} ; Restore caller's (%s) frame pointer and return ASM address
                """.formatted(Context.background().getCallerName(), Context.background().getCallerName()));
    }

    @Override
    public void visit(WhileStatementNode node) throws Exception {
        //TODO move loop logic and condition inside their respective visits
        String loopStartLabel = "loop_start_" + Context.background().getUniqueLabelId();
        String loopEndLabel = "loop_end_" + Context.background().getUniqueLabelId();

        output.append(loopStartLabel).append("\n");

        node.getCondition().accept(this);

        this.output.append("""
                \t CMP     R0, #0 ; Compare condition
                \t BEQ     %s ; Branch if condition is false
                """.formatted(loopEndLabel));

        node.getBody().accept(this);

        output.append("\t B       ").append(loopStartLabel).append("\n");

        output.append(loopEndLabel).append("\n");
    }

    @Override
    public void visit(BinaryExpressionNode node) throws Exception {
        Context.background().setLeftOperand(false);
        node.getRight().accept(this);
        Context.background().setLeftOperand(true);
        node.getLeft().accept(this);
        switch (node.getOperatorNode().getOperator()) {
            case ADD -> output.append("\t ADD     R0, R0, R1 ; Add operands\n");
            case SUB -> output.append("\t SUB     R0, R0, R1 ; Sub operands\n");
        }
    }

    @Override
    public void visit(CharacterValExpressionNode node) throws Exception {

    }

    @Override
    public void visit(LiteralNode node) {
        if (Context.background().isLeftOperand()) {
            this.output.append("\t MOV     R1, #%s ; Load literal value in R1\n".formatted(node.getValue()));
        } else {
            this.output.append("\t MOV     R0, #%s ; Load literal value in R0\n".formatted(node.getValue()));
        }
    }

    @Override
    public void visit(VariableReferenceNode node) throws Exception {
        System.out.println("Context: " + Context.background().getCallerName());
        Integer shift = this.findSymbolInScopes(Context.background().getCallerName(), node.getIdentifier());
        this.output.append("""
                \t LDR     R0, [R11, #%s] ; Load variable %s in R0
                """.formatted(shift, node.getIdentifier()));
    }

    @Override
    public void visit(NewExpressionNode node) throws UndefinedVariableException {

    }

    @Override
    public void visit(UnaryExpressionNode node) throws Exception {
        node.getOperator().accept(this);
        node.getOperand().accept(this);
    }

    @Override
    public void visit(TypeNode node) throws Exception {
        // TODO FINAL NODE
    }

    @Override
    public void visit(ProgramNode node) {
        // TODO Find a way to do this clearly and not to get var decl at the bottom
        try {
            Symbol symbol = this.findSymbolInScopes(node.getRootProcedure().getIdentifier());
            Context.background().setCallerName(symbol.getIdentifier());
            this.output.append(symbol.getIdentifier()).append("\n").append("""
                    \t STMFD   R13!, {R11, LR} ; Main environment setup
                    \t MOV     R11, R13 ; Set up new frame pointer
                    """);
            //this.updateContextNonCallableDeclaration();
           /* node.getRootProcedure().getBody().getStatements().forEach(statementNode -> {
                try {
                    statementNode.accept(this);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    System.exit(1);
                }
            });*/
            List<DeclarationNode> tempDeclarations = new ArrayList<>();
            node.getRootProcedure().getBody().getDeclarations().forEach(declaration -> {
                try {
                    if (declaration instanceof ProcedureDeclarationNode || declaration instanceof FunctionDeclarationNode) {
                        tempDeclarations.add(declaration);
                    } else {
                        declaration.accept(this);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            node.getRootProcedure().getBody().getStatements().forEach(statement -> {
                try {
                    statement.accept(this);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            this.output.append("\t END     ; Program ends here\n");

            tempDeclarations.forEach(declaration -> {
                try {
                    declaration.accept(this);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            /*node.getRootProcedure().getBody().getDeclarations().forEach(declarationNode -> {
                try {
                    declarationNode.accept(this);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    System.exit(1);
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    @Override
    public void visit(ParameterNode node) throws Exception {
        node.getType().accept(this);
        int shift = Context.background().getCounter() + 2;
        this.output.append("""
                \t LDR     R5, [R11, #4 * %s] ; Load parameter %s in R5
                \t STMFD   R13!, {R5} ; Store parameter %s in stack-frame
                """.formatted(shift, node.getIdentifier(), node.getIdentifier()));
    }
}

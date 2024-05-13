package org.trad.pcl.ast.expression;

import org.trad.pcl.Exceptions.Semantic.NonRecordTypeException;
import org.trad.pcl.Exceptions.Semantic.UndefinedFieldException;
import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.statement.IdentifiableStatement;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;
import org.trad.pcl.semantic.StackTDS;
import org.trad.pcl.semantic.symbol.Record;
import org.trad.pcl.semantic.symbol.Symbol;
import org.trad.pcl.semantic.symbol.Variable;
public class VariableReferenceNode extends ASTNode implements IdentifiableExpression, IdentifiableStatement {
    private String identifier;

    private VariableReferenceNode nextExpression;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public VariableReferenceNode getNextExpression() {
        return nextExpression;
    }

    public void setNextExpression(VariableReferenceNode nextExpression) {
        this.nextExpression = nextExpression;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    public void checkVariableReferenceAccess(String typeIdent) throws Exception {
        VariableReferenceNode nextExpression = this.getNextExpression();

        while(nextExpression != null) {
            Symbol type = SemanticAnalysisVisitor.scopeStack.findSymbolInScopes(typeIdent, this.getConcernedLine());
            if (!(type instanceof Record)) {
                throw new NonRecordTypeException(typeIdent, this.getConcernedLine());
            }
            Variable field = ((Record) type).getField(nextExpression.getIdentifier());
            if (field == null) {
                throw new UndefinedFieldException(nextExpression.getIdentifier(), typeIdent, this.getConcernedLine());
            }

            nextExpression = nextExpression.getNextExpression();
            typeIdent = field.getType();

        }
    }

    @Override
    public String getType(StackTDS stack) throws UndefinedVariableException {
        Variable variableExpression = (Variable) stack.findSymbolInScopes(this.getIdentifier(), this.getConcernedLine());

        String type = variableExpression.getType();

        if (this.nextExpression != null) {

                VariableReferenceNode next = this.nextExpression;
                while (next != null) {
                    Record recordType = (Record) stack.findSymbolInScopes(type, this.getConcernedLine());
                    type = recordType.getField(next.getIdentifier()).getType();
                    next = next.getNextExpression();
                }
        }
        return type;
    }

}

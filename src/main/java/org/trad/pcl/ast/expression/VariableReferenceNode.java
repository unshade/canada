package org.trad.pcl.ast.expression;

import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.statement.IdentifiableStatement;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;
import org.trad.pcl.semantic.symbol.Record;
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

    @Override
    public String getType() throws UndefinedVariableException {
        Variable variableExpression = (Variable) SemanticAnalysisVisitor.findSymbolInScopes(this.getIdentifier());

        String type = variableExpression.getType();

        if (this.nextExpression != null) {

                VariableReferenceNode next = this.nextExpression;
                while (next != null) {
                    Record recordType = (Record) SemanticAnalysisVisitor.findSymbolInScopes(type);
                    assert recordType != null;
                    type = recordType.getField(next.getIdentifier()).getType();
                    next = next.getNextExpression();
                }
            System.out.println("VariableReferenceNode: " + type);

        }
        return type;
    }
}

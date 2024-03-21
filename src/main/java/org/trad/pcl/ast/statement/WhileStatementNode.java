package org.trad.pcl.ast.statement;

import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.expression.ExpressionNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

public final class WhileStatementNode extends ASTNode implements StatementNode {

        private BlockNode body;


        private ExpressionNode condition;


        public void setBody(BlockNode body) {
            this.body = body;
        }

        public void setCondition(ExpressionNode condition) {
            this.condition = condition;
        }

    public BlockNode getBody() {
        return body;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }
}

package org.trad.pcl.ast.statement;

import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.expression.ExpressionNode;
import org.trad.pcl.semantic.ASTNodeVisitor;

public final class LoopStatementNode extends ASTNode implements StatementNode {

        private BlockNode body;

        private String identifier;
        private boolean isReverse;

        private ExpressionNode startExpression;

        private ExpressionNode endExpression;

        public void setReverse(boolean isReverse) {
            this.isReverse = isReverse;
        }

        public void setBody(BlockNode body) {
            this.body = body;
        }

        public void setStartExpression(ExpressionNode startExpression) {
            this.startExpression = startExpression;
        }

        public void setEndExpression(ExpressionNode endExpression) {
            this.endExpression = endExpression;
        }

        public BlockNode getBody() {
            return body;
        }

        public boolean isReverse() {
            return isReverse;
        }

        public ExpressionNode getStartExpression() {
            return startExpression;
        }

        public ExpressionNode getEndExpression() {
            return endExpression;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return identifier;
        }


    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}

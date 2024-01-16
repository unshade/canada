package org.trad.pcl.ast.statement;

import org.trad.pcl.ast.expression.ExpressionNode;

public final class LoopStatementNode extends StatementNode {

        private BlockNode body;

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


}

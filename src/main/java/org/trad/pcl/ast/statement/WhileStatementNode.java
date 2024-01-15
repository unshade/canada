package org.trad.pcl.ast.statement;

import org.trad.pcl.ast.expression.ExpressionNode;

public class WhileStatementNode extends StatementNode {

        private BlockNode body;


        private ExpressionNode condition;


        public void setBody(BlockNode body) {
            this.body = body;
        }

        public void setCondition(ExpressionNode condition) {
            this.condition = condition;
        }


}

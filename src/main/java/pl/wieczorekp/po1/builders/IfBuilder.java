package pl.wieczorekp.po1.builders;

import pl.wieczorekp.po1.instructions.expressions.Expression;
import pl.wieczorekp.po1.instructions.statements.CodeBlock;
import pl.wieczorekp.po1.instructions.statements.IfStatement;

public class IfBuilder {
    Expression leftOperand;
    Expression rightOperand;
    IfStatement.Condition condition;
    CodeBlock ifBranchBlock;
    CodeBlock elseBranchBlock;

    public IfBuilder() {
    }

    public IfBuilder leftOperand(Expression expression) {
        this.leftOperand = expression;
        return this;
    }

    public IfBuilder rightOperand(Expression expression) {
        this.rightOperand = expression;
        return this;
    }

    public IfBuilder condition(IfStatement.Condition condition) {
        this.condition = condition;
        return this;
    }

    public IfBuilder ifTrue(CodeBlock block) {
        if (block == null) {
            throw new NullPointerException("If true block cannot be null");
        }
        this.ifBranchBlock = block;
        return this;
    }

    public IfBuilder ifFalse(CodeBlock block) {
        this.elseBranchBlock = block;
        return this;
    }

    public IfStatement build() {
        return new IfStatement(null, leftOperand, rightOperand, condition, ifBranchBlock, elseBranchBlock);
    }
}

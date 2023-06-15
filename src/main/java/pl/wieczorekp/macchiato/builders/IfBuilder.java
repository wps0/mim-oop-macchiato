package pl.wieczorekp.macchiato.builders;

import pl.wieczorekp.macchiato.instructions.statements.CodeBlock;
import pl.wieczorekp.macchiato.instructions.statements.IfStatement;
import pl.wieczorekp.macchiato.instructions.expressions.Expression;

public class IfBuilder {
    private Expression leftOperand;
    private Expression rightOperand;
    private IfStatement.Condition condition;
    private CodeBlock ifBranchBlock;
    private CodeBlock elseBranchBlock;

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
        ifBranchBlock = block;
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

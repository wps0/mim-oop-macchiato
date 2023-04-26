package pl.wieczorekp.po1.instructions.statements;

import lombok.NonNull;
import pl.wieczorekp.po1.instructions.ExecutionEndedException;
import pl.wieczorekp.po1.instructions.expressions.Expression;

import java.util.function.BiPredicate;

public class IfStatement extends Statement {
    private Expression leftOperand;
    private Expression rightOperand;
    private Condition condition;
    private CodeBlock ifBranchBlock;
    private CodeBlock elseBranchBlock;
    private Branch branch;

    protected IfStatement(CodeBlock context,
                          Expression leftOperand,
                          Expression rightOperand,
                          Condition condition,
                          @NonNull CodeBlock ifBranchBlock,
                          CodeBlock elseBranchBlock) {
        super(context);
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.condition = condition;
        this.ifBranchBlock = ifBranchBlock;
        this.elseBranchBlock = elseBranchBlock;
        this.branch = Branch.CONDITION;
    }

    @Override
    public void executeOne() {
        if (branch == Branch.EXECUTION_FINISHED) {
            throw new ExecutionEndedException();
        }

        if (branch == Branch.CONDITION) {
            Integer exp1 = leftOperand.evaluate();
            Integer exp2 = rightOperand.evaluate();
            branch = condition.test(exp1, exp2) ? Branch.IF_BLOCK : Branch.ELSE_BLOCK;
            return;
        }

        CodeBlock executionBranch = branch == Branch.IF_BLOCK ? ifBranchBlock : elseBranchBlock;
        if (executionBranch != null) {
            executionBranch.executeOne();
            if (executionBranch.hasEnded()) {
                branch = Branch.EXECUTION_FINISHED;
            }
        }
    }

    @Override
    protected boolean hasEnded() {
        return branch == Branch.EXECUTION_FINISHED;
    }

    public enum Branch {
        CONDITION, IF_BLOCK, ELSE_BLOCK, EXECUTION_FINISHED;
    }

    public enum Condition {
        EQ(Integer::equals),
        NEQ((a, b) -> !a.equals(b)),
        GREATER((a, b) -> a > b),
        LESS((a, b) -> a < b),
        GEQ((a, b) -> a >= b),
        LEQ((a, b) -> a <= b);

        private BiPredicate<Integer, Integer> condition;

        Condition(BiPredicate<Integer, Integer> condition) {
            this.condition = condition;
        }

        public boolean test(Integer a, Integer b) {
            return this.condition.test(a, b);
        }
    }
}

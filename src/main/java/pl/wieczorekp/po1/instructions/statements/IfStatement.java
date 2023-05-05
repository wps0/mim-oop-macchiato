package pl.wieczorekp.po1.instructions.statements;

import pl.wieczorekp.po1.instructions.expressions.Expression;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

public class IfStatement extends BlockStatement {
    private final Expression leftOperand;
    private final Expression rightOperand;
    private final Condition condition;
    private final CodeBlock ifBranchBlock;
    private final CodeBlock elseBranchBlock;
    private Branch branch;

    public IfStatement(CodeBlock context,
                       Expression leftOperand,
                       Expression rightOperand,
                       Condition condition,
                       CodeBlock ifBranchBlock,
                       CodeBlock elseBranchBlock) {
        super(context);
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.condition = condition;
        this.ifBranchBlock = Objects.requireNonNull(ifBranchBlock);
        this.elseBranchBlock = elseBranchBlock;
        this.branch = Branch.CONDITION;
    }

    private CodeBlock getExecutionBranch() {
        return branch == Branch.IF_BLOCK ? ifBranchBlock : elseBranchBlock;
    }

    @Override
    public void executeOne() {
        if (hasEnded()) {
            branch = Branch.CONDITION;
        }
        if (branch == Branch.CONDITION) {
            Integer exp1 = leftOperand.evaluateInContext(context);
            Integer exp2 = rightOperand.evaluateInContext(context);
            branch = condition.test(exp1, exp2) ? Branch.IF_BLOCK : Branch.ELSE_BLOCK;
            return;
        }

        CodeBlock executionBranch = getExecutionBranch();
        if (executionBranch != null) {
            executionBranch.executeOne();
            if (executionBranch.hasEnded()) {
                branch = Branch.EXECUTION_FINISHED;
                executionBranch.setInstructionPointer(0);
            }
        } else {
            branch = Branch.EXECUTION_FINISHED;
        }
    }

    @Override
    public boolean hasEnded() {
        return branch == Branch.EXECUTION_FINISHED;
    }

    @Override
    public Optional<Statement> getCurrentStatement(boolean shiftIP) {
        if (branch == Branch.CONDITION || hasEnded()) {
            return Optional.of(this);
        }
        return getExecutionBranch() == null ? Optional.of(this) : getExecutionBranch().getCurrentStatement(shiftIP);
    }

    @Override
    public String toString() {
        String prefix = "  ".repeat(context.getNestedness()+1);
        StringBuilder asString = new StringBuilder();

        asString.append("if ").append(leftOperand).append(" ").append(condition).append(" ").append(rightOperand)
                .append(" (branch: ").append(branch).append(")\n")
                .append(ifBranchBlock);
        if (elseBranchBlock != null) {
                asString.append(prefix).append("else\n")
                        .append(elseBranchBlock);
        }

        return asString.toString();
    }

    public enum Branch {
        CONDITION, IF_BLOCK, ELSE_BLOCK, EXECUTION_FINISHED
    }

    public enum Condition {
        EQ(Integer::equals),
        NEQ((a, b) -> !a.equals(b)),
        GREATER((a, b) -> a > b),
        LESS((a, b) -> a < b),
        GEQ((a, b) -> a >= b),
        LEQ((a, b) -> a <= b);

        private final BiPredicate<Integer, Integer> condition;

        Condition(BiPredicate<Integer, Integer> condition) {
            this.condition = condition;
        }

        public boolean test(Integer a, Integer b) {
            return this.condition.test(a, b);
        }
    }
}

package pl.wieczorekp.po1.instructions.expressions;

import pl.wieczorekp.po1.instructions.Instruction;
import pl.wieczorekp.po1.instructions.statements.CodeBlock;

/**
 * Expressions are evaluated lazily.
 */
public abstract class Expression extends Instruction {
    protected Expression(CodeBlock context) {
        super(context);
    }

    public abstract Integer evaluate();

    public Integer evaluateInContext(CodeBlock newContext) {
        CodeBlock origContext = getContext();
        switchContext(newContext);

        Integer result = evaluate();

        switchContext(origContext);
        return result;
    }
}

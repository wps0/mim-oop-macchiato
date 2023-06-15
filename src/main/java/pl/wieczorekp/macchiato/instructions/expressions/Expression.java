package pl.wieczorekp.macchiato.instructions.expressions;

import pl.wieczorekp.macchiato.instructions.statements.CodeBlock;

/**
 * Expressions are evaluated lazily.
 */
public interface Expression {
    /**
     * Evaluates the expression. If an error occurred, an exception should be thrown.
     */
    Integer evaluateInContext(CodeBlock context);
}

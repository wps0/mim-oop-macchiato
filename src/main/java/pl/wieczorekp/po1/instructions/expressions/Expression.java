package pl.wieczorekp.po1.instructions.expressions;

import pl.wieczorekp.po1.instructions.statements.CodeBlock;

import java.util.Optional;

/**
 * Expressions are evaluated lazily.
 */
public interface Expression {
    /**
     * Evaluates the expression. If an error occurred, an exception should be thrown.
     */
    Integer evaluateInContext(CodeBlock context);
}

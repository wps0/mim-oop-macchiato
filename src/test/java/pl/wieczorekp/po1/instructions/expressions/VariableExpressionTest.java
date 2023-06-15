package pl.wieczorekp.po1.instructions.expressions;

import org.junit.jupiter.api.Test;
import pl.wieczorekp.macchiato.instructions.UndefinedIdentifierException;
import pl.wieczorekp.macchiato.instructions.expressions.VariableExpression;
import pl.wieczorekp.macchiato.instructions.statements.CodeBlock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VariableExpressionTest {
    private static final String VARIABLE_A = "variable_a";

    @Test
    void shouldEvaluateInNonEmptyContext() {
        // given
        CodeBlock context = new CodeBlock(null);
        context.declareVariable(VARIABLE_A, -31);
        VariableExpression variableExpression = new VariableExpression(VARIABLE_A);

        // when
        Integer evaluated = variableExpression.evaluateInContext(context);

        // then
        assertEquals(-31, evaluated);
    }

    @Test
    void shouldThrowAnExceptionInEmptyContext() {
        // given
        CodeBlock context = new CodeBlock(null);
        VariableExpression variableExpression = new VariableExpression(VARIABLE_A);

        // when & then
        assertThrows(UndefinedIdentifierException.class, () -> variableExpression.evaluateInContext(context));
    }
}
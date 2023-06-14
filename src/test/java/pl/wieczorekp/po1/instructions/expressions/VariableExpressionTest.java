package pl.wieczorekp.po1.instructions.expressions;

import org.junit.jupiter.api.Test;
import pl.wieczorekp.po1.instructions.UndefinedIdentifierException;
import pl.wieczorekp.po1.instructions.statements.CodeBlock;

import static org.junit.jupiter.api.Assertions.*;

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
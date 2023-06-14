package pl.wieczorekp.po1.instructions.expressions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pl.wieczorekp.po1.instructions.statements.CodeBlock;

import static org.junit.jupiter.api.Assertions.*;

class LiteralExpressionTest {

    @ParameterizedTest
    @ValueSource(ints = {3, 0, -1, 100})
    void shouldEvaluateInEmptyContext(Integer value) {
        // given
        CodeBlock context = new CodeBlock(null);
        LiteralExpression literal = new LiteralExpression(value);

        // when
        Integer evaluated = literal.evaluateInContext(context);

        // then
        assertEquals(value, evaluated);
    }
}
package pl.wieczorekp.po1.instructions.expressions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.wieczorekp.macchiato.instructions.expressions.DivExpression;
import pl.wieczorekp.macchiato.instructions.expressions.LiteralExpression;
import pl.wieczorekp.macchiato.instructions.statements.CodeBlock;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DivExpressionTest {

    @ParameterizedTest
    @MethodSource("provideTwoNonzeroIntsForEvaluateInContext")
    void givenTwoNonNullableIntegersShouldEvaluateInEmptyContext(Integer a, Integer b) {
        // given
        CodeBlock context = new CodeBlock(null);
        LiteralExpression aLiteral = LiteralExpression.of(a);
        LiteralExpression bLiteral = LiteralExpression.of(b);
        DivExpression div = DivExpression.of(aLiteral, bLiteral);

        // when
        Integer result = div.evaluateInContext(context);

        // then
        assertEquals(a/b, result);
    }

    @ParameterizedTest
    @MethodSource("provideTwoNullIntsForEvaluateInContext")
    void givenTwoNullableIntegersShouldEvaluateInEmptyContext(Integer a, Integer b) {
        // given
        CodeBlock context = new CodeBlock(null);
        LiteralExpression aLiteral = LiteralExpression.of(a);
        LiteralExpression bLiteral = LiteralExpression.of(b);
        DivExpression div = DivExpression.of(aLiteral, bLiteral);

        // when
        if (b == null || b == 0) {
            assertThrows(ArithmeticException.class, () -> div.evaluateInContext(context));
        } else {
            Integer result = div.evaluateInContext(context);

            // then
            assertEquals(a / b, result);
        }
    }

    private static Stream<Arguments> provideTwoNonzeroIntsForEvaluateInContext() {
        return Stream.of(
                Arguments.of(-1000, 321),
                Arguments.of(1000, -321),
                Arguments.of(-1000, -321),
                Arguments.of(3, 1),
                Arguments.of(1, 3),
                Arguments.of(7, 441),
                Arguments.of(441, 7),
                Arguments.of(8, 2)
        );
    }

    private static Stream<Arguments> provideTwoNullIntsForEvaluateInContext() {
        return Stream.of(
                Arguments.of(-1000, 0),
                Arguments.of(0, -1000),
                Arguments.of(3, 0),
                Arguments.of(0, 3),
                Arguments.of(0, 0),
                Arguments.of(330, 11)
        );
    }
}
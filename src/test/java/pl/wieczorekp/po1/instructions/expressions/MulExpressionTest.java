package pl.wieczorekp.po1.instructions.expressions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.wieczorekp.po1.instructions.statements.CodeBlock;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MulExpressionTest {
    @ParameterizedTest
    @MethodSource("provideTwoIntsForEvaluateInContext")
    void givenTwoIntegersShouldEvaluateInEmptyContext(Integer a, Integer b) {
        // given
        CodeBlock context = new CodeBlock(null);
        LiteralExpression aLiteral = LiteralExpression.of(a);
        LiteralExpression bLiteral = LiteralExpression.of(b);
        MulExpression exp = MulExpression.of(aLiteral, bLiteral);

        // when
        Integer result = exp.evaluateInContext(context);

        // then
        assertEquals(a * b, result);
    }

    private static Stream<Arguments> provideTwoIntsForEvaluateInContext() {
        return Stream.of(
                Arguments.of(-1000, 321),
                Arguments.of(1000, -321),
                Arguments.of(-1000, -321),
                Arguments.of(-1000, 0),
                Arguments.of(0, -1000),
                Arguments.of(3, 1),
                Arguments.of(1, 3),
                Arguments.of(7, 441),
                Arguments.of(441, 7),
                Arguments.of(8, 2),
                Arguments.of(3, 0),
                Arguments.of(0, 3),
                Arguments.of(0, 0),
                Arguments.of(330, 11)
        );
    }
}
package pl.wieczorekp.macchiato.instructions.statements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pl.wieczorekp.macchiato.builders.BlockBuilder;
import pl.wieczorekp.macchiato.instructions.expressions.LiteralExpression;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IfStatementTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output));
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, Integer.MAX_VALUE, 19, 20, 21})
    void shouldCheckTheConditionAndEnterTheElseBlock(final int k) {
        // given
        int expected = 20 >= k ? 10 : -10;
        CodeBlock ctx = new CodeBlock(null);
        IfStatement cond = new IfStatement(ctx,
                LiteralExpression.of(20),
                LiteralExpression.of(k),
                IfStatement.Condition.GEQ,
                new BlockBuilder()
                        .print(10)
                        .build(),
                new BlockBuilder()
                        .print(-10)
                        .build());
        ctx.appendStatement(cond);

        // when
        ctx.execute();

        // then
        assertEquals(expected + "\n", output.toString());
    }
}
package pl.wieczorekp.macchiato.instructions.statements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.wieczorekp.macchiato.builders.BlockBuilder;
import pl.wieczorekp.macchiato.instructions.expressions.LiteralExpression;
import pl.wieczorekp.macchiato.instructions.expressions.VariableExpression;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ForStatementTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output));
    }

    @Test
    void shouldLoopCorrectly() {
        // given
        final int K = 30;
        CodeBlock ctx = new CodeBlock(null);
        ctx.appendStatement(new VariableDeclarationStatement(ctx, "k", LiteralExpression.of(K)));
        ForStatement loop = new ForStatement(ctx, "i", VariableExpression.named("k"), new BlockBuilder()
                .print(VariableExpression.named("i"))
                .build());
        ctx.appendStatement(loop);

        StringBuilder solution = new StringBuilder();
        for (int i = 0; i < K; i++) {
            solution.append(i).append('\n');
        }

        // when
        ctx.execute();

        // then
        assertEquals(solution.toString(), output.toString());
    }
}
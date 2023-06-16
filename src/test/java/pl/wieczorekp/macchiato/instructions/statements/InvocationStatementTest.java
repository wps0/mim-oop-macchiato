package pl.wieczorekp.macchiato.instructions.statements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.wieczorekp.macchiato.builders.BlockBuilder;
import pl.wieczorekp.macchiato.instructions.expressions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvocationStatementTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output));
    }

    @Test
    void shouldInvokeAFunction() {
        // given
        CodeBlock ctx = new CodeBlock(null);
        FunctionStatement fun = new FunctionStatement(ctx, "fun", List.of("arg1", "arg2"), new BlockBuilder()
                .declareVariable("mod", LiteralExpression.of(1337))
                .assign("arg1", ModExpression.of(MulExpression.of(VariableExpression.named("arg1"), VariableExpression.named("arg2")),
                        VariableExpression.named("mod")))
                .print(VariableExpression.named("arg1"))
                .build());
        ctx.appendStatement(new VariableDeclarationStatement(ctx, "arg1", LiteralExpression.of(-10)));
        ctx.appendStatement(fun);
        ctx.appendStatement(new InvocationStatement(ctx,
                "fun",
                List.of(LiteralExpression.of(80713), LiteralExpression.of(6053))));

        // when
        ctx.execute();

        // then
        assertEquals("1282\n", output.toString());
    }
}
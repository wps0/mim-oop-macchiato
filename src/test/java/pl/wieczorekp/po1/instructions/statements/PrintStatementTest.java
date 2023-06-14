package pl.wieczorekp.po1.instructions.statements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.wieczorekp.po1.instructions.expressions.LiteralExpression;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static java.lang.Integer.MIN_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.wieczorekp.po1.Utils.VARIABLE_A;
import static pl.wieczorekp.po1.Utils.VARIABLE_B;

class PrintStatementTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output));
    }

    @Test
    void givenAnEmptyContextShouldPrintALiteralCorrectly() {
        // given
        CodeBlock context = new CodeBlock(null);
        PrintStatement ps = new PrintStatement(context, LiteralExpression.of(MIN_VALUE));

        // when
        ps.executeOne();

        // then
        assertEquals(0, context.getFunctions().size());
        assertEquals(0, context.getVariables().size());
        assertEquals(output.toString(), MIN_VALUE + "\n");
    }
}
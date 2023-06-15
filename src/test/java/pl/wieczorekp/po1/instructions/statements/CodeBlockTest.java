package pl.wieczorekp.po1.instructions.statements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.wieczorekp.macchiato.Execution;
import pl.wieczorekp.macchiato.instructions.statements.CodeBlock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static pl.wieczorekp.macchiato.Main.buildSampleProgramContainingMultipleBlocks;

class CodeBlockTest {
    private static final String VARIABLE_A = "variable_a";
    private static final String VARIABLE_B = "variable_b";
    private static final String VARIABLE_C = "variable_c";
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output));
    }

    @Test
    void givenSampleBlockProgramShouldManageVariablesVisibilityCorrectly() {
        // given
        Execution prog = new Execution(buildSampleProgramContainingMultipleBlocks());
        final String expected = "1\n3\n100\n2\n1\nProgram finished\n";

        // when
        prog.run();

        // then
        assertEquals(expected, output.toString());
    }

    @Test
    void lookupVariable_givenNonNestedContextShouldResolveTheVariableOrReturnAnEmptyOptional() {
        // given
        CodeBlock context = new CodeBlock(null);
        context.declareVariable(VARIABLE_C, -213);

        // when
        Optional<Integer> var_a = context.lookupVariable(VARIABLE_A);
        Optional<Integer> var_c = context.lookupVariable(VARIABLE_C);

        // then
        assertTrue(var_a.isEmpty());
        assertTrue(var_c.isPresent());
        assertEquals(-213, var_c.get());
    }

    @Test
    void lookupVariable_givenNestedContextShouldResolveTheVariableOrReturnAnEmptyOptional() {
        // given
        CodeBlock context = new CodeBlock(null);
        CodeBlock nested_context = new CodeBlock(context);
        context.declareVariable(VARIABLE_A, -213);
        nested_context.declareVariable(VARIABLE_B, 95840);

        // when
        Optional<Integer> ctx_var_a = context.lookupVariable(VARIABLE_A);
        Optional<Integer> ctx_var_b = context.lookupVariable(VARIABLE_B);
        Optional<Integer> ctx_var_c = context.lookupVariable(VARIABLE_C);
        Optional<Integer> nctx_var_a = nested_context.lookupVariable(VARIABLE_A);
        Optional<Integer> nctx_var_b = nested_context.lookupVariable(VARIABLE_B);
        Optional<Integer> nctx_var_c = nested_context.lookupVariable(VARIABLE_C);

        // then
        assertTrue(ctx_var_b.isEmpty());
        assertTrue(ctx_var_c.isEmpty());
        assertTrue(nctx_var_c.isEmpty());

        assertEquals(-213, ctx_var_a.get());
        assertEquals(-213, nctx_var_a.get());
        assertEquals(95840, nctx_var_b.get());
    }
}
package pl.wieczorekp.po1.instructions.statements;

import org.junit.jupiter.api.Test;
import pl.wieczorekp.macchiato.instructions.expressions.LiteralExpression;
import pl.wieczorekp.macchiato.instructions.statements.AssignmentStatement;
import pl.wieczorekp.macchiato.instructions.statements.CodeBlock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.wieczorekp.po1.Utils.VARIABLE_A;
import static pl.wieczorekp.po1.Utils.VARIABLE_B;

class AssignmentStatementTest {

    @Test
    void givenAContextShouldAssignTheVariableCorrectly() {
        // given
        CodeBlock context = new CodeBlock(null);
        context.declareVariable(VARIABLE_A, 123);
        context.declareVariable(VARIABLE_B, -123);
        AssignmentStatement st = new AssignmentStatement(context, VARIABLE_B, LiteralExpression.of(321));

        // when
        st.executeOne();

        // then
        assertEquals(0, context.getFunctions().size());
        assertEquals(2, context.getVariables().size());
        assertEquals(123, context.lookupVariable(VARIABLE_A).get());
        assertEquals(321, context.lookupVariable(VARIABLE_B).get());
    }
}
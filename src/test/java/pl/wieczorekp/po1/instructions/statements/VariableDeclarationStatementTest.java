package pl.wieczorekp.po1.instructions.statements;

import org.junit.jupiter.api.Test;
import pl.wieczorekp.po1.instructions.expressions.LiteralExpression;

import static org.junit.jupiter.api.Assertions.*;
import static pl.wieczorekp.po1.Utils.VARIABLE_A;

class VariableDeclarationStatementTest {
    @Test
    void givenAContextShouldDeclareVariableCorrectly() {
        // given
        CodeBlock context = new CodeBlock(null);
        AssignmentStatement st1 = new VariableDeclarationStatement(context, VARIABLE_A, LiteralExpression.of(123));

        // when
        st1.executeOne();

        // then
        assertEquals(0, context.getFunctions().size());
        assertEquals(1, context.getVariables().size());
        assertEquals(123, context.lookupVariable(VARIABLE_A).get());
    }

}
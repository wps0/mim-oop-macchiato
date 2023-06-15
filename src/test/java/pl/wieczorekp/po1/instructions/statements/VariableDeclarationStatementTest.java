package pl.wieczorekp.po1.instructions.statements;

import org.junit.jupiter.api.Test;
import pl.wieczorekp.macchiato.instructions.expressions.LiteralExpression;
import pl.wieczorekp.macchiato.instructions.statements.AssignmentStatement;
import pl.wieczorekp.macchiato.instructions.statements.CodeBlock;
import pl.wieczorekp.macchiato.instructions.statements.VariableDeclarationStatement;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
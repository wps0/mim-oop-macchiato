package pl.wieczorekp.macchiato.builders;

import pl.wieczorekp.macchiato.instructions.expressions.Expression;
import pl.wieczorekp.macchiato.instructions.expressions.LiteralExpression;
import pl.wieczorekp.macchiato.instructions.statements.*;

import java.util.List;

public class BlockBuilder {
    private final CodeBlock context;

    public BlockBuilder() {
        this.context = new CodeBlock(null);
    }

    public BlockBuilder declareVariable(String name, Expression value) {
        context.appendStatement(new VariableDeclarationStatement(context, name, value));
        return this;
    }

    public BlockBuilder assign(String symbol, Expression value) {
        context.appendStatement(new AssignmentStatement(context, symbol, value));
        return this;
    }

    public BlockBuilder declareProcedure(String name, List<String> params, CodeBlock body) {
        // The context of the body is dynamically changed, so setting it here is unnecessary.
        context.appendStatement(new FunctionStatement(context, name, params, body));
        return this;
    }

    public BlockBuilder invoke(String name, List<Expression> params) {
        context.appendStatement(new InvocationStatement(context, name, params));
        return this;
    }

    public BlockBuilder loop(String i, Expression repetitions, CodeBlock body) {
        body.setContext(context);
        context.appendStatement(new ForStatement(context, i, repetitions, body));
        return this;
    }

    public BlockBuilder ifElse(IfStatement ifStatement) {
        ifStatement.setContext(context);
        context.appendStatement(ifStatement);
        return this;
    }

    public BlockBuilder block(CodeBlock block) {
        block.setContext(context);
        context.appendStatement(block);
        return this;
    }

    public BlockBuilder statement(Statement s) {
        context.appendStatement(s);
        return this;
    }

    public BlockBuilder print(Expression message) {
        context.appendStatement(new PrintStatement(context, message));
        return this;
    }

    public BlockBuilder print(Integer message) {
        return print(new LiteralExpression(message));
    }

    public CodeBlock build() {
        return context;
    }
}

package pl.wieczorekp.po1.builders;

import pl.wieczorekp.po1.instructions.expressions.Expression;
import pl.wieczorekp.po1.instructions.statements.*;

import java.util.List;

public class BlockBuilder {
    private final CodeBlock context;

    public BlockBuilder() {
        this.context = new CodeBlock(null);
    }

    public BlockBuilder declareVariable(String name, Expression value) {
        context.addStatement(new VariableDeclarationStatement(context, name, value));
        return this;
    }

    public BlockBuilder assign(String symbol, Expression value) {
        context.addStatement(new AssignmentStatement(context, symbol, value));
        return this;
    }

    public BlockBuilder declareProcedure(String name, List<String> params, CodeBlock body) {
        throw new UnsupportedOperationException("to be implemented");
    }

    public BlockBuilder invoke(String name, List<Expression> params) {
        throw new UnsupportedOperationException("to be implemented");
    }

    public BlockBuilder loop(String i, Expression repetitions, CodeBlock body) {
        context.addStatement(new ForStatement(context, i, repetitions, body));
        return this;
    }

    public BlockBuilder condition(IfStatement ifStatement) {
        ifStatement.setContext(context);
        context.addStatement(ifStatement);
        return this;
    }

    public BlockBuilder block(CodeBlock block) {
        block.setContext(context);
        context.addStatement(block);
        return this;
    }

    public BlockBuilder print(Expression message) {
        context.addStatement(new PrintStatement(context, message));
        return this;
    }

    public CodeBlock build() {
        return context;
    }
}

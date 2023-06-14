package pl.wieczorekp.po1.instructions.statements;

import pl.wieczorekp.po1.instructions.UndefinedIdentifierException;
import pl.wieczorekp.po1.instructions.expressions.Expression;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class InvocationStatement extends BlockStatement {
    private final String functionIdentifier;
    private final List<Expression> args;
    private FunctionStatement function;
    private Branch branch;

    public InvocationStatement(CodeBlock context, String functionIdentifier, List<Expression> args) {
        super(context);
        this.functionIdentifier = functionIdentifier;
        this.args = args;
        this.branch = Branch.BINDING;
    }

    private void bind(FunctionStatement toFunc) {
        if (function == null) {
            function = toFunc;
        }
        function.getBody().resetVariables();
        function.getBody().setInstructionPointer(0);
        // Change the context of the body to the current invocation's one..
        function.getBody().setContext(context);

        Iterator<Expression> valuesIterator = args.iterator();
        for (String argumentName : function.getArgumentNames()) {
            function.getBody().declareVariable(argumentName, valuesIterator.next().evaluateInContext(context));
        }
    }

    @Override
    public Optional<Statement> getCurrentStatement(boolean shiftIP) {
        if (branch == Branch.BINDING || hasEnded()) {
            return Optional.of(this);
        }
        return function.getCurrentStatement(shiftIP);
    }

    @Override
    public boolean hasEnded() {
        return branch == Branch.FINISHED;
    }

    @Override
    public void executeOne() {
        if (hasEnded()) {
            branch = Branch.BINDING;
        }

        if (branch == Branch.BINDING) {
            Optional<FunctionStatement> f = context.lookupFunction(functionIdentifier);
            if (f.isEmpty()) {
                throw new UndefinedIdentifierException("No function named '" + functionIdentifier + "'");
            }

            bind(f.get());
            branch = Branch.EXECUTION;

            return;
        }

        function.getBody().executeOne();
        if (function.getBody().hasEnded()) {
            branch = Branch.FINISHED;
        }
    }

    @Override
    public String toString() {
        String prefix = "  ".repeat(context.getNestedness());

        if (function == null) {
            return "%s%s(%s)".formatted(prefix,
                    functionIdentifier,
                    args.stream()
                            .map(Object::toString)
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("void"));
        }

        StringBuilder asString = new StringBuilder();
        asString.append(prefix).append(functionIdentifier).append("(");
        Iterator<Expression> valuesIterator = args.iterator();
        for (String argumentName : function.getArgumentNames()) {
            asString.append(argumentName).append("=").append(valuesIterator.next());

            if (valuesIterator.hasNext()) {
                asString.append(", ");
            }
        }
        asString.append(")\n").append(function.getBody());
        return asString.toString();
    }

    private enum Branch {
        BINDING, EXECUTION, FINISHED
    }
}

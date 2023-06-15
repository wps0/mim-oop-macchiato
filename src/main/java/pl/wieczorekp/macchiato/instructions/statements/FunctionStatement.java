package pl.wieczorekp.macchiato.instructions.statements;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class FunctionStatement extends BlockStatement {
    private final String identifier;
    private final List<String> arguments;
    private final CodeBlock body;
    private boolean hasEnded;

    public FunctionStatement(CodeBlock context, String identifier, List<String> arguments, CodeBlock body) {
        super(context);
        this.identifier = identifier;
        this.arguments = arguments;
        this.body = body;
        this.hasEnded = false;
    }

    public List<String> getArgumentNames() {
        return arguments;
    }

    public String getHeader() {
        StringBuilder header = new StringBuilder();

        header.append("proc ").append(identifier).append("(");
        for (Iterator<String> iterator = arguments.iterator(); iterator.hasNext();) {
            String argumentName = iterator.next();
            header.append(argumentName);
            if (iterator.hasNext()) {
                header.append(", ");
            }
        }
        header.append(')');

        return header.toString();
    }

    @Override
    public Optional<Statement> getCurrentStatement(boolean shiftIP) {
        return Optional.of(this);
    }

    @Override
    public boolean hasEnded() {
        return hasEnded;
    }

    @Override
    public void executeOne() {
        if (hasEnded) {
            hasEnded = false;
        }
        context.declareFunction(identifier, this);
        hasEnded = true;
    }

    @Override
    public String toString() {
        String prefix = "  ".repeat(context.getNestedness());
        StringBuilder asString = new StringBuilder();
        asString.append(prefix).append(getHeader());
        asString.append(":\n").append(body);

        return asString.toString();
    }

    public CodeBlock getBody() {
        return body;
    }
}

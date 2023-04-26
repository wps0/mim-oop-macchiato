package pl.wieczorekp.po1.instructions;

import java.util.*;

public class CodeBlock extends Statement {
    private Map<String, Integer> variables;
    private List<Statement> statements;
    private int nextStatement;
    
    public CodeBlock(CodeBlock containingBlock) {
        super(containingBlock);
        this.variables = new HashMap<>();
        this.statements = new ArrayList<>();
        this.nextStatement = 0;
    }

    public Optional<Integer> lookupVariable(String name) {
        return Optional.ofNullable(variables.get(name));
    }

    public void declareVariable(String name, Integer value) {
        if (variables.containsKey(name)) {
            throw new IllegalNameException("redeclaration of variable " + name);
        }

        variables.put(name, value);
    }

    public void addStatement(Statement newStatement) {
        if (hasEnded()) {
            throw new ExecutionEndedException("the execution of the block has ended");
        }

        statements.add(newStatement);
    }

    @Override
    public void executeOne() {
        if (hasEnded()) {
            throw new ExecutionEndedException("no more statements to execute");
        }

        statements.get(nextStatement).executeOne();
        if (statements.get(nextStatement).hasEnded()) {
            nextStatement++;
        }
    }

    @Override
    public boolean hasEnded() {
        return nextStatement >= statements.size();
    }
}

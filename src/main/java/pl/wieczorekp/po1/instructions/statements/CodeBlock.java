package pl.wieczorekp.po1.instructions.statements;

import pl.wieczorekp.po1.instructions.ExecutionEndedException;
import pl.wieczorekp.po1.instructions.IllegalNameException;

import java.util.*;

public class CodeBlock extends Statement {
    private Map<String, Integer> variables;
    private List<Statement> statements;
    private int instructionPointer;
    
    public CodeBlock(CodeBlock containingBlock) {
        super(containingBlock);
        this.variables = new HashMap<>();
        this.statements = new ArrayList<>();
        this.instructionPointer = 0;
    }

    public Optional<Integer> lookupVariable(String name) {
        if (variables.containsKey(name)) {
            return Optional.of(variables.get(name));
        }
        if (getContext() == null) {
            return Optional.empty();
        }
        return getContext().lookupVariable(name);
    }

    private void declareVariable(String name, Integer value, boolean force) {
        if (!force && variables.containsKey(name)) {
            throw new IllegalNameException("redeclaration of variable " + name);
        }

        variables.put(name, value);
    }

    public void declareVariable(String name, Integer value) {
        declareVariable(name, value, false);
    }

    public void addStatement(Statement newStatement) {
        if (hasEnded()) {
            throw new ExecutionEndedException("the execution of the block has ended");
        }

        statements.add(newStatement);
    }

    public void setInstructionPointer(int instructionPointer) {
        if (instructionPointer < 0 || instructionPointer > statements.size()) {
            throw new IndexOutOfBoundsException("IP: " + instructionPointer + " out of bounds");
        }
        this.instructionPointer = instructionPointer;
    }

    @Override
    public void executeOne() {
        if (hasEnded()) {
            throw new ExecutionEndedException("no more statements to execute");
        }

        statements.get(instructionPointer).executeOne();
        if (statements.get(instructionPointer).hasEnded()) {
            instructionPointer++;
        }
    }

    @Override
    public boolean hasEnded() {
        return instructionPointer >= statements.size();
    }

    @Override
    public String toString() {
        // todo: change it
        return "CodeBlock{" +
                "variables=" + variables +
                ", nextStatement=" + instructionPointer +
                '}';
    }
}

package pl.wieczorekp.po1.instructions.statements;

import pl.wieczorekp.po1.instructions.ExecutionEndedException;
import pl.wieczorekp.po1.instructions.IllegalNameException;

import java.util.*;

public class CodeBlock extends BlockStatement {
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

    private void assignVariable(String name, Integer value, boolean force) {
        if (!force && variables.containsKey(name)) {
            throw new IllegalNameException("redeclaration of variable " + name);
        }

        variables.put(name, value);
    }

    public void declareVariable(String name, Integer value) {
        assignVariable(name, value, false);
    }

    public void assignVariable(String name, Integer value) {
        assignVariable(name, value, true);
    }

    public void addStatement(Statement newStatement) {
        statements.add(newStatement);
    }

    public void setInstructionPointer(int instructionPointer) {
        if (instructionPointer < 0 || instructionPointer > statements.size()) {
            throw new IndexOutOfBoundsException("IP: " + instructionPointer + " out of bounds");
        }
        this.instructionPointer = instructionPointer;
    }

    public int getNestedness() {
        if (context != null) {
            return context.getNestedness() + 1;
        }
        return 0;
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
        int nestedness = getNestedness();
        // 2 spaces per level
        String prefix = "  ".repeat(nestedness);

        StringBuilder partOfCode = new StringBuilder();
        partOfCode.append(prefix).append("begin block\n");

        for (Statement s : statements) {
            partOfCode.append(prefix).append("  ");
            partOfCode.append(s.toString()).append('\n');
        }

        partOfCode.append(prefix).append("end block\n");
        return partOfCode.toString();
    }

    @Override
    public Optional<Statement> getCurrentStatement() {
        if (hasEnded()) {
            return Optional.empty();

        }
        Statement last = statements.get(instructionPointer);
        if (last instanceof BlockStatement) {
            return ((BlockStatement) last).getCurrentStatement();
        }
        return Optional.ofNullable(last);
    }
}

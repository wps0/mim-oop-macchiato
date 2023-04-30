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

    public void declareVariable(String name, Integer value) {
        assignVariable(name, value, false);
    }

    public void assignVariable(String name, Integer value) {
        assignVariable(name, value, true);
    }

    public void addStatement(Statement newStatement) {
        statements.add(newStatement);
    }

    public Map<String, Integer> getVariables() {
        return variables;
    }

    public void resetVariables() {
        variables.clear();
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

    public int getIp(boolean shift) {
        if (shift) {
            return instructionPointer == 0 ? statements.size() - 1 : instructionPointer - 1;
        }
        return instructionPointer;
    }

    private void assignVariable(String name, Integer value, boolean force) {
        if (!force && variables.containsKey(name)) {
            throw new IllegalNameException("redeclaration of variable " + name);
        }

        variables.put(name, value);
    }

    @Override
    public void executeOne() {
        if (hasEnded()) {
            throw new ExecutionEndedException("no more statements to execute");
        }

        Statement next = statements.get(instructionPointer);
        next.executeOne();
        if (next instanceof BlockStatement) {
            // if BlockStatement hasn't finished execution, don't increase the instruction pointer
            instructionPointer += Boolean.compare(((BlockStatement) next).hasEnded(), false);
        } else {
            instructionPointer++;
        }
    }

    @Override
    public boolean hasEnded() {
        return instructionPointer >= statements.size();
    }

    @Override
    public Optional<Statement> getCurrentStatement(boolean shiftIP) {
        if (hasEnded()) {
            return Optional.empty();
        }

        Statement last = statements.get(getIp(shiftIP));
        if (last instanceof BlockStatement) {
            return ((BlockStatement) last).getCurrentStatement(shiftIP);
        }
        return Optional.of(last);
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
}

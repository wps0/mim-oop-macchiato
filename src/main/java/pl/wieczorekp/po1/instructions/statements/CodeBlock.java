package pl.wieczorekp.po1.instructions.statements;

import pl.wieczorekp.po1.instructions.ExecutionEndedException;
import pl.wieczorekp.po1.instructions.IllegalNameException;
import pl.wieczorekp.po1.instructions.UndefinedIdentifierException;

import java.util.*;
import java.util.function.BiPredicate;

public class CodeBlock extends BlockStatement {
    private static final BiPredicate<CodeBlock, String> VARIABLE_TEST;
    private static final BiPredicate<CodeBlock, String> FUNCTION_TEST;
    private final Map<String, Integer> variables;
    private final Map<String, FunctionStatement> functions;
    private final LinkedList<Statement> statements;
    private int instructionPointer;

    static {
        VARIABLE_TEST = (block, id) -> block.getVariables().containsKey(id);
        FUNCTION_TEST = (block, id) -> block.getFunctions().containsKey(id);
    }

    public CodeBlock(CodeBlock containingBlock) {
        super(containingBlock);
        this.variables = new TreeMap<>();
        this.functions = new TreeMap<>();
        this.statements = new LinkedList<>();
        this.instructionPointer = 0;
    }

    public Optional<Integer> lookupVariable(String name) {
        return getClosestDeclaration(VARIABLE_TEST, name).map(codeBlock -> codeBlock.getVariables().get(name));
    }

    public void declareVariable(String name, Integer value) {
        assignVariable(name, value, false);
    }

    public void assignVariable(String name, Integer value) {
        Optional<CodeBlock> closestDeclaration = getClosestDeclaration(VARIABLE_TEST, name);
        closestDeclaration.orElseThrow(UndefinedIdentifierException::new).assignVariable(name, value, true);
    }

    public Optional<FunctionStatement> lookupFunction(String name) {
        return getClosestDeclaration(FUNCTION_TEST, name).map(codeBlock -> codeBlock.getFunctions().get(name));
    }

    public void declareFunction(String identifier, FunctionStatement function) {
        if (functions.containsKey(identifier)) {
            throw new IllegalNameException("Function " + identifier + " is already defined in this block.");
        }

        functions.put(identifier, function);
    }

    public void prependStatement(Statement newStatement) {
        statements.addFirst(newStatement);
    }

    public void appendStatement(Statement newStatement) {
        statements.add(newStatement);
    }

    public Map<String, Integer> getVariables() {
        return variables;
    }

    public Map<String, FunctionStatement> getFunctions() {
        return functions;
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

    private Optional<CodeBlock> getClosestDeclaration(BiPredicate<CodeBlock, String> doesBlockContain, String identifier) {
        CodeBlock curBlock = this;

        while (curBlock != null) {
            if (doesBlockContain.test(curBlock, identifier)) {//curBlock.getVariables().containsKey
                return Optional.of(curBlock);
            }

            curBlock = curBlock.getContext();
        }

        return Optional.empty();
    }

    @Override
    public void executeOne() {
        if (hasEnded()) {
            throw new ExecutionEndedException("no more statements to execute");
        }

        Statement next = statements.get(instructionPointer);
        next.executeOne();
        if (next instanceof BlockStatement) {
            // if BlockStatement hasn't finished yet, don't increase the instruction pointer
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

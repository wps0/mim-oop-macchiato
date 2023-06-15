package pl.wieczorekp.macchiato;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Command(String shortCmd, String cmd, List<String> arguments, String description, Predicate<List<String>> verificationStrategy,
                      Consumer<List<String>> executionStrategy) implements Comparable<Command> {

    public Command(String shortCmd, String cmd, String description, Predicate<List<String>> verificationStrategy, Consumer<List<String>> executionStrategy) {
        this(shortCmd, cmd, List.of(), description, verificationStrategy, executionStrategy);
    }

    @Override
    public String toString() {
        Pattern toStringPattern = Pattern.compile("(.*?)(" + shortCmd + ")(.*)");
        Matcher cmdMatcher = toStringPattern.matcher(cmd);
        cmdMatcher.find();

        return cmdMatcher.group(1) +
                '(' + cmdMatcher.group(2) + ')' +
                cmdMatcher.group(3) +
                " " + arguments.stream()
                .map(s -> "[" + s + "]")
                .reduce((s1, s2) -> s1 + " " + s2).orElse("");
    }

    @Override
    public int compareTo(Command command) {
        if (this.equals(command))
            return 0;
        if (!shortCmd.equals(command.shortCmd()))
            return shortCmd.compareTo(command.shortCmd());
        return Integer.compare(arguments.size(), command.arguments.size());
    }
}

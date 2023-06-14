package pl.wieczorekp.po1;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Command(String shortCmd, String cmd, String description, Predicate<List<String>> verificationStrategy,
                      Consumer<List<String>> executionStrategy) {
    @Override
    public String toString() {
        Pattern toStringPattern = Pattern.compile("(.*?)(" + shortCmd + ")(.*)");
        Matcher cmdMatcher = toStringPattern.matcher(cmd);
        cmdMatcher.find();

        StringBuilder rep = new StringBuilder();
        rep.append(cmdMatcher.group(1));
        rep.append('(').append(cmdMatcher.group(2)).append(')');
        rep.append(cmdMatcher.group(3));
        return rep.toString();
    }
}

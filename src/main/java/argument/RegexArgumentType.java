package argument;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexArgumentType implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("/match/");
    @Override
    public String parse(StringReader stringReader) throws CommandSyntaxException {
        String regex = stringReader.readString();
        Pattern pattern;
        if(regex.isEmpty()) {

            SimpleCommandExceptionType err = new SimpleCommandExceptionType(new LiteralMessage("Expected valid regex"));
            throw err.createWithContext(stringReader);
        }
        try {
            pattern = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            throw SyntaxException.READER_INVALID_REGEX.createWithContext(stringReader, e.getMessage());
        }
        return regex;
    }
}

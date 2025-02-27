package argument;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class SyntaxException  {
    public static final SimpleCommandExceptionType READER_EXPECTED_REGEX = new SimpleCommandExceptionType(new LiteralMessage("Expected valid regex"));
    public static final DynamicCommandExceptionType READER_INVALID_REGEX = new DynamicCommandExceptionType(obj -> new LiteralMessage("Invalid regex" + obj));


}

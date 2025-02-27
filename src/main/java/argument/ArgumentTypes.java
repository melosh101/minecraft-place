package argument;

import com.mojang.brigadier.arguments.ArgumentType;

public final class ArgumentTypes {
    public final static ArgumentType<String> regex = new RegexArgumentType();
}

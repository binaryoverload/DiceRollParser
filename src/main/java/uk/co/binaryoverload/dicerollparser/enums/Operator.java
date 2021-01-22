package uk.co.binaryoverload.dicerollparser.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public enum Operator {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    public static final Pattern OPERATOR_PATTERN = Pattern.compile("[+\\-*/]");

    private static final Map<String, Operator> literalCache = new HashMap<>();
    private final String literal;

    static {
        for (Operator value : values()) {
            literalCache.put(value.getLiteral(), value);
        }
    }

    Operator(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    public static Map<String, Operator> getLiteralCache() {
        return Map.copyOf(literalCache);
    }
}

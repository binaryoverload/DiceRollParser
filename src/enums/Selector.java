package enums;

import java.util.HashMap;
import java.util.Map;

public enum Selector {
    GREATER_THAN(">"),
    LESS_THAN("<"),
    LOW("l"),
    HIGH("h");


    private static final Map<String, Selector> literalCache = new HashMap<>();

    private final String literal;

    static {
        for (Selector value : values()) {
            literalCache.put(value.getLiteral(), value);
        }
    }

    Selector(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    public static Map<String, Selector> getLiteralCache() {
        return Map.copyOf(literalCache);
    }

}

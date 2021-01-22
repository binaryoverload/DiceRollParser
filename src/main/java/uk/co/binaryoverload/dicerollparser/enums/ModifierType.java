package uk.co.binaryoverload.dicerollparser.enums;

import java.util.HashMap;
import java.util.Map;

public enum ModifierType {
    REROLL("rr"),
    REROLL_ONE("ro"),
    REROLL_ADD("ra"),
    MINIMUM("mi"),
    MAXIMUM("ma"),
    EXPLODE("e"),
    KEEP("k"),
    DROP("d")
    ;


    private static final Map<String, ModifierType> literalCache = new HashMap<>();

    private final String literal;

    static {
        for (ModifierType value : values()) {
            literalCache.put(value.getLiteral(), value);
        }
    }

    ModifierType(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    public static Map<String, ModifierType> getLiteralCache() {
        return Map.copyOf(literalCache);
    }

}

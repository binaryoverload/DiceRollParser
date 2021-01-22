package uk.co.binaryoverload.dicerollparser.objects;

import uk.co.binaryoverload.dicerollparser.enums.ModifierType;
import uk.co.binaryoverload.dicerollparser.enums.Selector;

public class Modifier {

    private ModifierType type;
    private Integer value;
    private Selector selector;
    private Integer selectorValue;

    public Modifier(ModifierType type, Integer value, Selector selector, Integer selectorValue) {
        this.type = type;
        this.value = value;
        this.selector = selector;
        this.selectorValue = selectorValue;
    }

    public ModifierType getType() {
        return type;
    }

    public Integer getValue() {
        return value;
    }

    public Selector getSelector() {
        return selector;
    }

    public Integer getSelectorValue() {
        return selectorValue;
    }

    @Override
    public String toString() {
        return "objects.Modifier{" +
                "type=" + type +
                ", value=" + value +
                ", selector=" + selector +
                ", selectorValue=" + selectorValue +
                '}';
    }

}

package uk.co.binaryoverload.dicerollparser.objects;


import uk.co.binaryoverload.dicerollparser.enums.Operator;

import java.util.List;

public class DiceRoll {

    private List<Die> dice;
    private String label;
    private List<Modifier> modifiers;
    private List<AddedValue> addedValue;
    private Operator operator;

    public DiceRoll(List<Die> dice, String label, List<Modifier> modifiers, List<AddedValue> addedValue, Operator operator) {
        this.dice = dice;
        this.label = label;
        this.modifiers = modifiers;
        this.addedValue = addedValue;
        this.operator = operator;
    }

    public List<Die> getDice() {
        return dice;
    }

    public String getLabel() {
        return label;
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public List<AddedValue> getAddedValue() {
        return addedValue;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "objects.DiceRoll{" +
                "dice=" + dice +
                ", label='" + label + '\'' +
                ", modifiers=" + modifiers +
                ", addedValue=" + addedValue +
                ", operator=" + operator +
                '}';
    }

}

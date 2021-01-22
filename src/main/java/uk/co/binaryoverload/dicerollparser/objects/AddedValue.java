package uk.co.binaryoverload.dicerollparser.objects;

import uk.co.binaryoverload.dicerollparser.enums.Operator;

public class AddedValue {

    private Operator operator;
    private int value;

    public AddedValue(Operator operator, int value) {
        this.operator = operator;
        this.value = value;
    }

    public Operator getOperator() {
        return operator;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "objects.AddedValue{" +
                "operator=" + operator +
                ", value=" + value +
                '}';
    }

}

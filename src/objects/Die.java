package objects;

public class Die {

    private final int diceCount;
    private final int diceValue;

    public Die(int diceCount, int diceValue) {
        this.diceCount = diceCount;
        this.diceValue = diceValue;
    }

    public int getDiceCount() {
        return diceCount;
    }

    public int getDiceValue() {
        return diceValue;
    }

    @Override
    public String toString() {
        return "objects.Die{" +
                "diceCount=" + diceCount +
                ", diceValue=" + diceValue +
                '}';
    }

}

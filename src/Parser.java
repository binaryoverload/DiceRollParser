
import enums.ModifierType;
import enums.Operator;
import enums.Selector;
import objects.AddedValue;
import objects.DiceRoll;
import objects.Die;
import objects.Modifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {

    private static final Pattern ROLL_PATTERN = Pattern.compile("(?:\\(?)((?:\\d+d\\d+,?)+)(?:\\)?)(?:((?:k|p|mi|ma|rr|ro|ra|e)[><lh]?\\d+)+)?(?:\\[(.+)\\])?((?:[+\\-*\\/]\\d+)+(?!d))?([+\\-*\\/])?");

    private static final Pattern ADDED_VALUE_PATTERN = Pattern.compile("([+\\-*\\/])(\\d+)");
    private static final Pattern DICE_PATTERN = Pattern.compile("^(\\d+)d(\\d+)$");
    private static final Pattern MODIFIER_PATTERN = Pattern.compile("(k|p|mi|ma|rr|ro|ra|e)(\\d+)?(?:([><lh])?([0-9]+))?");

    public static void main(String[] args) {

        List<String> inputRolls = List.of(
                "1d20",
                "1d20ra6",
                "2d20kh1+4",
                "2d20kl1-2",
                "4d6ro<3",
                "2d6e6",
                "4d6mi2[fire]+2d20kh1+4+7+11+3d10ra10",
                "(1d6,1d8,1d10)kh2"
        );

        for (String inputRoll : inputRolls) {

            System.out.println("\n ---- New Input ---- \n");

            Matcher rollMatcher = ROLL_PATTERN.matcher(inputRoll);

            List<DiceRoll> rolls = new ArrayList<>();
            while (rollMatcher.find()) {
                String[] diceCombos = rollMatcher.group(1).split("\\s*,\\s*");
                List<Die> dice = parseDie(diceCombos);


                Stream<MatchResult> modifiersInput = Stream.of();
                if (rollMatcher.group(2) != null) {
                    modifiersInput = MODIFIER_PATTERN.matcher(rollMatcher.group(2)).results();
                }
                List<Modifier> modifiers = parseModifiers(modifiersInput);

                String label = rollMatcher.group(3);

                Stream<MatchResult> addedValueInput = Stream.of();
                if (rollMatcher.group(4) != null) {
                    addedValueInput = ADDED_VALUE_PATTERN.matcher(rollMatcher.group(4)).results();
                }
                List<AddedValue> addedValues = parseAddedValues(addedValueInput);

                Operator operator = null;
                if (rollMatcher.group(5) != null) {
                    operator = Operator.getLiteralCache().get(rollMatcher.group(5));
                }

                DiceRoll roll = new DiceRoll(dice, label, modifiers, addedValues, operator);
                rolls.add(roll);
                System.out.println(roll);
            }

        }


    }

    private static List<AddedValue> parseAddedValues(Stream<MatchResult> addedValueInput) {
        return addedValueInput.map(result -> {
            Operator operator = Operator.getLiteralCache().get(result.group(1));
            int value = Integer.parseInt(result.group(2));
            return new AddedValue(operator, value);
        }).collect(Collectors.toList());
    }

    private static List<Die> parseDie(String[] diceCombos) {
        return Arrays.stream(diceCombos).map(s -> {
            Matcher matcher = DICE_PATTERN.matcher(s);
            if (matcher.find()) {
                return new Die(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            }
            System.out.println("Invalid die: " + s);
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static List<Modifier> parseModifiers(Stream<MatchResult> modifiersInput) {
        return modifiersInput.map(result -> {
            ModifierType type = ModifierType.getLiteralCache().get(result.group(1));
            Integer value = null;
            if (result.group(2) != null) {
                value = Integer.parseInt(result.group(2));
            }
            Selector selector = null;
            Integer selectorValue = null;
            if (result.group(3) != null) {
                selector = Selector.getLiteralCache().get(result.group(3));
            }
            if (result.group(4) != null) {
                selectorValue = Integer.parseInt(result.group(4));
            }
            return new Modifier(type, value, selector, selectorValue);
        }).collect(Collectors.toList());
    }

}

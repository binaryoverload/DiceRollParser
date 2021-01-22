package uk.co.binaryoverload.dicerollparser;

import uk.co.binaryoverload.dicerollparser.enums.ModifierType;
import uk.co.binaryoverload.dicerollparser.enums.Operator;
import uk.co.binaryoverload.dicerollparser.enums.Selector;
import uk.co.binaryoverload.dicerollparser.objects.AddedValue;
import uk.co.binaryoverload.dicerollparser.objects.DiceRoll;
import uk.co.binaryoverload.dicerollparser.objects.Die;
import uk.co.binaryoverload.dicerollparser.objects.Modifier;

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

    private static final Pattern ROLL_PATTERN =
            //@formatter:off
            Pattern.compile(
                    "(?:" +
                            "(\\d+d\\d+)" + // Capture Group 1, matches a single instance of 1d20
                            "|" + // or
                            "\\(((?:\\d+d\\d+,?)+)\\)" + // Capture Group 2, matches a CSV list of 1d20,2d20 instead ()
                    ")" +
                    "(" + // Capture Group 3 (Is optional), matches a single or multiple selectors
                        "(?:" +
                            "(?:k|p|mi|ma|rr|ro|ra|e)" + // Modifier Type
                            "(?:\\d+)?" + // Modifier Value (Optional)
                            "(?:[><lh]\\d+)?" + // Selector and value (Optional)
                        ")+" + // One or more selectors
                    ")?" +
                    "((?:[+\\-*\\/]\\d+)+(?!d))?" + // Capture Group 4 (Is optional), matches a list of operators (+4*3)
                    "(?:\\[(.+)\\])?"+ // Capture Group 5 (Is optional), matches a [label] with any text inside
                    "(?:([+\\-*\\/])|$)"); // Capture Group 6, matches the operator between dice rolls
            //@formatter:off

    private static final Pattern ADDED_VALUE_PATTERN = Pattern.compile("([+\\-*\\/])(\\d+)");
    private static final Pattern DICE_PATTERN = Pattern.compile("^(\\d+)d(\\d+)$");
    private static final Pattern MODIFIER_PATTERN = Pattern.compile("(k|p|mi|ma|rr|ro|ra|e)(\\d+)?(?:([><lh])(\\d+))?");

    public static void main(String[] args) {

        List<String> inputRolls = List.of(
                "1d20",
                "1d20ra6",
                "2d20kh1+4",
                "2d20kl1-2",
                "4d6ro<3",
                "2d6e6e10",
                "4d6mi2[fire]+2d20kh1+4+7+11+3d10ra10",
                "(1d6,1d8,1d10)kh2"
        );

        for (String inputRoll : inputRolls) {

            System.out.println("\n ---- New Input ---- \n");

            List<DiceRoll> rolls = parseDiceRoll(inputRoll);
            rolls.forEach(System.out::println);
        }


    }

    public static List<DiceRoll> parseDiceRoll(String input) {
        Matcher rollMatcher = ROLL_PATTERN.matcher(input);

        List<DiceRoll> rolls = new ArrayList<>();
        while (rollMatcher.find()) {

            String[] diceCombos = new String[0];
            if (rollMatcher.group(1) != null) {
                diceCombos = new String[]{rollMatcher.group(1)};
            } else if (rollMatcher.group(2) != null) {
                diceCombos = rollMatcher.group(2).split("\\s*,\\s*");
            } else {
                throw new RuntimeException("No dice could be parsed!");
            }

            List<Die> dice = parseDie(diceCombos);

            Stream<MatchResult> modifiersInput = Stream.of();
            if (rollMatcher.group(3) != null) {
                modifiersInput = MODIFIER_PATTERN.matcher(rollMatcher.group(3)).results();
            }
            List<Modifier> modifiers = parseModifiers(modifiersInput);


            Stream<MatchResult> addedValueInput = Stream.of();
            if (rollMatcher.group(4) != null) {
                addedValueInput = ADDED_VALUE_PATTERN.matcher(rollMatcher.group(4)).results();
            }
            List<AddedValue> addedValues = parseAddedValues(addedValueInput);

            String label = rollMatcher.group(5);

            Operator operator = null;
            if (rollMatcher.group(6) != null) {
                operator = Operator.getLiteralCache().get(rollMatcher.group(6));
            }

            rolls.add(new DiceRoll(dice, label, modifiers, addedValues, operator));
        }
        return rolls;
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

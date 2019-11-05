package seedu.guilttrip.model.entry;

import static java.util.Objects.requireNonNull;
import static seedu.guilttrip.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's phone number in the guilttrip book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Amount {

    public static final double MAX_VALUE = Double.parseDouble("9999999.99");
    public static final String MESSAGE_CONSTRAINTS =
            "Amount should only contain numbers, and it should be only 2 decimal points at maximum. It "
                    + "should also non-zero and not be bigger than " + MAX_VALUE;
    public static final String VALIDATION_REGEX = "[0-9]+([.][0-9]{1,2})?";
    public final double value;

    /**
     * Constructs an {@code Amount}.
     *
     * @param amount A valid phone number.
     */
    public Amount(String amount) {
        requireNonNull(amount);
        checkArgument(isValidAmount(amount), MESSAGE_CONSTRAINTS);
        double amt = Double.parseDouble((amount));
        checkArgument(isValidValue(amt), MESSAGE_CONSTRAINTS);
        value = amt;
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidAmount(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public static boolean isValidValue(double test) {
        return test <= MAX_VALUE && test > 0;
    }

    @Override
    public String toString() {
        return value + "";
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Amount) // instanceof handles nulls
                && value == ((Amount) other).value; // state check
    }

}

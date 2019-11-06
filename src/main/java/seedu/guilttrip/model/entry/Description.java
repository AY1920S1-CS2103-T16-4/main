package seedu.guilttrip.model.entry;

import static java.util.Objects.requireNonNull;
import static seedu.guilttrip.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the guilttrip book. Guarantees: immutable; is
 * valid as declared in {@link #isValidDescription(String)}
 */
public class Description {

    public static final String MESSAGE_CONSTRAINTS =
            "Description should not be blank";

    /*
     * The first character of the guilttrip must not be a whitespace, otherwise " " (a
     * blank string) becomes a valid input.
     */
    // public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String fullDesc;

    /**
     * Constructs a {@code Name}.
     *
     * @param desc A valid name.
     */
    public Description(String desc) {
        requireNonNull(desc);
        checkArgument(isValidDescription(desc), MESSAGE_CONSTRAINTS);
        fullDesc = desc;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidDescription(String test) {
        return !test.trim().isEmpty();
    }

    @Override
    public String toString() {
        return fullDesc;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Description // instanceof handles nulls
                        && fullDesc.equals(((Description) other).fullDesc)); // state check
    }

    @Override
    public int hashCode() {
        return fullDesc.hashCode();
    }

}

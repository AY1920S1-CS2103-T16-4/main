package seedu.address.model.entry;

import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents an Expense.
 */
public class Wish extends Entry {

    private static final String ENTRY_TYPE = "Wish";

    public Wish(Category cat, Description desc, Date date, Amount amount, Set<Tag> tags) {
        super(cat, desc, date, amount, tags);
    }

    public String getType() {
        return this.ENTRY_TYPE;
    }


    /**
     * Returns true if both expenses have the same data fields.
     * This defines a stronger notion of equality between two entries.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Wish)) {
            return false;
        }

        Wish otherWish = (Wish) other;
        return otherWish.getCategory().equals(getCategory())
                && otherWish.getDesc().equals(getDesc())
                && otherWish.getAmount().equals(getAmount())
                && otherWish.getTags().equals(getTags())
                && otherWish.getDate().equals(getDate());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(ENTRY_TYPE + ": ")
                .append(" | Category: ")
                .append(getCategory())
                .append(" Description: ")
                .append(getDesc())
                .append(" Amount: ")
                .append(getAmount())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        builder.append(" (" + getDate() + ")");
        return builder.toString();
    }

}

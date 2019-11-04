package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.entry.Amount;
import seedu.address.model.entry.Category;
import seedu.address.model.entry.Date;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Entry objects.
 */
public class EntryBuilder {

    public static final String DEFAULT_CATEGORY = "FOOD";
    public static final String DEFAULT_DESCRIPTION = "Alice Pauline";
    public static final String DEFAULT_AMOUNT = "5.60";
    public static final String DEFAULT_TIME = "2019-09-09";

    private Category cat;
    private Description desc;
    private Amount amt;
    private Date date;
    private Set<Tag> tags;

    public EntryBuilder() {
        cat = new Category(DEFAULT_CATEGORY, "Expense");
        desc = new Description(DEFAULT_DESCRIPTION);
        amt = new Amount(DEFAULT_AMOUNT);
        date = new Date(DEFAULT_TIME);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public EntryBuilder(Entry entryToCopy) {
        desc = entryToCopy.getDesc();
        amt = entryToCopy.getAmount();
        tags = new HashSet<>(entryToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public EntryBuilder withDesc(String desc) {
        this.desc = new Description(desc);
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public EntryBuilder withTime(String time) {
        this.date = new Date(time);
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public EntryBuilder withAmt(double amt) {
        this.amt = new Amount(Double.toString(amt));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public EntryBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    public Entry build() {
        return new Entry(cat, desc, date, amt, tags);
    }

}

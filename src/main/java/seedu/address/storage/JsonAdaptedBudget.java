package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Entry}.
 */
class JsonAdaptedBudget {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Budget's %s field is missing!";
    private final String category;
    private final String desc;
    private final String date;
    private final double amt;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedBudget} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedBudget(@JsonProperty("category") String category, @JsonProperty("desc") String desc,
                             @JsonProperty("amt") double amt, @JsonProperty("time") String time,
                             @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.category = category;
        this.desc = desc;
        this.amt = amt;
        this.date = date;

        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }

    /**
     * Converts a given {@code Budget} into this class for Jackson use.
     */
    public JsonAdaptedBudget(Budget source) {
        category = source.getCategory().categoryName;
        desc = source.getDesc().fullDesc;
        amt = source.getAmount().value;
        date = source.getDate().toString();
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted budget object into the model's {@code Budget} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted budget.
     */
    public Budget toModelType() throws IllegalValueException {
        final List<Tag> entryTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            entryTags.add(tag.toModelType());
        }

        if (desc == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(desc)) {
            throw new IllegalValueException(Description.MESSAGE_CONSTRAINTS);
        }

        final Category modelCategory = new Category(category);

        final Description modelDesc = new Description(desc);

        final Date modelDate = new Date(date);

        final Amount modelAmt = new Amount(amt);

        final Set<Tag> modelTags = new HashSet<>(entryTags);
        return new Budget(modelCategory, modelDesc, modelDate, modelAmt, modelTags);
    }

}

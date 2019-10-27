package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Amount;
import seedu.address.model.person.Category;
import seedu.address.model.person.Date;
import seedu.address.model.person.Description;
import seedu.address.model.person.Wish;
import seedu.address.model.tag.Tag;


/**
 * Jackson-friendly version of {@link Entry}.
 */
class JsonAdaptedWish {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Wish's %s field is missing!";

    private final String category;
    private final String desc;
    private final String date;
    private final double amt;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedWish(@JsonProperty("category") String category, @JsonProperty("desc") String desc,
                           @JsonProperty("amt") double amt, @JsonProperty("date") String date,
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
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedWish(Wish source) {
        category = source.getCategory().categoryName;
        desc = source.getDesc().fullDesc;
        amt = source.getAmount().value;
        date = source.getDate().toString();
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Wish toModelType() throws IllegalValueException {
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
        final Description modelDesc = new Description(desc);

        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Date.class.getSimpleName()));
        }
        /*if (!Date.isValidDate(date)) {
            throw new IllegalValueException((Date.MESSAGE_CONSTRAINTS));
        }*/
     
        final Date modelDate = new Date(time);
        
        final Category modelCategory = new Category(category, "Expense");
        final Amount modelAmt = new Amount(amt);
        final Set<Tag> modelTags = new HashSet<>(entryTags);
        return new Wish(modelCategory, modelDesc, modelDate, modelAmt, modelTags);
    }
}

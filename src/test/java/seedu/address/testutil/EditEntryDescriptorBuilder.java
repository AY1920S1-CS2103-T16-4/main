package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.address.model.entry.Amount;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditEntryDescriptorBuilder {

    private EditEntryDescriptor descriptor;

    public EditEntryDescriptorBuilder() {
        descriptor = new EditEntryDescriptor();
    }

    public EditEntryDescriptorBuilder(EditEntryDescriptor descriptor) {
        this.descriptor = new EditEntryDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code entry}'s details
     */
    public EditEntryDescriptorBuilder(Entry entry) {
        descriptor = new EditEntryDescriptor();
        descriptor.setDesc(entry.getDesc());
        descriptor.setAmount(entry.getAmount());
        //        descriptor.setPhone(entry.getPhone());
        //        descriptor.setEmail(entry.getEmail());
        //        descriptor.setAddress(entry.getAddress());
        descriptor.setTags(entry.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditEntryDescriptorBuilder withDescription(String desc) {
        descriptor.setDesc(new Description(desc));
        return this;
    }

    /**
     * Sets the Amount of the EditEntryDescriptor that we are building.
     *
     * @param amt amount to be added
     * @return
     */
    public EditEntryDescriptorBuilder withAmount(double amt) {
        descriptor.setAmount(new Amount(Double.toString(amt)));
        return this;
    }

    //    /**
    //     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
    //     */
    //    public EditEntryDescriptorBuilder withPhone(String phone) {
    //        descriptor.setPhone(new Phone(phone));
    //        return this;
    //    }
    //
    //    /**
    //     * Sets the {@code Email} of the {@code EditPersonDescriptor} that we are building.
    //     */
    //    public EditEntryDescriptorBuilder withEmail(String email) {
    //        descriptor.setEmail(new Email(email));
    //        return this;
    //    }
    //
    //    /**
    //     * Sets the {@code Address} of the {@code EditPersonDescriptor} that we are building.
    //     */
    //    public EditEntryDescriptorBuilder withAddress(String address) {
    //        descriptor.setAddress(new Address(address));
    //        return this;
    //    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditEntryDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditEntryDescriptor build() {
        return descriptor;
    }
}

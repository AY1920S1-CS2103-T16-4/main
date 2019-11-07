package seedu.guilttrip.logic.commands.remindercommands;

import static java.util.Objects.requireNonNull;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_DESC;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_LOWER_BOUND;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_TYPE;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_UPPER_BOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.guilttrip.commons.core.index.Index;
import seedu.guilttrip.logic.CommandHistory;
import seedu.guilttrip.logic.commands.Command;
import seedu.guilttrip.logic.commands.CommandResult;
import seedu.guilttrip.logic.commands.exceptions.CommandException;
import seedu.guilttrip.model.Model;
import seedu.guilttrip.model.entry.Amount;
import seedu.guilttrip.model.entry.Date;
import seedu.guilttrip.model.entry.Description;
import seedu.guilttrip.model.reminders.GeneralReminder;
import seedu.guilttrip.model.reminders.conditions.Condition;
import seedu.guilttrip.model.reminders.conditions.DateCondition;
import seedu.guilttrip.model.reminders.conditions.QuotaCondition;
import seedu.guilttrip.model.reminders.conditions.TagsCondition;
import seedu.guilttrip.model.reminders.conditions.TypeCondition;
import seedu.guilttrip.model.tag.Tag;


/**
 * Adds a entry to the guilttrip book.
 */
public class AddGeneralReminderCommand extends Command {

    public static final String COMMAND_WORD = "addReminder";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a GeneralReminder to reminders list. "
            + "Parameters: "
            + PREFIX_TYPE + "INCOME OR EXPENSE "
            + PREFIX_DESC + "REMINDER HEADER "
            + PREFIX_LOWER_BOUND + "LOWERBOUND FOR AMT "
            + PREFIX_UPPER_BOUND + "UPPERBOUND FOR AMT "
            + PREFIX_START_DATE + "START DATE "
            + PREFIX_END_DATE + "END DATE"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DESC + "Don't be broke. "
            + PREFIX_LOWER_BOUND + "10 "
            + PREFIX_UPPER_BOUND + "20 "
            + PREFIX_START_DATE + "2019/01/01 "
            + PREFIX_END_DATE + "2019/12/30 \n";

    public static final String MESSAGE_SUCCESS = "New GeneralReminder added: %1$s";

    private Description message;
    private String type = "expense";
    private Double lowerBound;
    private Double upperBound;
    private Date start;
    private Date end;
    private Set<Tag> tagList;
    private List<Condition> conditions;
    private GeneralReminder reminder;

    /**
     * Creates an AddCommand to add the specified {@code GeneralReminder} with no tracker.
     */
    public AddGeneralReminderCommand(Description message) {
        this.message = message;
        conditions.add(new TypeCondition("expense"));
    }

    public void setType(String type) {
        this.type = type;
        conditions.add(new TypeCondition(type));
    }

    public void setLowerBound(Double lowerBound) {
        this.lowerBound = lowerBound;
        conditions.add(new QuotaCondition(lowerBound, true));
    }

    public void setUpperBound(Double upperBound) {
        this.upperBound = upperBound;
        conditions.add(new QuotaCondition(upperBound, false));
    }

    public void setStart(Date start) {
        this.start = start;
        conditions.add(new DateCondition(start, true));
    }

    public void setEnd(Date end) {
        this.end = end;
        conditions.add(new DateCondition(end, false));
    }

    public void setTagList(Set<Tag> tagList) {
        this.tagList = tagList;
        List<Tag> tags = new ArrayList<>(tagList);
        conditions.add(new TagsCondition(tags));
    }

    /**
     * Creates an AddCommand to add the specified {@code GeneralReminder} with tracker.
     */
    public AddGeneralReminderCommand(
            Description message, List<Index> conditionIndexes, Amount quota) {
        this.message = message;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        this.reminder = new GeneralReminder(message, conditions);
        for (Condition condition : conditions) {
            model.addCondition(condition);
        }
        model.addReminder(reminder);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_SUCCESS, reminder));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddGeneralReminderCommand // instanceof handles nulls
                && reminder.equals(((AddGeneralReminderCommand) other).reminder));
    }
}

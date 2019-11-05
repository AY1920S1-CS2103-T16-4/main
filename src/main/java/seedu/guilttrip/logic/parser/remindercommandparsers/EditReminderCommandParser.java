package seedu.guilttrip.logic.parser.remindercommandparsers;

import static java.util.Objects.requireNonNull;
import static seedu.guilttrip.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_DESC;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_TRACKER_TYPE;

import seedu.guilttrip.commons.core.index.Index;
import seedu.guilttrip.logic.commands.remindercommands.EditReminderCommand;
import seedu.guilttrip.logic.parser.ArgumentMultimap;
import seedu.guilttrip.logic.parser.ArgumentTokenizer;
import seedu.guilttrip.logic.parser.Parser;
import seedu.guilttrip.logic.parser.ParserUtil;
import seedu.guilttrip.logic.parser.exceptions.ParseException;
import seedu.guilttrip.model.reminders.Reminder;

/**
 * Parses input arguments and creates a new EditExpenseCommand object
 */
public class EditReminderCommandParser implements Parser<EditReminderCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditExpenseCommand
     * and returns an EditExpenseCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditReminderCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DESC, PREFIX_INDEX, PREFIX_TRACKER_TYPE, PREFIX_AMOUNT);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format
                    (MESSAGE_INVALID_COMMAND_FORMAT, EditReminderCommand.MESSAGE_USAGE), pe);
        }

        EditReminderCommand.EditReminderDescriptor editReminderDescriptor =
                new EditReminderCommand.EditReminderDescriptor();
        if (argMultimap.getValue(PREFIX_DESC).isPresent()) {
            editReminderDescriptor.setDesc(
                    ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESC).get()));
        }

        if (argMultimap.getValue(PREFIX_AMOUNT).isPresent()) {
            editReminderDescriptor.setQuota(
                    (Double) ParserUtil.parseAmount(argMultimap.getValue(PREFIX_AMOUNT).get()).value);
        }
        if (argMultimap.getValue(PREFIX_INDEX).isPresent()) {
            editReminderDescriptor.setConditionIndices(
                    ParserUtil.parseIndexes(argMultimap.getAllValues(PREFIX_INDEX)));
        }

        if (argMultimap.getValue(PREFIX_TRACKER_TYPE).isPresent()) {
            editReminderDescriptor.setTrackerType(
                    Reminder.TrackerType.parse(argMultimap.getValue(PREFIX_TRACKER_TYPE).get()));
        }

        if (!editReminderDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditReminderCommand.MESSAGE_NOT_EDITED);
        }

        return new EditReminderCommand(index, editReminderDescriptor);
    }
}

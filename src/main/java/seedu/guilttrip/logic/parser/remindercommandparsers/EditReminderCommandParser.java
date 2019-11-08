package seedu.guilttrip.logic.parser.remindercommandparsers;

import static java.util.Objects.requireNonNull;
import static seedu.guilttrip.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_DESC;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_LOWER_BOUND;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_PERIOD;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.guilttrip.logic.parser.CliSyntax.PREFIX_UPPER_BOUND;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.guilttrip.commons.core.index.Index;
import seedu.guilttrip.logic.commands.remindercommands.EditReminderCommand;
import seedu.guilttrip.logic.commands.remindercommands.EditReminderCommand.EditGeneralReminderDescriptor;
import seedu.guilttrip.logic.parser.ArgumentMultimap;
import seedu.guilttrip.logic.parser.ArgumentTokenizer;
import seedu.guilttrip.logic.parser.Parser;
import seedu.guilttrip.logic.parser.ParserUtil;
import seedu.guilttrip.logic.parser.Prefix;
import seedu.guilttrip.logic.parser.exceptions.ParseException;
import seedu.guilttrip.model.tag.Tag;

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
                ArgumentTokenizer.tokenize(args,
                        PREFIX_DESC, PREFIX_UPPER_BOUND, PREFIX_LOWER_BOUND,
                        PREFIX_START_DATE, PREFIX_END_DATE, PREFIX_TAG, PREFIX_AMOUNT, PREFIX_PERIOD);

        Index index;

        boolean isGeneralReminder = isPrefixPresent(argMultimap, PREFIX_DESC, PREFIX_UPPER_BOUND, PREFIX_LOWER_BOUND,
                PREFIX_START_DATE, PREFIX_END_DATE, PREFIX_TAG);
        boolean isEntryReminder = isPrefixPresent(argMultimap, PREFIX_AMOUNT, PREFIX_PERIOD);

        if (isEntryReminder && isEntryReminder) {
            throw new ParseException(EditReminderCommand.MISMATCHING_REMINDER_TYPES);
        }

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format
                    (MESSAGE_INVALID_COMMAND_FORMAT, EditReminderCommand.MESSAGE_USAGE), pe);
        }

        EditReminderCommand.EditReminderDescriptor editReminderDescriptor;
        if (isGeneralReminder) {
            editReminderDescriptor = new EditReminderCommand.EditGeneralReminderDescriptor();
            EditGeneralReminderDescriptor editGeneralReminderDescriptor =
                    (EditGeneralReminderDescriptor) editReminderDescriptor;
            if (argMultimap.getValue(PREFIX_LOWER_BOUND).isPresent()) {
                editGeneralReminderDescriptor.setLowerBound(
                        ParserUtil.parseAmount(argMultimap.getValue(PREFIX_LOWER_BOUND).get()).value);
            }
            if (argMultimap.getValue(PREFIX_UPPER_BOUND).isPresent()) {
                editGeneralReminderDescriptor.setUpperBound(
                        ParserUtil.parseAmount(argMultimap.getValue(PREFIX_UPPER_BOUND).get()).value);
            }
            if (argMultimap.getValue(PREFIX_START_DATE).isPresent()) {
                editGeneralReminderDescriptor.setStart(
                        ParserUtil.parseDate(argMultimap.getValue(PREFIX_START_DATE).get()));
            }
            if (argMultimap.getValue(PREFIX_END_DATE).isPresent()) {
                editGeneralReminderDescriptor.setEnd(
                        ParserUtil.parseDate(argMultimap.getValue(PREFIX_END_DATE).get()));
            }
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editGeneralReminderDescriptor::setTags);
            return new EditReminderCommand(editReminderDescriptor);
        } else {
            throw new ParseException(EditReminderCommand.MISMATCHING_REMINDER_TYPES);
        }
    }


    private static boolean isPrefixPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }
}

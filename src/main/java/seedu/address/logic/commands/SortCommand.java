package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEQUENCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.entry.SortSequence;
import seedu.address.model.entry.SortType;

/**
 * Sorts the list according to sortType and sortSequence
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts the list of entries in guiltTrip(). \n"
            + "Parameters: "
            + PREFIX_TYPE + "TYPE "
            + PREFIX_SEQUENCE + "SEQUENCE "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TYPE + "Time "
            + PREFIX_SEQUENCE + "Ascending ";

    public static final String MESSAGE_SUCCESS = "Sorted all entries by %s";

    private final SortType type;
    private final SortSequence sequence;

    public SortCommand(SortType type, SortSequence sequence) {
        this.type = type;
        this.sequence = sequence;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.sortFilteredEntry(type, sequence);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_SUCCESS, type));
    }

}

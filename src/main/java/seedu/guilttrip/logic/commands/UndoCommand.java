package seedu.guilttrip.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.guilttrip.commons.core.step.Step;
import seedu.guilttrip.logic.CommandHistory;
import seedu.guilttrip.logic.commands.exceptions.CommandException;
import seedu.guilttrip.model.Model;


/**
 * Reverts the {@code model}'s finance tracker to its previous state.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo success!";
    public static final String MESSAGE_FAILURE = "There are insufficient commands to undo!";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undo the most recent command that modified the state of the finance tracker\n"
            + "If number n is provided, the most recent n number of commands will be undone\n"
            + "Parameters: NUMBER (must be a positive integer)\n"
            + "Example 1: " + COMMAND_WORD + "\n"
            + "Example 2: " + COMMAND_WORD + " 2";

    public final Step step;

    public UndoCommand(Step step) {
        this.step = step;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (!model.canUndoAddressBook(step)) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        for (long l = 0; l < step.value; l++) {
            model.undoAddressBook();
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}

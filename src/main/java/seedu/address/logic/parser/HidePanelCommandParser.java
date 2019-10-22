package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.HidePanelCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PanelName;

public class HidePanelCommandParser implements Parser<HidePanelCommand> {

    public HidePanelCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        PanelName panelName;

        try {
            panelName = ParserUtil.parsePanelName(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HidePanelCommand.MESSAGE_USAGE), pe);
        }

        return new HidePanelCommand(panelName);
    }
}

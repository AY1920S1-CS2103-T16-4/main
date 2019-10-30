package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Objects;

import seedu.address.model.person.PanelName;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    private final PanelName panelName;
    private final boolean togglePanel;
    private final boolean toggleStats;
    private final boolean toggleGraphics;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.panelName = null;
        this.togglePanel = false;
        this.toggleStats = false;
        this.toggleGraphics = false;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    public CommandResult(String feedbackToUser, ArrayList<Boolean> toggleBooleans) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = false;
        this.exit = false;
        this.togglePanel = false;
        this.panelName = null;
        this.toggleStats = toggleBooleans.get(0);
        this.toggleGraphics = toggleBooleans.get(1);
    }

    public CommandResult(String feedbackToUser, PanelName panelName, boolean togglePanel) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = false;
        this.exit = false;
        this.panelName = panelName;
        this.togglePanel = togglePanel;
        this.toggleStats = false;
        this.toggleGraphics = false;
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean isTogglePanel() {
        return togglePanel;
    }

    public boolean isToggleStats() {
        return toggleStats;
    }

    public boolean isToggleGraphics() {
        return toggleGraphics;
    }

    public PanelName getPanelName() {
        return this.panelName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit
                && togglePanel == otherCommandResult.togglePanel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, panelName, togglePanel);
    }

}

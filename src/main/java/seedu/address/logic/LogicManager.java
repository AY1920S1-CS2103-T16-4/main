package seedu.address.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.AutoExpense;
import seedu.address.model.person.Budget;
import seedu.address.model.person.Entry;
import seedu.address.model.person.ExpenseReminder;
import seedu.address.model.person.Wish;
import seedu.address.model.statistics.CategoryStatistics;
import seedu.address.model.statistics.Statistics;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Statistics stats;
    private final Storage storage;
    private final CommandHistory history;
    private final AddressBookParser addressBookParser;
    private boolean addressBookModified;

    public LogicManager(Model model, Statistics stats, Storage storage) {
        this.model = model;
        this.stats = stats;
        this.storage = storage;
        history = new CommandHistory();
        addressBookParser = new AddressBookParser();

        //Set addressBookModified to true whenever the models' addressbook is modified.
        model.getAddressBook().addListener(observable -> addressBookModified = true);
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException, IllegalArgumentException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        try {
            Command command = addressBookParser.parseCommand(commandText);
            commandResult = command.execute(model, history);
        } finally {
            history.add(commandText);
        }

        if (addressBookModified) {
            logger.info("Finance tracker modified, saving to file");
            try {
                storage.saveAddressBook(model.getAddressBook());
            } catch (IOException ioe) {
                throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
            }
        }

        return commandResult;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<CategoryStatistics> getListOfStatsForExpense() {
        return stats.getListOfStatsForExpense();
    }

    @Override
    public ObservableList<CategoryStatistics> getListOfStatsForIncome() {
        return stats.getListOfStatsForIncome();
    }

    @Override
    public ObservableList<Entry> getFilteredExpenseAndIncomeList() {
        return model.getFilteredExpensesAndIncomes();
    }

    @Override
    public ObservableList<AutoExpense> getFilteredAutoExpenseList() {
        return model.getFilteredAutoExpenses();
    }

    @Override
    public ObservableList<Wish> getFilteredWishList() {
        return model.getFilteredWishes();
    }

    @Override
    public ObservableList<Budget> getFilteredBudgetList() {
        return model.getFilteredBudgets();
    }

    @Override
    public ObservableList<ExpenseReminder> getFilteredExpenseReminderList() {
        return model.getFilteredExpenseReminders();
    }

    @Override
    public ObservableList<String> getHistory() {
        return history.getHistory();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}

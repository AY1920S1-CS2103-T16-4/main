package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Entry;
import seedu.address.model.person.Expense;
import seedu.address.model.person.SortType;
import seedu.address.model.util.ComparatorType;
import seedu.address.model.util.EntryComparator;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    private final SortType sortByDescription = new SortType("description");
    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final SortedList<Expense> sortedExpenseList;
    private final FilteredList<Expense> filteredExpenseList;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        sortedExpenseList = new SortedList<>(this.addressBook.getExpenseList());
        sortedExpenseList.setComparator(new EntryComparator(sortByDescription));
        filteredExpenseList = new FilteredList<>(sortedExpenseList);
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasEntry(Entry entry) {
        requireNonNull(entry);
        return addressBook.hasEntry(entry);
    }

    @Override
    public void deleteEntry(Entry target) {
        addressBook.removeEntry(target);
    }

    @Override
    public void addEntry(Entry entry) {
        addressBook.addEntry(entry);
        sortFilteredExpenseList(sortByDescription);
        updateFilteredExpenseList(PREDICATE_SHOW_ALL_ENTRIES);
    }

    @Override
    public void addExpense(Expense expense) {
        addressBook.addExpense(expense);
        sortFilteredExpenseList(sortByDescription);
        updateFilteredExpenseList(PREDICATE_SHOW_ALL_ENTRIES);
    }

    @Override
    public void setEntry(Entry target, Entry editedEntry) {
        requireAllNonNull(target, editedEntry);
        addressBook.setEntry(target, editedEntry);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Expense> getFilteredExpenseList() {
        return filteredExpenseList;
    }

    @Override
    public void updateFilteredExpenseList(Predicate<Entry> predicate) {
        requireNonNull(predicate);
        filteredExpenseList.setPredicate(predicate);
    }

    @Override
    public void sortFilteredExpenseList(SortType c) {
        sortedExpenseList.setComparator(new EntryComparator(c));
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && userPrefs.equals(other.userPrefs)
                && filteredExpenseList.equals(other.filteredExpenseList);
    }

}

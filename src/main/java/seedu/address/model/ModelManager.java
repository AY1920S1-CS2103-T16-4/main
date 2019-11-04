package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.entry.AutoExpense;
import seedu.address.model.entry.Budget;
import seedu.address.model.entry.Category;
import seedu.address.model.entry.CategoryList;
import seedu.address.model.entry.Date;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Expense;
import seedu.address.model.entry.Income;
import seedu.address.model.entry.SortSequence;
import seedu.address.model.entry.SortType;
import seedu.address.model.entry.Wish;
import seedu.address.model.reminders.Reminder;
import seedu.address.model.reminders.conditions.Condition;
import seedu.address.model.statistics.CategoryStatistics;
import seedu.address.model.statistics.DailyStatistics;
import seedu.address.model.statistics.StatisticsManager;
import seedu.address.model.util.EntryComparator;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    private StatisticsManager stats;
    private final SortType sortByTime = new SortType("time");
    private final SortSequence sortByAsc = new SortSequence("descending");
    private final UserPrefs userPrefs;
    private final ObservableList<Category> incomeCategoryList;
    private final ObservableList<Category> expenseCategoryList;
    private final FilteredList<Entry> filteredEntries;
    private final FilteredList<Expense> filteredExpenses;
    private final FilteredList<Income> filteredIncomes;
    private final FilteredList<Wish> filteredWishes;
    private final FilteredList<Budget> filteredBudgets;
    private final FilteredList<AutoExpense> filteredAutoExpenses;
    private final SortedList<Entry> sortedEntryList;
    private final FilteredList<Reminder> filteredReminders;
    private final FilteredList<Condition> filteredConditions;
    private final VersionedGuiltTrip versionedAddressBook;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyGuiltTrip addressBook, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        versionedAddressBook = new VersionedGuiltTrip(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        incomeCategoryList = versionedAddressBook.getIncomeCategoryList();
        expenseCategoryList = versionedAddressBook.getExpenseCategoryList();
        filteredExpenses = new FilteredList<>(versionedAddressBook.getExpenseList());
        filteredIncomes = new FilteredList<>(versionedAddressBook.getIncomeList());
        filteredWishes = new FilteredList<>(versionedAddressBook.getWishList());
        filteredBudgets = new FilteredList<>(versionedAddressBook.getBudgetList());
        filteredAutoExpenses = new FilteredList<>(versionedAddressBook.getAutoExpenseList());
        sortedEntryList = new SortedList<>(versionedAddressBook.getEntryList());
        sortedEntryList.setComparator(new EntryComparator(sortByTime, sortByAsc));
        filteredEntries = new FilteredList<>(sortedEntryList);
        filteredReminders = new FilteredList<>(versionedAddressBook.getReminderList());
        filteredConditions = new FilteredList<>(versionedAddressBook.getConditionList());
        createExpensesfromAutoExpenses();
        this.stats = new StatisticsManager(this.filteredExpenses, this.filteredIncomes,
                versionedAddressBook.getCategoryList());
    }

    public ModelManager() {
        this(new GuiltTrip(false), new UserPrefs());
    }

    // =========== UserPrefs
    // ==================================================================================
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

    // =========== GuiltTrip
    // ================================================================================

    @Override
    public void setAddressBook(ReadOnlyGuiltTrip addressBook) {
        versionedAddressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyGuiltTrip getAddressBook() {
        return versionedAddressBook;
    }

    @Override
    public boolean hasCategory(Category category) {
        return versionedAddressBook.hasCategory(category);
    }

    @Override
    public boolean hasEntry(Entry entry) {
        requireNonNull(entry);
        return versionedAddressBook.hasEntry(entry);
    }

    @Override
    public boolean hasBudget(Budget budget) {
        requireNonNull(budget);
        return versionedAddressBook.hasBudget(budget);
    }

    @Override
    public boolean hasWish(Wish wish) {
        requireNonNull(wish);
        return versionedAddressBook.hasWish(wish);
    }

    @Override
    public boolean hasReminder(Reminder reminder) {
        requireNonNull(reminder);
        return versionedAddressBook.hasReminder(reminder);
    }

    @Override
    public boolean hasCondition(Condition condition) {
        requireNonNull(condition);
        return versionedAddressBook.hasCondition(condition);
    }

    @Override
    public void deleteCategory(Category target) {
        versionedAddressBook.removeCategory(target);
    }

    @Override
    public void deleteEntry(Entry target) {
        versionedAddressBook.removeEntry(target);
        if (target instanceof Expense) {
            versionedAddressBook.removeExpense((Expense) target);
            versionedAddressBook.updateBudgets(filteredExpenses);
            updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
        } else if (target instanceof Income) {
            versionedAddressBook.removeIncome((Income) target);
        } else if (target instanceof Wish) {
            versionedAddressBook.removeWish((Wish) target);
        } else if (target instanceof Budget) {
            versionedAddressBook.removeBudget((Budget) target);
        }
        filteredReminders.filtered(PREDICATE_SHOW_ACTIVE_REMINDERS);
        filteredReminders.filtered(PREDICATE_SHOW_ALL_REMINDERS);
    }

    @Override
    public void deleteExpense(Expense target) {
        versionedAddressBook.removeEntry(target);
        versionedAddressBook.removeExpense(target);
        versionedAddressBook.updateBudgets(filteredExpenses);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
        filteredReminders.filtered(PREDICATE_SHOW_ACTIVE_REMINDERS);
        filteredReminders.filtered(PREDICATE_SHOW_ALL_REMINDERS);
    }

    @Override
    public void deleteIncome(Income target) {
        versionedAddressBook.removeEntry(target);
        versionedAddressBook.removeIncome(target);
        filteredReminders.filtered(PREDICATE_SHOW_ACTIVE_REMINDERS);
        filteredReminders.filtered(PREDICATE_SHOW_ALL_REMINDERS);
    }

    @Override
    public void deleteWish(Wish target) {
        versionedAddressBook.removeWish(target);
        filteredReminders.filtered(PREDICATE_SHOW_ACTIVE_REMINDERS);
        filteredReminders.filtered(PREDICATE_SHOW_ALL_REMINDERS);
    }

    @Override
    public void deleteBudget(Budget target) {
        versionedAddressBook.removeBudget(target);
        filteredReminders.filtered(PREDICATE_SHOW_ACTIVE_REMINDERS);
        filteredReminders.filtered(PREDICATE_SHOW_ALL_REMINDERS);
    }

    @Override
    public void deleteAutoExpense(AutoExpense target) {
        versionedAddressBook.removeEntry(target);
        versionedAddressBook.removeAutoExpense(target);
        filteredReminders.filtered(PREDICATE_SHOW_ACTIVE_REMINDERS);
        filteredReminders.filtered(PREDICATE_SHOW_ALL_REMINDERS);
    }

    @Override
    public void deleteReminder(Reminder target) {
        versionedAddressBook.removeReminder(target);
    }

    @Override
    public void deleteCondition(Condition target) {
        versionedAddressBook.removeCondition(target);
    }

    @Override
    public void addEntry(Entry entry) {
        versionedAddressBook.addEntry(entry);
        if (entry instanceof Expense) {
            versionedAddressBook.addExpense((Expense) entry);
            versionedAddressBook.updateBudgets(filteredExpenses);
            updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
        } else if (entry instanceof Income) {
            versionedAddressBook.addIncome((Income) entry);
        } else if (entry instanceof Wish) {
            versionedAddressBook.addWish((Wish) entry);
        } else if (entry instanceof Budget) {
            versionedAddressBook.addBudget((Budget) entry);
        } else if (entry instanceof AutoExpense) {
            versionedAddressBook.addAutoExpense((AutoExpense) entry);
        }
        sortFilteredEntry(sortByTime, sortByAsc);
    }

    @Override
    public void addCategory(Category category) {
        versionedAddressBook.addCategory(category);
    }

    @Override
    public void addExpense(Expense expense) {
        versionedAddressBook.addExpense(expense);
        versionedAddressBook.updateBudgets(filteredExpenses);
        sortFilteredEntry(sortByTime, sortByAsc);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
    }

    @Override
    public void addIncome(Income income) {
        versionedAddressBook.addIncome(income);
        sortFilteredEntry(sortByTime, sortByAsc);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
        filteredReminders.filtered(PREDICATE_SHOW_ACTIVE_REMINDERS);
        filteredReminders.filtered(PREDICATE_SHOW_ALL_REMINDERS);
    }

    @Override
    public void addWish(Wish wish) {
        versionedAddressBook.addWish(wish);
        sortFilteredEntry(sortByTime, sortByAsc);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
        filteredReminders.filtered(PREDICATE_SHOW_ACTIVE_REMINDERS);
        filteredReminders.filtered(PREDICATE_SHOW_ALL_REMINDERS);
    }

    @Override
    public void addAutoExpense(AutoExpense autoExpense) {
        versionedAddressBook.addAutoExpense(autoExpense);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
        filteredReminders.filtered(PREDICATE_SHOW_ACTIVE_REMINDERS);
        filteredReminders.filtered(PREDICATE_SHOW_ALL_REMINDERS);
    }

    @Override
    public void addBudget(Budget budget) {
        budget.setSpent(filteredExpenses);
        versionedAddressBook.addBudget(budget);
        versionedAddressBook.updateBudgets(filteredExpenses);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
        filteredReminders.filtered(PREDICATE_SHOW_ACTIVE_REMINDERS);
        filteredReminders.filtered(PREDICATE_SHOW_ALL_REMINDERS);
    }

    @Override
    public void addReminder(Reminder reminder) {
        versionedAddressBook.addReminder(reminder);
    }

    @Override
    public void addCondition(Condition condition) {
        versionedAddressBook.addCondition(condition);
    }

    @Override
    public void setCategory(Category target, Category editedCategory) {
        requireAllNonNull(target, editedCategory);
        versionedAddressBook.setCategory(target, editedCategory);
    }

    @Override
    public void setEntry(Entry target, Entry editedEntry) {
        requireAllNonNull(target, editedEntry);
        if (target instanceof Expense) {
            //TODO
            Expense toEditEntry = new Expense(editedEntry.getCategory(), editedEntry.getDesc(), editedEntry.getDate(),
                    editedEntry.getAmount(), editedEntry.getTags());
            Entry entryToEdit = versionedAddressBook.getEntryList().get(versionedAddressBook.getEntryList()
                    .indexOf(target));
            Expense expenseToEdit = versionedAddressBook.getExpenseList().get(versionedAddressBook.getExpenseList()
                    .indexOf(target));
            versionedAddressBook.setEntry(entryToEdit, toEditEntry);
            versionedAddressBook.setExpense(expenseToEdit, toEditEntry);
            versionedAddressBook.updateBudgets(filteredExpenses);
            updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
        } else {
            Income incomeToEdit = versionedAddressBook.getIncomeList().filtered(t -> t == target).get(0);
            Income toEditEntry = new Income(editedEntry.getCategory(), editedEntry.getDesc(), editedEntry.getDate(),
                    editedEntry.getAmount(), editedEntry.getTags());
            versionedAddressBook.setEntry(incomeToEdit, toEditEntry);
            versionedAddressBook.setIncome(incomeToEdit, toEditEntry);
        }
        filteredReminders.filtered(PREDICATE_SHOW_ACTIVE_REMINDERS);
        filteredReminders.filtered(PREDICATE_SHOW_ALL_REMINDERS);
    }

    @Override
    public void setReminder(Reminder target, Reminder editedReminder) {
        requireAllNonNull(target, editedReminder);
        versionedAddressBook.setReminder(target, editedReminder);
    }

    @Override
    public void setCondition(Condition target, Condition editedCondition) {
        requireAllNonNull(target, editedCondition);
        versionedAddressBook.setCondition(target, editedCondition);
    }

    public void setExpense(Expense target, Expense editedEntry) {
        requireAllNonNull(target, editedEntry);
        versionedAddressBook.setEntry(target, editedEntry);
        versionedAddressBook.setExpense(target, editedEntry);
        versionedAddressBook.updateBudgets(filteredExpenses);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
    }

    @Override
    public void setIncome(Income target, Income editedEntry) {
        requireAllNonNull(target, editedEntry);
        versionedAddressBook.setEntry(target, editedEntry);
        versionedAddressBook.setIncome(target, editedEntry);
    }

    @Override
    public void setWish(Wish target, Wish editedWish) {
        requireAllNonNull(target, editedWish);
        versionedAddressBook.setWish(target, editedWish);
    }

    @Override
    public void setBudget(Budget target, Budget editedBudget) {
        requireAllNonNull(target, editedBudget);
        versionedAddressBook.setBudget(target, editedBudget);
    }

    @Override
    public void updateListOfStats() {
        this.stats.updateListOfStats();
    }

    @Override
    public void updateListOfStats(List<Date> period) {
        this.stats.updateListOfStats(period);
    }

    @Override
    public void updateBarCharts() {
        this.stats.updateBarCharts();
    }

    @Override
    public void updateBarCharts(Date month) {
        this.stats.updateBarCharts(month);
    }

    @Override
    public DoubleProperty getTotalExpenseForPeriod() {
        return this.stats.getTotalExpenseForPeriod();
    }

    @Override
    public DoubleProperty getTotalIncomeForPeriod() {
        return this.stats.getTotalIncomeForPeriod();
    }

    @Override
    public ObservableList<DailyStatistics> getListOfStatsForBarChart() {
        return this.stats.getListOfStatsForBarChart();
    }

    @Override
    public ObservableList<CategoryStatistics> getListOfStatsForExpense() {
        return this.stats.getListOfStatsForExpense();
    }

    @Override
    public ObservableList<CategoryStatistics> getListOfStatsForIncome() {
        return this.stats.getListOfStatsForIncome();
    }

    @Override
    public CategoryList getCategoryList() {
        return versionedAddressBook.getCategoryList();
    }
    // =========== Filtered Person List Accessors

    /**
     * Returns an unmodifiable view of the list of {@code Entry} backed by the
     * internal list of {@code versionedAddressBook}
     */

    @Override
    public ObservableList<Category> getExpenseCategoryList() {
        return expenseCategoryList;
    }

    @Override
    public ObservableList<Category> getIncomeCategoryList() {
        return incomeCategoryList;
    }

    @Override
    public ObservableList<Entry> getFilteredEntryList() {
        return filteredEntries;
    }

    @Override
    public ObservableList<Expense> getFilteredExpenses() {
        return filteredExpenses;
    }

    @Override
    public ObservableList<Income> getFilteredIncomes() {
        return filteredIncomes;
    }

    @Override
    public ObservableList<Entry> getFilteredExpensesAndIncomes() {
        return new FilteredList<>(filteredEntries, entry -> entry instanceof Expense || entry instanceof Income);
    }

    @Override
    public ObservableList<Wish> getFilteredWishes() {
        return filteredWishes;
    }

    @Override
    public ObservableList<Budget> getFilteredBudgets() {
        return filteredBudgets;
    }

    @Override
    public ObservableList<AutoExpense> getFilteredAutoExpenses() {
        return filteredAutoExpenses;
    }

    @Override

    public ObservableList<Reminder> getFilteredReminders() {
        return filteredReminders;
    }

    public ObservableList<Condition> getFilteredConditions() {
        return filteredConditions;
    }

    /**
     * return List of Entries matching condition.
     * @param predicate predicate to filter
     */
    public void updateFilteredEntryList(Predicate<Entry> predicate) {
        requireNonNull(predicate);
        filteredEntries.setPredicate(predicate);
    }

    @Override
    public void sortFilteredEntry(SortType c, SortSequence sequence) {
        sortedEntryList.setComparator(new EntryComparator(c, sequence));
    }

    @Override
    public void updateFilteredExpenses(Predicate<Expense> predicate) {
        requireNonNull(predicate);
        filteredExpenses.setPredicate(predicate);
    }

    @Override
    public void updateFilteredIncomes(Predicate<Income> predicate) {
        requireNonNull(predicate);
        filteredIncomes.setPredicate(predicate);
    }

    @Override
    public void updateFilteredWishes(Predicate<Entry> predicate) {
        requireNonNull(predicate);
        filteredWishes.setPredicate(predicate);
    }

    @Override
    public void updateFilteredBudgets(Predicate<Entry> predicate) {
        requireNonNull(predicate);
        filteredBudgets.setPredicate(predicate);
        for (Budget budget : filteredBudgets) {
            budget.setSpent(filteredExpenses);
            budget.updateSpent();
        }
    }

    @Override
    /**
     * return list of reminders matching this condition.
     * @param predicate condition to be matched.
     */
    public void updateFilteredAutoExpenses(Predicate<AutoExpense> predicate) {
        requireNonNull(predicate);
        filteredAutoExpenses.setPredicate(predicate);
    }

    @Override
    /**
     * return list of reminders matching this condition.
     * @param predicate condition to be matched.
     */
    public void updateFilteredReminders(Predicate<Reminder> predicate) {
        requireNonNull(predicate);
        filteredReminders.setPredicate(predicate);
    }

    // =========== Undo/Redo =============================================================

    @Override
    public boolean canUndoAddressBook() {
        return versionedAddressBook.canUndo();
    }

    @Override
    public boolean canRedoAddressBook() {
        return versionedAddressBook.canRedo();
    }

    @Override
    public void undoAddressBook() {
        versionedAddressBook.undo();
        versionedAddressBook.updateBudgets(filteredExpenses);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
    }

    @Override
    public void redoAddressBook() {
        versionedAddressBook.redo();
        versionedAddressBook.updateBudgets(filteredExpenses);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
    }

    @Override
    public void commitAddressBook() {
        versionedAddressBook.commit();
    }

    private void createExpensesfromAutoExpenses() {
        for (AutoExpense autoExpense : filteredAutoExpenses) {
            autoExpense.generateNewExpenses().stream().forEach(this::addExpense);
        }
    }

    // =========== TrackTime =============================================================

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
        return versionedAddressBook.equals(other.versionedAddressBook) && userPrefs.equals(other.userPrefs)
                && filteredEntries.equals(other.filteredEntries);
    }
}

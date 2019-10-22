package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import seedu.address.commons.util.InvalidationListenerManager;
import seedu.address.model.person.AutoExpense;
import seedu.address.model.person.AutoExpenseList;
import seedu.address.model.person.Budget;
import seedu.address.model.person.BudgetList;
import seedu.address.model.person.Entry;
import seedu.address.model.person.Expense;
import seedu.address.model.person.ExpenseList;
import seedu.address.model.person.ExpenseReminder;
import seedu.address.model.person.ExpenseReminderList;
import seedu.address.model.person.ExpenseTracker;
import seedu.address.model.person.ExpenseTrackerList;
import seedu.address.model.person.Income;
import seedu.address.model.person.IncomeList;
import seedu.address.model.person.UniqueEntryList;
import seedu.address.model.person.Wish;
import seedu.address.model.person.WishList;
import seedu.address.model.person.WishReminder;
import seedu.address.model.person.WishReminderList;

/**
 * Wraps all data at the address-book level Duplicates are not allowed (by
 * .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {
    private final UniqueEntryList entries;
    private final ExpenseList expenses;
    private final IncomeList incomes;
    private final BudgetList budgets;
    private final WishList wishes;
    private final AutoExpenseList autoExpenses;
    private final ExpenseReminderList expenseReminders;
    private final ExpenseTrackerList expenseTrackers;
    private final InvalidationListenerManager invalidationListenerManager = new InvalidationListenerManager();
    private final WishReminderList wishReminders;

    /*
     * The 'unusual' code block below is a non-static initialization block,
     * sometimes used to avoid duplication between constructors. See
     * https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other
     * ways to avoid duplication among constructors.
     */
    {
        entries = new UniqueEntryList();
        expenses = new ExpenseList();
        incomes = new IncomeList();
        budgets = new BudgetList();
        wishes = new WishList();
        autoExpenses = new AutoExpenseList();
        expenseReminders = new ExpenseReminderList();
        expenseTrackers = new ExpenseTrackerList();
        wishReminders = new WishReminderList();
    }
    public AddressBook() {
    }
    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }
    //// list overwrite operations
    /**
     * Replaces the contents of entries with {@code entry}. {@code entry} must not
     * contain duplicate entries.
     */
    public void setEntries(List<Entry> entries) {
        this.entries.setEntries(entries);
        indicateModified();
    }
    public void setExpenses(List<Expense> expenses) {
        this.expenses.setEntries(expenses);
        indicateModified();
    }
    public void setIncomes(List<Income> incomes) {
        this.incomes.setEntries(incomes);
        indicateModified();
    }
    public void setWishes(List<Wish> wishes) {
        this.wishes.setEntries(wishes);
        indicateModified();
    }
    public void setAutoExpenses(List<AutoExpense> autoExpenses) {
        this.autoExpenses.setEntries(autoExpenses);
        indicateModified();
    }
    public void setExpenseReminders(List<ExpenseReminder> expenseReminders) {
        this.expenseReminders.setEntries(expenseReminders);
        indicateModified();
    }
    public void setExpenseTrackers(List<ExpenseTracker> trackers) {
        this.expenseTrackers.setEntries(trackers);
        indicateModified();
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets.setEntries(budgets);
        indicateModified();
    }
    public void setWishReminders(List<WishReminder> wishReminders) {
        this.wishReminders.setEntries(wishReminders);
    }
    /**
     * updates the status of all reminders in ExpenseReminderList
     */
    public void updateExpenseReminders() {
        expenseReminders.updateList();
    }
    /**
     * When Wishes and WishReminders are read from list, new instances are created.
     * As such editing a wish after loading a file may result in WishReminder not updating wish accordingly.
     * This attempts to address that.
     */
    private void mapWishToReminders() {
        for (WishReminder reminder : wishReminders) {
            for (Wish wish : wishes) {
                if ((reminder.getWish()).equals(wish)) {
                    reminder.setWish(wish);
                }
            }
        }
    }
    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setEntries(newData.getEntryList());
        setExpenses(newData.getExpenseList());
        setIncomes(newData.getIncomeList());
        setWishes(newData.getWishList());
        setBudgets(newData.getBudgetList());
        setAutoExpenses(newData.getAutoExpenseList());
        setExpenseReminders(newData.getExpenseReminderList());
        setExpenseTrackers(newData.getExpenseTrackerList());
        setWishReminders(newData.getWishReminderList());
        mapWishToReminders();
    }
    //// person-level operations
    /**
     * Returns true if a entry with the same identity as {@code entry} exists in the
     * address book.
     */
    public boolean hasEntry(Entry entry) {
        requireNonNull(entry);
        return entries.contains(entry);
    }
    /**
     * Returns true if a reminder with the same identity as {@code reminder} exists
     * in the address book.
     */
    public boolean hasExpenseReminder(ExpenseReminder reminder) {
        requireNonNull(reminder);
        return expenseReminders.contains(reminder);
    }
    /**
     * Returns true if a reminder with the same identity as {@code reminder} exists in the address book.
     */
    public boolean hasWishReminder(WishReminder reminder) {
        requireNonNull(reminder);
        return wishReminders.contains(reminder);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addEntry(Entry entry) {
        entries.add(entry);
        indicateModified();
    }

    /**
     * Adds a specified Expense to the finance app.
     *
     * * @param expense the specified Expense to be added.
     */
    public void addExpense(Expense expense) {
        entries.add(expense);
        expenses.add(expense);
        indicateModified();
    }

    /**
     * Adds the specified Income to the finance app.
     *
     * @param income the specified Income to be added.
     */
    public void addIncome(Income income) {
        entries.add(income);
        incomes.add(income);
        indicateModified();
    }

    /**
     * Adds the specified Income to the finance app.
     * @param budget the specified Income to be added.
     */
    public void addBudget(Budget budget) {
        entries.add(budget);
        budgets.add(budget);
        indicateModified();
    }

    /**
     * Adds the specified Wish to the finance app.
     *
     * @param wish the specified Wish to be added.
     */
    public void addWish(Wish wish) {
        entries.add(wish);
        wishes.add(wish);
        indicateModified();
    }

    private void addExpenseTracker(ExpenseTracker tracker) {
        expenseTrackers.add(tracker);
        indicateModified();
    }

    /**
     * Adds the specified ExpenseTrackerReminder to the app.
     * @param expenseReminder the specified ExpenseTracker to be added.
     */
    public void addExpenseReminder(ExpenseReminder expenseReminder) {
        expenseReminders.add(expenseReminder);
        addExpenseTracker(expenseReminder.getTracker());
        indicateModified();
    }

    /**
     * Adds the specified ExpenseTrackerReminder to the app.
     * @param wishReminder the specified ExpenseTracker to be added.
     */
    public void addWishReminder(WishReminder wishReminder) {
        wishReminders.add(wishReminder);
    }
    /**
     * Adds the specified AutoExpense to the finance app.
     *
     * @param autoExpense the specified AutoExpense to be added.
     */
    public void addAutoExpense(AutoExpense autoExpense) {
        entries.add(autoExpense);
        autoExpenses.add(autoExpense);
        indicateModified();
    }

    /**
     * Replaces the given person {@code target} in the list with
     * {@code editedPerson}. {@code target} must exist in the address book. The
     * person identity of {@code editedPerson} must not be the same as another
     * existing person in the address book.
     */
    public void setEntry(Entry target, Entry editedEntry) {
        requireNonNull(editedEntry);
        entries.setEntry(target, editedEntry);
        indicateModified();
    }

    /**
     * Replaces the given Expense {@code target} in the list with
     * {@code editedEntry}. {@code target} must exist in the address book. The
     * expense identity of {@code editedEntry} must not be the same as another
     * existing expense in the address book.
     */
    public void setExpense(Expense target, Expense editedEntry) {
        requireNonNull(editedEntry);
        expenses.setExpense(target, editedEntry);
        entries.setEntry(target, editedEntry);
        indicateModified();
    }

    /**
     * Replaces the given Income {@code target} in the list with
     * {@code editedIncome}. {@code target} must exist in the address book. The
     * income identity of {@code editedEntry} must not be the same as another
     * existing income in the address book.
     */
    public void setIncome(Income target, Income editedEntry) {
        requireNonNull(editedEntry);
        incomes.setIncome(target, editedEntry);
        entries.setEntry(target, editedEntry);
        indicateModified();
    }

    /**
     * Replaces the given Income {@code target} in the list with
     * {@code editedIncome}. {@code target} must exist in the address book. The
     * income identity of {@code editedEntry} must not be the same as another
     * existing income in the address book.
     */
    public void setWish(Wish target, Wish editedEntry) {
        requireNonNull(editedEntry);
        wishes.setWish(target, editedEntry);
        entries.setEntry(target, editedEntry);
        indicateModified();
    }

    private void setExpenseTracker(ExpenseTracker target, ExpenseTracker editedEntry) {
        requireNonNull(editedEntry);
        expenseTrackers.setTracker(target, editedEntry);
        indicateModified();
    }

    /**
     * Replaces the given ExpenseTracker {@code target} in the list with {@code editedTracker}.
     * {@code target} must exist in the address book.
     * The ExpenseTracer identity of {@code editedTracker}
     * must not be the same as another existing ExpenseTracker in the address book.
     */
    public void setExpenseReminder(ExpenseReminder target, ExpenseReminder editedEntry) {
        requireNonNull(editedEntry);
        expenseReminders.setExpenseReminder(target, editedEntry);
        setExpenseTracker(target.getTracker(), editedEntry.getTracker());
        indicateModified();
    }

    /**
     * Replaces the given Budget {@code target} in the list with {@code editedBudget}.
     * {@code target} must exist in the finance tracker.
     * The budget identity of {@code editedEntry} must not be the same as another existing budget
     * in the finance tracker.
     */
    public void setBudget(Budget target, Budget editedEntry) {
        requireNonNull(editedEntry);
        budgets.setBudget(target, editedEntry);
        indicateModified();
    }

    /**
     * Replaces the given ExpenseTracker {@code target} in the list with {@code editedTracker}.
     * {@code target} must exist in the address book.
     * The ExpenseTracer identity of {@code editedTracker}
     * must not be the same as another existing ExpenseTracker in the address book.
     */
    public void setWishReminder(WishReminder target, WishReminder editedEntry) {
        requireNonNull(editedEntry);
        wishReminders.setWishReminder(target, editedEntry);
    }



    /**
     * Replaces the given AutoExpense {@code target} in the list with
     * {@code editedIncome}. {@code target} must exist in the address book. The
     * income identity of {@code editedEntry} must not be the same as another
     * existing income in the address book.
     */
    public void setAutoExpense(AutoExpense target, AutoExpense editedEntry) {
        requireNonNull(editedEntry);
        autoExpenses.setAutoExpense(target, editedEntry);
        entries.setEntry(target, editedEntry);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeEntry(Entry key) {
        entries.remove(key);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code expenses}. {@code key} must exist in the
     * address book.
     */
    public void removeExpense(Expense key) {
        expenses.remove(key);
        entries.remove(key);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code incomes}. {@code key} must exist in the
     * address book.
     */
    public void removeIncome(Income key) {
        incomes.remove(key);
        entries.remove(key);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code wishes}. {@code key} must exist in the
     * address book.
     */
    public void removeWish(Wish key) {
        wishes.remove(key);
        entries.remove(key);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code budgets}.
     * {@code key} must exist in the address book.
     */
    public void removeBudget(Budget key) {
        budgets.remove(key);
        entries.remove(key);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code autoExpenses}. {@code key} must exist in
     * the address book.
     */
    public void removeAutoExpense(AutoExpense key) {
        autoExpenses.remove(key);
        entries.remove(key);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code expensetracker}.
     * {@code key} must exist in the address book.
     */

    private void removeExpenseTracker(ExpenseTracker key) {
        expenseTrackers.remove(key);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code wishes}.
     * {@code key} must exist in the address book.
     */
    public void removeExpenseReminder(ExpenseReminder key) {
        expenseReminders.remove(key);
        removeExpenseTracker(key.getTracker());
        indicateModified();
    }
    public void removeWishReminder(WishReminder key) {
        wishReminders.remove(key);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListenerManager.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListenerManager.removeListener(listener);
    }

    /**
     * Notifies listeners that the finance tracker has been modified.
     */
    protected void indicateModified() {
        invalidationListenerManager.callListeners(this);
    }

    //// util methods
    @Override
    public String toString() {
        return entries.asUnmodifiableObservableList().size() + " persons";
        // TODO: refine later
    }

    @Override
    public ObservableList<Entry> getEntryList() {
        return entries.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Expense> getExpenseList() {
        return expenses.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Income> getIncomeList() {
        return incomes.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Wish> getWishList() {
        return wishes.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Budget> getBudgetList() {
        return budgets.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<AutoExpense> getAutoExpenseList() {
        return autoExpenses.asUnmodifiableObservableList();
    }

    public ObservableList<ExpenseReminder> getExpenseReminderList() {
        return expenseReminders.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<ExpenseTracker> getExpenseTrackerList() {
        return expenseTrackers.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<WishReminder> getWishReminderList() {
        return wishReminders.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                        && entries.equals(((AddressBook) other).entries));
    }

    @Override
    public int hashCode() {
        return entries.hashCode();
    }
}

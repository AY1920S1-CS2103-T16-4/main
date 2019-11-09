package seedu.guilttrip.model.statistics;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import seedu.guilttrip.commons.core.LogsCenter;
import seedu.guilttrip.model.entry.Category;
import seedu.guilttrip.model.entry.CategoryList;
import seedu.guilttrip.model.entry.Date;
import seedu.guilttrip.model.entry.Expense;
import seedu.guilttrip.model.entry.Income;

/**
 * Handles calculation of statistics.
 */
public class StatisticsManager implements Statistics {
    private ObservableMap<Integer, ObservableMap<Integer, MonthList>> yearlyRecord;
    private ObservableList<CategoryStatistics> listOfStatsForExpense;
    private ObservableList<CategoryStatistics> listOfStatsForIncome;
    private ObservableList<DailyStatistics> listOfStatsForDaily;
    private FilteredList<Expense> modelTotalFilteredExpenses;
    private FilteredList<Income> modelTotalFilteredIncomes;
    private CategoryList listOfCategories;
    private DoubleProperty totalExpenseForPeriod;
    private DoubleProperty totalIncomeForPeriod;

    /**
     * Manages the general Statistics.
     */
    public StatisticsManager(FilteredList<Expense> listOfFilteredExpenses, FilteredList<Income> listOfFilteredIncomes,
                             CategoryList listOfCategories) {
        this.modelTotalFilteredExpenses = new FilteredList<Expense>(listOfFilteredExpenses);
        this.modelTotalFilteredIncomes = new FilteredList<Income>(listOfFilteredIncomes);
        listOfCategories.getInternalListForOtherEntries().addListener(new ListChangeListener<Category>() {
            @Override
            public void onChanged(Change<? extends Category> change) {
                updateCategory();
                updateListOfStats();
            }
        });
        listOfCategories.getInternalListForIncome().addListener(new ListChangeListener<Category>() {
            @Override
            public void onChanged(Change<? extends Category> change) {
                updateCategory();
                updateListOfStats();
            }
        });
        this.listOfCategories = listOfCategories;
        int currentYear = LocalDate.now().getYear();
        yearlyRecord = FXCollections.observableHashMap();
        listOfStatsForExpense = FXCollections.observableArrayList();
        listOfStatsForIncome = FXCollections.observableArrayList();
        listOfStatsForDaily = FXCollections.observableArrayList();
        totalExpenseForPeriod = new SimpleDoubleProperty(0.00);
        totalIncomeForPeriod = new SimpleDoubleProperty(0.00);
        initRecords(currentYear);
        initStats();
    }

    /**
     * Loads the Records from scratch. Only creates records for the currentYear to increase speed for startup.
     */
    private void initRecords(int currentYear) {
        ObservableMap<Integer, MonthList> monthlyRecord = FXCollections.observableHashMap();
        for (int i = 1; i <= 12; i++) {
            FilteredList<Expense> filteredExpenseByMonth = modelTotalFilteredExpenses;
            FilteredList<Income> filteredIncomeByMonth = modelTotalFilteredIncomes;
            MonthList monthToCompare = new MonthList(listOfCategories, filteredExpenseByMonth, filteredIncomeByMonth,
                    Month.of(i), currentYear);
            monthlyRecord.put(i, monthToCompare);
        }
        yearlyRecord.put(currentYear, monthlyRecord);
    }

    /**
     * Updates the list of Category to be displayed in Stats if the original list of Categories change.
     */
    private void updateCategory() {
        listOfStatsForExpense.clear();
        listOfStatsForIncome.clear();
        listOfCategories.getInternalListForOtherEntries().stream().forEach(t -> listOfStatsForExpense
                .add(new CategoryStatistics(t, 0.00)));
        listOfCategories.getInternalListForIncome().stream().forEach(t -> listOfStatsForIncome
                .add(new CategoryStatistics(t, 0.00)));
    }

    /**
     * Obtains the properties of this classm .
     */
    @Override
    public ObservableList<DailyStatistics> getListOfStatsForBarChart() {
        return listOfStatsForDaily;
    }

    @Override
    public ObservableList<CategoryStatistics> getListOfStatsForExpense() {
        return listOfStatsForExpense;
    }

    @Override
    public ObservableList<CategoryStatistics> getListOfStatsForIncome() {
        return listOfStatsForIncome;
    }

    @Override
    public DoubleProperty getTotalExpenseForPeriod() {
        return totalExpenseForPeriod;
    }

    @Override
    public DoubleProperty getTotalIncomeForPeriod() {
        return totalIncomeForPeriod;
    }

    /**
     * Initiates the Statistics for the listsOfCategories, and adds Listeners for the relevant ObservableLists.
     */
    private void initStats() {
        listOfCategories.getInternalListForOtherEntries().stream().forEach(t -> listOfStatsForExpense
                .add(new CategoryStatistics(t, 0.00)));
        listOfCategories.getInternalListForIncome().stream().forEach(t -> listOfStatsForIncome
                .add(new CategoryStatistics(t, 0.00)));
        modelTotalFilteredExpenses.addListener(new ListChangeListener<Expense>() {
            @Override
            public void onChanged(Change<? extends Expense> change) {
                while (change.next()) {
                    List<? extends Expense> changed = change.getAddedSubList();
                    if (changed.size() == 1) {
                        Expense changedExpense = changed.get(0);
                        updateBarCharts(changedExpense.getDate());
                        updateListOfStats(Arrays.asList(changedExpense.getDate()));
                    }
                }
            }
        });
        modelTotalFilteredIncomes.addListener(new ListChangeListener<Income>() {
            @Override
            public void onChanged(Change<? extends Income> change) {
                while (change.next()) {
                    List<? extends Income> changed = change.getAddedSubList();
                    if (changed.size() == 1) {
                        Income changedIncome = changed.get(0);
                        updateBarCharts(changedIncome.getDate());
                        updateListOfStats(Arrays.asList(changedIncome.getDate()));
                    }
                }
            }
        });
        updateListOfStats();
        updateBarCharts();
    }

    /**
     * Calculates the statistics for the current month.
     */
    @Override
    public void updateListOfStats() {
        ObservableMap<Integer, MonthList> yearOfRecord = yearlyRecord.get(LocalDate.now().getYear());
        ArrayList<MonthList> listOfMonths = new ArrayList<MonthList>();
        listOfMonths.add(yearOfRecord.get(LocalDate.now().getMonth().getValue()));
        double totalExpense = countStats(listOfMonths, listOfStatsForExpense);
        this.totalExpenseForPeriod.setValue(totalExpense);
        double totalIncome = countStats(listOfMonths, listOfStatsForIncome);
        this.totalIncomeForPeriod.setValue(totalIncome);
    }

    /**
     * Calculates the statistics for the specified month or the range of periods given. This statistics is to be used
     * later in the table or piechart.
     *
     * @param listOfPeriods contains the range of periods that was specified by the user.
     */
    @Override
    public void updateListOfStats(List<Date> listOfPeriods) {
        ArrayList<MonthList> listOfMonths;
        if (listOfPeriods.size() == 1) {
            listOfMonths = getMonth(listOfPeriods.get(0));
        } else {
            listOfMonths = getListOfMonths(listOfPeriods);
        }
        double totalExpense = countStats(listOfMonths, listOfStatsForExpense);
        this.totalExpenseForPeriod.setValue(totalExpense);
        double totalIncome = countStats(listOfMonths, listOfStatsForIncome);
        this.totalIncomeForPeriod.setValue(totalIncome);
    }

    /**
     * Recalculates statistics for one month only.
     *
     * @param monthToCalculate the date which contains the format of what is needed to calculate.
     */
    private ArrayList<MonthList> getMonth(Date monthToCalculate) {
        int yearToCheck = monthToCalculate.getDate().getYear();
        if (!yearlyRecord.containsKey(yearToCheck)) {
            initRecords(yearToCheck);
        }
        ArrayList<MonthList> listOfMonths = new ArrayList<MonthList>();
        Month month = monthToCalculate.getDate().getMonth();
        MonthList startMonthListToCalculate = yearlyRecord.get(yearToCheck).get(month.getValue());
        listOfMonths.add(startMonthListToCalculate);
        return listOfMonths;
    }

    /**
     * Recalculates statistics for a range of periods if the user specified a huge range of periods.
     *
     * @param rangeOfDates a list of size 2 that contains the range of dates that needs to be calculated.
     */
    private ArrayList<MonthList> getListOfMonths(List<Date> rangeOfDates) {
        Date startDate = rangeOfDates.get(0);
        Date endDate = rangeOfDates.get(1);
        int startYear = startDate.getDate().getYear();
        int endYear = endDate.getDate().getYear();
        ArrayList<MonthList> listOfMonths = new ArrayList<>();
        for (int i = startYear; i <= endYear; i++) {
            if (!yearlyRecord.containsKey(i)) {
                initRecords(i);
            }
            if (i == endYear) {
                ArrayList<MonthList> listOfMonthsRetrieved = retrieveSpecifiedMonths(
                        startDate.getDate().getMonth().getValue(),
                        endDate.getDate().getMonth().getValue(), i);
                listOfMonths.addAll(listOfMonthsRetrieved);
            } else {
                ArrayList<MonthList> listOfMonthsRetrieved = retrieveSpecifiedMonths(1, 12, i);
                listOfMonths.addAll(listOfMonthsRetrieved);
            }
        }
        assert(listOfMonths.size() != 0);
        return listOfMonths;
    }

    /**
     * Recalculates statistics for the months in a year. Helper method to SLAP.
     *
     * @param startMonth the Month which needs to start calculating from.
     * @param endMonth the Month which needs to end calculating from.
     * @param year the year in which the calculation is taking place.
     * @return listOfMonths the particular monthlist that needs to be calculated.
     */
    private ArrayList<MonthList> retrieveSpecifiedMonths(int startMonth, int endMonth, int year) {
        ArrayList<MonthList> listOfMonths = new ArrayList<MonthList>();
        for (int i = startMonth; i <= endMonth; i++) {
            MonthList monthListToCalculate = yearlyRecord.get(year).get(i);
            listOfMonths.add(monthListToCalculate);
        }
        return listOfMonths;
    }


    /**
     * Recalculates statistics for a specific period of time.
     *
     * @param currentMonthList the lists of months that needs to be recalculated.
     * @param typeOfCategory the list of categories that need to be recalculated whether it be income or expense.
     */
    private double countStats(ArrayList<MonthList> currentMonthList,
                              ObservableList<CategoryStatistics> typeOfCategory) {
        double totalAmountCalculated = 0.00;
        for (int i = 0; i < typeOfCategory.size(); i++) {
            Category toVerifyCat = typeOfCategory.get(i).getCategory();
            double totalForCategoryOverMonths = calculateTotalAmountForCategory(currentMonthList, toVerifyCat);
            CategoryStatistics toCheck = new CategoryStatistics(toVerifyCat, totalForCategoryOverMonths);
            totalAmountCalculated = toCheck.getAmountCalculated() + totalAmountCalculated;
            if (!typeOfCategory.get(i).equals(toCheck)) {
                typeOfCategory.set(i, toCheck);
            }
        }
        return totalAmountCalculated;
    }

    /**
     * Recalculates statistics for a Category for all the months in the period. Helper method to SLAP.
     *
     * @param currentMonthList the lists of months that needs to be recalculated.
     * @param category the category that need to be recalculated whether it be income or expense.
     */
    private double calculateTotalAmountForCategory(ArrayList<MonthList> currentMonthList, Category category) {
        double totalAmountForTotalMonths = 0.00;
        for (int k = 0; k < currentMonthList.size(); k++) {
            MonthList monthToCalculate = currentMonthList.get(k);
            totalAmountForTotalMonths = totalAmountForTotalMonths + monthToCalculate.updateListOfStats(category);
        }
        return totalAmountForTotalMonths;
    }


    /**
     * Calculates the statistics for the current month. This statistics is to be used later in the barchart.
     */
    @Override
    public void updateBarCharts() {
        ObservableMap<Integer, MonthList> yearOfRecord = yearlyRecord.get(LocalDate.now().getYear());
        MonthList monthListToCalculate = yearOfRecord.get(LocalDate.now().getMonth().getValue());
        this.listOfStatsForDaily.clear();
        this.listOfStatsForDaily.addAll(monthListToCalculate.calculateStatisticsForBarChart());
        if (this.listOfStatsForDaily.isEmpty()) {
            this.listOfStatsForDaily.add(new DailyStatistics(LocalDate.now(), 0.00, 0.00));
        }
    }

    /**
     * Calculates the statistics for the specified month or the range of periods given. This statistics is to be used
     * later in the barchart.
     *
     * @param monthToShow contains the month that was specified by the user.
     */
    @Override
    public void updateBarCharts(Date monthToShow) {
        ObservableMap<Integer, MonthList> yearOfRecord = yearlyRecord.get(monthToShow.getDate().getYear());
        MonthList monthListToCalculate = yearOfRecord.get(monthToShow.getDate().getMonth().getValue());
        this.listOfStatsForDaily.clear();
        ObservableList<DailyStatistics> dailyStatsToBeDisplayed = monthListToCalculate.calculateStatisticsForBarChart();
        this.listOfStatsForDaily.addAll(dailyStatsToBeDisplayed);
        if (this.listOfStatsForDaily.isEmpty()) {
            this.listOfStatsForDaily.add(new DailyStatistics(monthToShow.getDate(), 0.00, 0.00));
        }
    }
}

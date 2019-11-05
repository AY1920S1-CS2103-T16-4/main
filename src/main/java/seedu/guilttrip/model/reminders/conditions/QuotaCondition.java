package seedu.guilttrip.model.reminders.conditions;

import java.util.function.Predicate;

import seedu.guilttrip.model.entry.Entry;
/**
 * Condition is met when entry amount is greater or equal to specified quota.
 */
public class QuotaCondition extends Condition {
    private double quota;
    private Predicate<Entry> quotaPredicate = new Predicate<>() {
        @Override
        public boolean test(Entry entry) {
            return entry.getAmount().value >= quota;
        }
    };
    public QuotaCondition(Double quota) {
        super("Quota Condition");
        this.quota = quota;
        super.setPred(quotaPredicate);
    }
    public double getQuota() {
        return quota;
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof QuotaCondition)) {
            return false;
        } else {
            return this.quota == (((QuotaCondition) other).quota);
        }
    }
}

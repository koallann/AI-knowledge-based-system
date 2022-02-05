package inference;

import domain.Rule;

public final class RuleProcessing {

    public enum Status {
        PENDING, IMPLICATED, NOT_IMPLICATED;
    }

    final Rule rule;
    Status status;

    public RuleProcessing(Rule rule) {
        this.rule = rule;
        this.status = Status.PENDING;
    }

}

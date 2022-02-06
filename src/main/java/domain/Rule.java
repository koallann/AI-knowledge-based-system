package domain;

import java.util.List;

public final class Rule {

    public enum Status {
        PENDING, IMPLICATED, NOT_IMPLICATED
    }

    public final List<Variable> conditions;
    public final List<Variable> implications;
    public Status status;

    public Rule(List<Variable> conditions, List<Variable> implications) {
        this.conditions = conditions;
        this.implications = implications;
        this.status = Status.PENDING;
    }

    @Override
    public String toString() {
        return String.format(
            "Rule { IF %s THEN %s }",
            Variable.collectionString(conditions, " and "),
            Variable.collectionString(implications, " and ")
        );
    }

}

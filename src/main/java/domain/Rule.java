package domain;

import java.util.Iterator;
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
        StringBuilder builder = new StringBuilder("Rule { IF");

        Iterator<Variable> iterator1 = conditions.iterator();
        while (iterator1.hasNext()) {
            Variable c = iterator1.next();
            builder.append(String.format(" %s=%s", c.key, c.value));
            if (iterator1.hasNext()) builder.append(" and");
        }

        builder.append(" THEN");

        Iterator<Variable> iterator2 = implications.iterator();
        while (iterator2.hasNext()) {
            Variable i = iterator2.next();
            builder.append(String.format(" %s=%s", i.key, i.value));
            if (iterator2.hasNext()) builder.append(" and");
        }

        return builder.append(" }").toString();
    }

}

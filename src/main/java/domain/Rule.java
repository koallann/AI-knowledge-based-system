package domain;

import java.util.List;

public final class Rule {

    public enum Status {
        PENDING, IMPLICATED, NOT_IMPLICATED
    }

    public final List<Sentence> conditions;
    public final List<Sentence> implications;
    public Status status;

    public Rule(List<Sentence> conditions, List<Sentence> implications) {
        this.conditions = conditions;
        this.implications = implications;
        this.status = Status.PENDING;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Rule{IF ");

        conditions.forEach(c -> builder.append(String.format("%s=%s and ", c.key, c.value)));
        builder.delete(builder.length() - 4, builder.length());

        builder.append("THEN ");

        implications.forEach(i -> builder.append(String.format("%s=%s and ", i.key, i.value)));
        builder.delete(builder.length() - 5, builder.length());

        builder.append("}");
        return builder.toString();
    }

}

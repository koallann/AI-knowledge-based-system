package domain;

import java.util.Set;

public final class Rule {

    public final Set<Sentence> conditions;
    public final Set<Sentence> implications;

    public Rule(Set<Sentence> conditions, Set<Sentence> implications) {
        this.conditions = conditions;
        this.implications = implications;
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

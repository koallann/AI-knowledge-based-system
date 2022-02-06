package domain;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public final class Variable {

    public final String key;
    public final String value;

    public Variable(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Variable { %s=%s }", key, value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return key.equals(variable.key) && value.equals(variable.value);
    }

    public static String collectionString(Collection<Variable> variables, String separator) {
        StringBuilder builder = new StringBuilder();

        Iterator<Variable> iterator = variables.iterator();
        while (iterator.hasNext()) {
            Variable match = iterator.next();
            builder.append(String.format("%s=%s", match.key, match.value));
            if (iterator.hasNext()) builder.append(separator);
        }

        return builder.toString();
    }

}

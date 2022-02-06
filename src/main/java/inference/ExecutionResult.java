package inference;

import domain.Variable;

import java.util.ArrayList;
import java.util.List;

public final class ExecutionResult {

    public final List<Variable> goalMatches;

    public ExecutionResult() {
        this.goalMatches = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("ExecutionResult{");

        goalMatches.forEach(m -> builder.append(String.format("%s=%s, ", m.key, m.value)));
        builder.delete(builder.length() - 2, builder.length());
        builder.append("}");

        return builder.toString();
    }

}

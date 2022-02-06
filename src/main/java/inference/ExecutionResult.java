package inference;

import domain.Variable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ExecutionResult {

    public final List<Variable> goalMatches;

    public ExecutionResult() {
        this.goalMatches = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("ExecutionResult { ");

        Iterator<Variable> iterator = goalMatches.iterator();
        while (iterator.hasNext()) {
            Variable match = iterator.next();
            builder.append(String.format("%s=%s", match.key, match.value));
            if (iterator.hasNext()) builder.append(", ");
        }

        return builder.append(" }").toString();
    }

}

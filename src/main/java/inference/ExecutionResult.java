package inference;

import domain.Variable;

import java.util.ArrayList;
import java.util.List;

public final class ExecutionResult {

    public final List<Variable> allMatches;
    public final List<Variable> goalMatches;

    public ExecutionResult() {
        this.allMatches = new ArrayList<>();
        this.goalMatches = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format(
            "ExecutionResult {\n    All Matches:  %s\n    Goal Matches: %s\n}",
            Variable.collectionString(allMatches, ", "),
            Variable.collectionString(goalMatches, ", ")
        );
    }

}

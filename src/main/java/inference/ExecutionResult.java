package inference;

import domain.Sentence;

import java.util.ArrayList;
import java.util.List;

public final class ExecutionResult {

    public final List<Sentence> goalMatches;

    public ExecutionResult() {
        this.goalMatches = new ArrayList<>();
    }

}

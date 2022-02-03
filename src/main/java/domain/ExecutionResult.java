package domain;

import java.util.ArrayList;
import java.util.List;

public final class ExecutionResult {

    final List<Sentence> goalMatches;

    public ExecutionResult() {
        this.goalMatches = new ArrayList<>();
    }

}

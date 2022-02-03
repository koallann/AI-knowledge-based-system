package domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ExecutionParams {

    final List<Rule> rules;
    final Set<String> goals;

    public ExecutionParams() {
        this.rules = new ArrayList<>();
        this.goals = new HashSet<>();
    }

}

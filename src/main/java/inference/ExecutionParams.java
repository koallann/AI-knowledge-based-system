package inference;

import domain.OnRequestUserVariable;
import domain.Rule;

import java.util.List;
import java.util.Set;

public final class ExecutionParams {

    public final List<Rule> rules;
    public final Set<String> goals;
    public final OnRequestUserVariable onRequestUserVariable;

    public ExecutionParams(List<Rule> rules, Set<String> goals, OnRequestUserVariable onRequestUserVariable) {
        this.rules = rules;
        this.goals = goals;
        this.onRequestUserVariable = onRequestUserVariable;
    }

}

package inference;

import domain.OnRequestUserVariable;
import domain.Rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ExecutionParams {

    public final List<Rule> rules;
    public final Set<String> goals;
    public final OnRequestUserVariable onRequestUserVariable;

    public ExecutionParams(OnRequestUserVariable onRequestUserVariable) {
        this.rules = new ArrayList<>();
        this.goals = new HashSet<>();
        this.onRequestUserVariable = onRequestUserVariable;
    }

}

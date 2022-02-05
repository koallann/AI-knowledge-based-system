package domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ExecutionParams {

    public final List<Rule> rules;
    public final Set<String> goals;
    public final OnRequestUserInput onRequestUserInput;

    public ExecutionParams(OnRequestUserInput onRequestUserInput) {
        this.rules = new ArrayList<>();
        this.goals = new HashSet<>();
        this.onRequestUserInput = onRequestUserInput;
    }

}

package inference;

import domain.OnRequestUserVariable;
import domain.Rule;
import domain.Variable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static domain.Rule.Status.*;

public final class InferenceEngine {

    public ExecutionResult run(ExecutionParams params) {
        List<Rule> rules = params.rules;
        Set<Variable> workingMemory = new HashSet<>();
        ExecutionResult result = new ExecutionResult();

        if (rules.isEmpty()) return result;

        // start with the first rule
        backwardChaining(rules.get(0), rules, workingMemory, params.onRequestUserVariable);

        // set result
        workingMemory.forEach(s -> {
            if (params.goals.contains(s.key))
                result.goalMatches.add(s);
        });

        return result;
    }

    private void backwardChaining(
        Rule goal,
        List<Rule> rules,
        Set<Variable> workingMemory,
        OnRequestUserVariable onRequestUserVariable
    ) {
        // ignore rule already evaluated
        if (goal.status == IMPLICATED || goal.status == NOT_IMPLICATED) return;

        // we need to evaluate all the goal conditions
        for (Variable condition : goal.conditions) {
            // first check if condition is already on working memory
            Optional<Variable> conditionInWM = findInWorkingMemory(condition, workingMemory);

            if (conditionInWM.isPresent()) {
                if (conditionInWM.get().equals(condition))
                    continue; // condition resolved, lets to the next condition
                else
                    break; // can't imply the goal, don't proceed
            }

            // search a rule to the condition
            Rule match = canBeImplicated(condition, rules);

            if (match == null) {
                // condition can't be implicated by any rule, so request the condition value from the user
                String value = onRequestUserVariable.requestValue(condition.key);

                if (value.equals(condition.value)) {
                    workingMemory.add(condition);
                } else {
                    Variable actual = new Variable(condition.key, value);
                    workingMemory.add(actual);
                }
                // try to generate new implications
                forwardChaining(rules, workingMemory);

                // don't proceed if condition not satisfied
                if (!value.equals(condition.value)) break;
            } else {
                boolean matchResolved = false;

                while (match != null) {
                    // recursive call breaking the condition into a sub-problem
                    backwardChaining(match, rules, workingMemory, onRequestUserVariable);

                    // condition resolved, lets to the next condition
                    if (match.status == IMPLICATED) {
                        matchResolved = true;
                        break;
                    }

                    // try with another rule
                    match = canBeImplicated(condition, rules);
                }

                // don't proceed if condition not satisfied
                if (!matchResolved) break;
            }
        }

        // then evaluate the goal
        if (workingMemory.containsAll(goal.conditions)) {
            workingMemory.addAll(goal.implications);
            goal.status = IMPLICATED;
        } else {
            goal.status = NOT_IMPLICATED;
        }
    }

    private Rule canBeImplicated(Variable variable, List<Rule> rules) {
        for (Rule r : rules) {
            if (r.status == PENDING && r.implications.contains(variable))
                return r;
        }
        return null;
    }

    private Optional<Variable> findInWorkingMemory(Variable variable, Set<Variable> workingMemory) {
        return workingMemory.stream()
            .filter(c -> c.key.equals(variable.key))
            .findFirst();
    }

    private void forwardChaining(List<Rule> rules, Set<Variable> workingMemory) {
        for (Rule r : rules) {
            if (r.status != PENDING) continue;

            if (workingMemory.containsAll(r.conditions)) {
                workingMemory.addAll(r.implications);
                r.status = IMPLICATED;
            }
        }
    }

}

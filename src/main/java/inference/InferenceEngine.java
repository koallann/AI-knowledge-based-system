package inference;

import domain.OnRequestUserVariable;
import domain.Rule;
import domain.Variable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static domain.Rule.Status.*;

public final class InferenceEngine {

    public ExecutionResult run(ExecutionParams params) {
        Set<Variable> workingMemory = new HashSet<>();
        ExecutionResult result = new ExecutionResult();

        if (params.rules.isEmpty()) return result;

        // run chaining for each rule that's not resolved yet

        for (Rule rule : params.rules) {
            if (rule.status == PENDING)
                backwardChaining(rule, params.rules, workingMemory, params.onRequestUserVariable);
        }

        // set result

        result.allMatches.addAll(workingMemory);

        result.goalMatches.addAll(
            workingMemory.stream()
                .filter(v -> params.goals.contains(v.key))
                .collect(Collectors.toList())
        );

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
            Variable conditionInWM = findVariable(condition.key, workingMemory);

            if (conditionInWM != null) {
                if (conditionInWM.equals(condition))
                    continue; // condition resolved, lets to the next condition
                else
                    break; // can't imply the goal, don't proceed
            }

            // find a rule that implies the condition
            Rule match = findRule(condition, rules);

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

                // don't proceed if condition not resolved
                if (!workingMemory.contains(condition)) break;
            } else {
                boolean matchResolved = false;

                while (match != null) {
                    // recursive call breaking the condition into a sub-problem
                    backwardChaining(match, rules, workingMemory, onRequestUserVariable);

                    // condition resolved, lets to the next
                    if (match.status == IMPLICATED) {
                        matchResolved = true;
                        break;
                    }

                    // not resolved? try with another rule
                    match = findRule(condition, rules);
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

    private Rule findRule(Variable variable, List<Rule> rules) {
        for (Rule r : rules) {
            if (r.status == PENDING
                && r.implications.contains(variable)) return r;
        }
        return null;
    }

    private Variable findVariable(String key, Set<Variable> workingMemory) {
        for (Variable v : workingMemory) {
            if (v.key.equals(key)) return v;
        }
        return null;
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

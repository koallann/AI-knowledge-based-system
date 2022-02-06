package inference;

import domain.OnRequestUserInput;
import domain.Rule;
import domain.Sentence;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static domain.Rule.Status.*;

public final class InferenceEngine {

    public ExecutionResult run(ExecutionParams params) {
        List<Rule> rules = params.rules;
        Set<Sentence> workingMemory = new HashSet<>();
        ExecutionResult result = new ExecutionResult();

        if (rules.isEmpty()) return result;

        // start with the first rule
        backwardChaining(rules.get(0), rules, workingMemory, params.onRequestUserInput);

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
        Set<Sentence> workingMemory,
        OnRequestUserInput onRequestUserInput
    ) {
        // ignore rule already evaluated
        if (goal.status == IMPLICATED || goal.status == NOT_IMPLICATED) return;

        // we need to evaluate all the goal conditions
        for (Sentence condition : goal.conditions) {
            // first check if condition is already on working memory
            Optional<Sentence> conditionInWM = findInWorkingMemory(condition, workingMemory);

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
                String value = onRequestUserInput.requestValue(condition);

                if (value.equals(condition.value)) {
                    workingMemory.add(condition);
                } else {
                    Sentence actual = new Sentence(condition.key, value);
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
                    backwardChaining(match, rules, workingMemory, onRequestUserInput);

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

    private Rule canBeImplicated(Sentence sentence, List<Rule> rules) {
        for (Rule r : rules) {
            if (r.status == PENDING && r.implications.contains(sentence))
                return r;
        }
        return null;
    }

    private Optional<Sentence> findInWorkingMemory(Sentence sentence, Set<Sentence> workingMemory) {
        return workingMemory.stream()
            .filter(c -> c.key.equals(sentence.key))
            .findFirst();
    }

    private void forwardChaining(List<Rule> rules, Set<Sentence> workingMemory) {
        for (Rule r : rules) {
            if (r.status != PENDING) continue;

            if (workingMemory.containsAll(r.conditions)) {
                workingMemory.addAll(r.implications);
                r.status = IMPLICATED;
            }
        }
    }

}

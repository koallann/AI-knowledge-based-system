package inference;

import domain.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static inference.RuleProcessing.Status.*;

public final class InferenceEngine {

    public ExecutionResult run(ExecutionParams params) {
        List<RuleProcessing> processing = buildProcessingList(params.rules);
        Set<Sentence> workingMemory = new HashSet<>();
        ExecutionResult result = new ExecutionResult();

        processing.forEach(p -> {
            if (p.status == PENDING)
                backwardChaining(p, processing, workingMemory, params.onRequestUserInput);
        });

        workingMemory.forEach(s -> {
            if (params.goals.contains(s.key))
                result.goalMatches.add(s);
        });

        return result;
    }

    private List<RuleProcessing> buildProcessingList(List<Rule> rules) {
        return rules.stream()
            .map(RuleProcessing::new)
            .collect(Collectors.toList());
    }

    private void backwardChaining(
        RuleProcessing goal,
        List<RuleProcessing> processing,
        Set<Sentence> workingMemory,
        OnRequestUserInput onRequestUserInput
    ) {
        // ignore rule already evaluated
        if (goal.status == IMPLICATED || goal.status == NOT_IMPLICATED) return;

        // we need to evaluate all the goal conditions
        for (Sentence condition : goal.rule.conditions) {
            // first check if condition is already on working memory
            Optional<Sentence> conditionInWM = findInWorkingMemory(condition, workingMemory);

            if (conditionInWM.isPresent()) {
                if (conditionInWM.get().equals(condition))
                    continue; // condition resolved, lets to the next condition
                else
                    break; // can't imply the goal, don't proceed
            }

            // search a rule to the condition
            RuleProcessing match = canBeImplicated(condition, processing);

            if (match == null) {
                // condition can't be implicated by any rule, so request the condition value from the user
                String value = onRequestUserInput.requestValue(condition);

                if (value.equals(condition.value)) {
                    workingMemory.add(condition);
                } else {
                    Sentence actual = new Sentence(condition.key, value);
                    workingMemory.add(actual);
                }
            } else {
                while (match != null) {
                    // recursive call breaking the condition into a sub-problem
                    backwardChaining(match, processing, workingMemory, onRequestUserInput);

                    // condition resolved, lets to the next condition
                    if (match.status == IMPLICATED) break;

                    // try with another rule
                    match = canBeImplicated(condition, processing);
                }
            }
        }

        // then evaluate the goal
        if (workingMemory.containsAll(goal.rule.conditions)) {
            workingMemory.addAll(goal.rule.implications);
            goal.status = IMPLICATED;
        } else {
            goal.status = NOT_IMPLICATED;
        }
    }

    private RuleProcessing canBeImplicated(Sentence sentence, List<RuleProcessing> processing) {
        for (RuleProcessing p : processing) {
            if (p.status == PENDING && p.rule.implications.contains(sentence))
                return p;
        }
        return null;
    }

    private Optional<Sentence> findInWorkingMemory(Sentence sentence, Set<Sentence> workingMemory) {
        return workingMemory.stream()
            .filter(c -> c.key.equals(sentence.key))
            .findFirst();
    }

}

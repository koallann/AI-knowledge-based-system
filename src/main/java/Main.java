import data.GoalsRepository;
import data.RulesRepository;
import domain.Rule;
import domain.Variable;
import inference.ExecutionParams;
import inference.ExecutionResult;
import inference.InferenceEngine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static util.OutputUtils.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final RulesRepository rulesRepository = new RulesRepository();
    private static final GoalsRepository goalsRepository = new GoalsRepository();

    public static void main(String[] args) {
        startMainMenu();
    }

    private static void startMainMenu() {
        int option;
        do {
            clear();
            println("===== Knowledge-Based System =====\n");
            println("1 - Run");
            println("2 - Manage Rules");
            println("3 - Manage Goals");
            println("0 - Exit");
            print("\nOption: ");

            option = Integer.parseInt(scanner.nextLine());
            print("\n");

            switch (option) {
                case 1:
                    runInferenceEngine();
                    break;
                case 2:
                    startRulesMenu();
                    break;
                case 3:
                    startGoalsMenu();
                    break;
                case 0:
                    println("Exiting...");
                    hold(scanner);
                    break;
                default:
                    println("Invalid option!");
                    hold(scanner);
                    break;
            }
        } while (option != 0);
    }

    private static void runInferenceEngine() {
        List<Rule> rules = rulesRepository.all();
        List<String> goals = goalsRepository.all();

        ExecutionParams params = new ExecutionParams(rules, new HashSet<>(goals), key -> {
            printf("What the value of variable \"%s\"?\n", key);
            print("Response: ");
            return scanner.nextLine().trim();
        });

        InferenceEngine engine = new InferenceEngine();
        ExecutionResult result = engine.run(params);

        println("\n" + result);
        hold(scanner);
    }

    private static void startRulesMenu() {
        int option;
        do {
            clear();
            println("===== Rules =====\n");
            println("1 - List");
            println("2 - Add");
            println("3 - Remove");
            println("0 - Back");
            print("\nOption: ");

            option = Integer.parseInt(scanner.nextLine());
            print("\n");

            switch (option) {
                case 1:
                    List<Rule> rules = rulesRepository.all();

                    if (rules.isEmpty()) println("EMPTY");
                    for (int i = 0; i < rules.size(); i++) printf("%d - %s\n", i, rules.get(i));

                    hold(scanner);
                    break;
                case 2:
                    println("Format: (key1=value1 and key2=value2 and ...)\n");

                    print("Type the rule conditions: ");
                    List<Variable> conditions = requestVariableConjunction();

                    print("Type the rule implications: ");
                    List<Variable> implications = requestVariableConjunction();

                    Rule rule = new Rule(conditions, implications);
                    rulesRepository.add(rule);

                    hold(scanner);
                    break;
                case 3:
                    print("Type the rule index: ");
                    int index = Integer.parseInt(scanner.nextLine());
                    rulesRepository.remove(index);
                    hold(scanner);
                    break;
                case 0:
                    break;
                default:
                    println("Invalid option!");
                    hold(scanner);
                    break;
            }
        } while (option != 0);
    }

    private static List<Variable> requestVariableConjunction() {
        String input = scanner.nextLine();
        String[] variables = input.split(" and ");

        return Arrays.stream(variables)
            .map(v -> {
                String[] keyValue = v.split("=");
                return new Variable(keyValue[0].trim(), keyValue[1].trim());
            })
            .collect(Collectors.toList());
    }

    private static void startGoalsMenu() {
        int option;
        do {
            clear();
            println("===== Goals =====\n");
            println("1 - List");
            println("2 - Add");
            println("3 - Remove");
            println("0 - Back");
            print("\nOption: ");

            option = Integer.parseInt(scanner.nextLine());
            print("\n");

            switch (option) {
                case 1:
                    List<String> goals = goalsRepository.all();

                    if (goals.isEmpty()) println("EMPTY");
                    for (int i = 0; i < goals.size(); i++) printf("%d - %s\n", i, goals.get(i));

                    hold(scanner);
                    break;
                case 2:
                    print("Type the goal key: ");
                    String goal = scanner.nextLine().trim();
                    goalsRepository.add(goal);
                    hold(scanner);
                    break;
                case 3:
                    print("Type the goal index: ");
                    int index = Integer.parseInt(scanner.nextLine());
                    goalsRepository.remove(index);
                    hold(scanner);
                    break;
                case 0:
                    break;
                default:
                    println("Invalid option!");
                    hold(scanner);
                    break;
            }
        } while (option != 0);
    }

}

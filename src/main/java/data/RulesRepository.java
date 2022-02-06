package data;

import domain.Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RulesRepository {

    private static final List<Rule> rules = new ArrayList<>();

    public List<Rule> all() {
        return Collections.unmodifiableList(rules);
    }

    public void add(Rule rule) {
        rules.add(rule);
    }

    public void remove(int index) {
        if (index < 0 || index >= rules.size()) return;
        rules.remove(index);
    }

}

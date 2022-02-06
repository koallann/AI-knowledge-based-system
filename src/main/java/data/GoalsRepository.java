package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GoalsRepository {

    private static final List<String> goals = new ArrayList<>();

    public List<String> all() {
        return Collections.unmodifiableList(goals);
    }

    public void add(String goal) {
        goals.add(goal);
    }

    public void remove(int index) {
        if (index < 0 || index >= goals.size()) return;
        goals.remove(index);
    }

}

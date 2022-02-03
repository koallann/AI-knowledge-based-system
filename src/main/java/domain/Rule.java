package domain;

import java.util.HashSet;
import java.util.Set;

public final class Rule {

    final Set<Sentence> conditions;
    final Set<Sentence> implications;

    public Rule() {
        this.conditions = new HashSet<>();
        this.implications = new HashSet<>();
    }

}

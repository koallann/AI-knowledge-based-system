package domain;

@FunctionalInterface
public interface OnRequestUserInput {
    String requestValue(Sentence sentence);
}

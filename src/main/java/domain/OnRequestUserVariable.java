package domain;

@FunctionalInterface
public interface OnRequestUserVariable {
    String requestValue(String key);
}

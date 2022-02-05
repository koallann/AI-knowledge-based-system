package domain;

import java.util.Objects;

public final class Sentence {

    public final String key;
    public final String value;

    public Sentence(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Sentence{%s=%s}", key, value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sentence sentence = (Sentence) o;
        return key.equals(sentence.key) && value.equals(sentence.value);
    }

}

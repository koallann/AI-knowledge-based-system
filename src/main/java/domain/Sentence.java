package domain;

import java.util.Objects;

public final class Sentence {

    final String key;
    final String value;

    public Sentence(String key, String value) {
        this.key = key;
        this.value = value;
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

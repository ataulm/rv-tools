package com.ataulm.rv;

import java.util.ArrayList;
import java.util.List;

public class StableIdDictionary<T> {

    private final List<T> dictionary;

    public StableIdDictionary() {
        this(0);
    }

    public StableIdDictionary(int itemCount) {
        this.dictionary = new ArrayList<>(itemCount);
    }

    public int getId(T t) {
        if (dictionary.contains(t)) {
            return dictionary.indexOf(t);
        } else {
            dictionary.add(t);
            return dictionary.size() - 1;
        }
    }

}

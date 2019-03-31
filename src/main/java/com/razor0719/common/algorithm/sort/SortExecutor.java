package com.razor0719.common.algorithm.sort;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SortExecutor<S extends Comparable> {
    @NonNull
    Sort<S> sort;

    public String getName() {
        return sort.getClass().getName();
    }

    public Direction getDirection() {
        return sort.getDirection();
    }

    public int getSize() {
        return sort.getSize();
    }

    public void printOriginal() {
        for (S s : sort.getValues()) {
            System.out.println(s.toString());
        }
    }

    public void printOrdered() {
        for (S s : sort.getValues()) {
            System.out.println(s.toString());
        }
    }
}

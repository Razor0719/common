package com.razor0719.common.algorithm.sort;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SortExecutor<S extends Comparable> implements Sort<S> {
    @NonNull
    Sort<S> sort;

    @Override
    public String getName() {
        return sort.getName();
    }

    @Override
    public List<S> getValues() {
        return sort.getValues();
    }

    @Override
    public Direction getDirection() {
        return sort.getDirection();
    }

    @Override
    public int getSize() {
        return sort.getSize();
    }

    @Override
    public Sort<S> sort() {
        return sort.sort();
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

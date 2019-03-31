package com.razor0719.common.algorithm.sort;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InsertSort<I extends Comparable> implements Sort<I> {

    @NonNull
    private List<I> values;
    @NonNull
    private Direction direction;
    private int size;

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    private void swap(int x, int y) {
        I value = values.get(x);
        values.set(x, values.get(y));
        values.set(y, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public InsertSort<I> sort() {
        this.size = values.size();
        for (int i = 0; i < size; i++) {
            for (int j = i; j > 0; j--) {
                if (values.get(j).compareTo(values.get(j - 1)) == direction.getValue()) {
                    swap(j, j-1);
                }
            }
        }
        return this;
    }

}

package com.razor0719.common.algorithm.sort;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BubbleSort<B extends Comparable> implements Sort<B> {
    @NonNull
    private List<B> values;
    @NonNull
    private Direction direction;
    private int size;

    public String getName() {
        return this.getClass().getName();
    }

    private void swap(int x, int y) {
        B value = values.get(x);
        values.set(x, values.get(y));
        values.set(y, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public BubbleSort<B> sort() {
        this.size = values.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (values.get(j + 1).compareTo(values.get(j)) == direction.getValue()) {
                    swap(j, j + 1);
                }
            }
        }
        return this;
    }

}

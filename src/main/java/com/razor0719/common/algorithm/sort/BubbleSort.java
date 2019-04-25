package com.razor0719.common.algorithm.sort;

import java.util.List;

import lombok.Getter;

@Getter
public class BubbleSort<B extends Comparable> implements Sort<B> {
    private List<B> values;
    private Direction direction;
    private int size;

    public BubbleSort(List<B> values, Direction direction) {
        this.values = values;
        this.direction = direction;
        this.size = values.size();
    }

    @Override
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

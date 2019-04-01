package com.razor0719.common.algorithm.sort;

import java.util.List;

import lombok.Getter;

@Getter
public class InsertSort<I extends Comparable> implements Sort<I> {

    private List<I> values;
    private Direction direction;
    private int size;

    public InsertSort(List<I> values, Direction direction) {
        this.values = values;
        this.direction = direction;
        this.size = values.size();
    }

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

package com.razor0719.common.algorithm.sort;

import java.util.List;
import java.util.Random;

import com.google.common.base.Objects;
import lombok.Getter;

@Getter
public class QuickSort<Q extends Comparable> implements Sort<Q> {

    private List<Q> values;
    private Direction direction;
    private int size;

    public QuickSort(List<Q> values, Direction direction) {
        this.values = values;
        this.direction = direction;
        this.size = values.size();
    }

    @SuppressWarnings("unchecked")
    private void quickly(int start, int end) {
        if (start >= end) {
            return;
        }
        int s = start;
        int e = end;
        Q key = values.get(s);
        while (s < e) {
            while (s < e && (key.compareTo(values.get(e)) == direction.getValue() || Objects.equal(values.get(e), key))) {
                e--;
            }
            while (s < e && (values.get(s).compareTo(key) == direction.getValue() || Objects.equal(key, values.get(s)))) {
                s++;
            }
            if (s < e) {
                swap(s, e);
            }
        }
        swap(s, start);
        quickly(start, s - 1);
        quickly(e + 1, end);
    }

    private void swap(int x, int y) {
        Q value = values.get(x);
        values.set(x, values.get(y));
        values.set(y, value);
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public QuickSort<Q> sort() {
        quickly(0, size - 1);
        return this;
    }

}

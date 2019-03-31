package com.razor0719.common.algorithm.sort;

import java.util.List;
import java.util.Random;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuickSort<Q extends Comparable> implements Sort<Q> {

    @NonNull
    private List<Q> values;
    @NonNull
    private Direction direction;
    private int size;

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

    @SuppressWarnings("unchecked")
    private void randomQuickly(int start, int end) {
        if (start >= end) {
            return;
        }
        int s = start;
        int e = end;
        int pos = new Random().nextInt(end - start) + start;
        Q key = values.get(pos);
        while (s < e) {
            while (s < e && (key.compareTo(values.get(e)) == direction.getValue() || Objects.equal(values.get(e), key))) {
                e--;
            }
            if (s < e) {
                swap(pos, e);
                pos = e;
            }
            while (s < e && (values.get(s).compareTo(key) == direction.getValue() || Objects.equal(key, values.get(s)))) {
                s++;
            }
            if (s < e) {
                swap(s, pos);
                pos = s;
            }
        }
        randomQuickly(start, s - 1);
        randomQuickly(e + 1, end);
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
        this.size = values.size();
        quickly(0, size - 1);
        return this;
    }

    public QuickSort<Q> randomSort() {
        this.size = values.size();
        randomQuickly(0, size - 1);
        return this;
    }

}

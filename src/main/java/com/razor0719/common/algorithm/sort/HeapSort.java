package com.razor0719.common.algorithm.sort;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HeapSort<H extends Comparable> implements Sort<H> {

    @NonNull
    private List<H> values;
    @NonNull
    private Direction direction;
    private int size;

    @SuppressWarnings("unchecked")
    private void heap(int start, int end) {
        int current = start;
        int left = start * 2 + 1;
        H dest = values.get(current);
        for (; left <= end; current = left, left = left * 2 + 1) {
            int right = left + 1;
            if (left < end && values.get(left).compareTo(values.get(right)) == direction.getValue()) {
                left = right;
            }
            if (dest.compareTo(values.get(left)) == direction.getValue()) {
                values.set(current, values.get(left));
                values.set(left, dest);
            } else {
                break;
            }
        }
    }

    @Override
    public HeapSort<H> sort() {
        this.size = values.size();
        for (int i = size / 2 - 1; i >= 0; i--) {
            heap(i, size - 1);
        }
        for (int i = size - 1; i > 0; i--) {
            H value = values.get(0);
            values.set(0, values.get(i));
            values.set(i, value);
            heap(0, i - 1);
        }
        return this;
    }

}

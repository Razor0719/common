package com.razor0719.common.algorithm.sort;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import lombok.Getter;

@Getter
public class MergeSort<M extends Comparable> implements Sort<M> {
    private List<M> values;
    private Direction direction;
    private int size;

    public MergeSort(List<M> values, Direction direction) {
        this.values = values;
        this.direction = direction;
        this.size = values.size();
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    private MergeSort setValues(List<M> values) {
        this.values = values;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MergeSort<M> sort() {
        return this.setValues(sort(0, size - 1));
    }

    private List<M> sort(int left, int right) {
        if (left == right) {
            return Lists.newArrayList(values.get(left));
        } else {
            int mid = (left + right) / 2;
            List<M> l = sort(left, mid);
            List<M> r = sort(mid + 1, right);
            return merge(l, r);
        }
    }

    @SuppressWarnings("unchecked")
    private List<M> merge(List<M> left, List<M> right) {
        List<M> m = Lists.newArrayList();
        int l = 0;
        int r = 0;
        while (l < left.size() && r < right.size()) {
            m.add(left.get(l).compareTo(right.get(r)) == this.direction.getValue() ? left.remove(l) : right.remove(r));
        }
        m.addAll(left);
        m.addAll(right);
        return m;
    }

}

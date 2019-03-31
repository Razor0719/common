package com.razor0719.common.algorithm.sort;

import java.util.List;

public interface Sort<S extends Comparable> {
    String getName();
    List<S> getValues();
    //方向
    Direction getDirection();
    int getSize();

    Sort<S> sort();
}

package com.razor0719.common.algorithm.sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Direction {
    ASC(-1),
    DESC(1);

    private final int value;
}

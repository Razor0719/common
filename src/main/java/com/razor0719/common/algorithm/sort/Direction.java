package com.razor0719.common.algorithm.sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Direction {
    /**
     * 升序
     */
    ASC(-1),
    /**
     * 降序
     */
    DESC(1);

    private final int value;
}

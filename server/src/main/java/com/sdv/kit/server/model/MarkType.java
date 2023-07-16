package com.sdv.kit.server.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MarkType {

    DEFAULT(0),
    MANDATORY_CONTROL_WORK(1),
    COURSE_WORK(2);

    private final Integer index;

    public static MarkType fromIndex(Integer index) {
        return MarkType.values()[index];
    }
}

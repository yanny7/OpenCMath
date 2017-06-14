package com.opencmath;

import java.util.HashMap;
import java.util.Map;

public enum NumberType {
    INVALID((byte) 0),
    INTEGER((byte) 1),
    REAL((byte) 2),
    COMPLEX((byte) 3),
    CONSTANT((byte) 4),
    MATRIX((byte) 5),
    ;

    private static final Map<Byte, NumberType> BY_VALUE = new HashMap<>();

    final byte value;

    static {
        for (NumberType type : values()) {
            BY_VALUE.put(type.value, type);
        }
    }

    NumberType(byte value) {
        this.value = value;
    }

    static NumberType fromValue(byte value) {
        return BY_VALUE.get(value);
    }
}

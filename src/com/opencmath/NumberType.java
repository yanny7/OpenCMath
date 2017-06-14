package com.opencmath;

import java.util.HashMap;
import java.util.Map;

public enum NumberType {
    COMPLEX((byte) 0),
    MATRIX((byte) 1),
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

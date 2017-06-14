package com.opencmath;

import java.util.HashMap;
import java.util.Map;

/**
 * Number types
 */
public enum NumberType {
    /**
     * Invalid number type, similar to NaN
     */
    INVALID((byte) 0),
    /**
     * Integer number type stored as Long
     */
    INTEGER((byte) 1),
    /**
     * Real number type stored as Double
     */
    REAL((byte) 2),
    /**
     * Complex number type stored as two Doubles for real and imaginary part
     */
    COMPLEX((byte) 3),
    /**
     * Constant number type
     * @see ConstantNumber
     */
    CONSTANT((byte) 4),
    /**
     * Matrix number type with max size 127x127. Items can be any type, also another matrix
     */
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

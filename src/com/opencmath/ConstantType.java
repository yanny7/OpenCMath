package com.opencmath;

import java.util.HashMap;
import java.util.Map;

/**
 * Constant types
 */
public enum ConstantType {
    /**
     * PI
     */
    PI("π", 0, Math.PI),

    /**
     * E
     */
    E("e", 1, Math.E),
    ;

    private static final Map<Integer, ConstantType> BY_VALUE = new HashMap<>();

    final int id;
    public final String visual;
    public final double value;

    static {
        for (ConstantType type : values()) {
            BY_VALUE.put(type.id, type);
        }
    }

    ConstantType(String visual, int id, double value) {
        this.visual = visual;
        this.id = id;
        this.value = value;
    }

    static ConstantType fromValue(int value) {
        return BY_VALUE.get(value);
    }
}

package com.opencmath;

/**
 * Angle types
 */
public enum AngleType {
    /**
     * Degrees
     */
    DEG(0.01745329251994329577, 57.29577951308232087679),

    /**
     * Gradians
     */
    GRAD(0.01570796326794896619, 63.66197723675813430755),

    /**
     * Radians
     */
    RAD(1, 1),
    ;

    final double toValue;
    final double fromValue;

    AngleType(double toValue, double fromValue) {
        this.toValue = toValue;
        this.fromValue = fromValue;
    }
}

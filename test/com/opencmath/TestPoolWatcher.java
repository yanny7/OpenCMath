package com.opencmath;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class TestPoolWatcher extends TestWatcher {
    private int initialInvalidPoolSize;
    private int initialIntegerPoolSize;
    private int initialRealPoolSize;
    private int initialComplexPoolSize;
    private int initialConstantPoolSize;
    private int initialMatrixPoolSize;

    protected void starting(Description description) {
        initialInvalidPoolSize = InvalidNumber.poolSize();
        initialIntegerPoolSize = IntegerNumber.poolSize();
        initialRealPoolSize = RealNumber.poolSize();
        initialComplexPoolSize = ComplexNumber.poolSize();
        initialConstantPoolSize = ConstantNumber.poolSize();
        initialMatrixPoolSize = MatrixNumber.poolSize();

        System.out.printf("Test '%s'\n", description.getDisplayName());
    }

    protected void succeeded(Description description) {
        assertEquals(initialInvalidPoolSize, InvalidNumber.poolSize());
        assertEquals(initialIntegerPoolSize, IntegerNumber.poolSize());
        assertEquals(initialRealPoolSize, RealNumber.poolSize());
        assertEquals(initialComplexPoolSize, ComplexNumber.poolSize());
        assertEquals(initialConstantPoolSize, ConstantNumber.poolSize());
        assertEquals(initialMatrixPoolSize, MatrixNumber.poolSize());

        assertTrue("InvalidNumber consistency", InvalidNumber.checkConsistency());
        assertTrue("IntegerNumber consistency", IntegerNumber.checkConsistency());
        assertTrue("RealNumber consistency", RealNumber.checkConsistency());
        assertTrue("ComplexNumber consistency", ComplexNumber.checkConsistency());
        assertTrue("ConstantNumber consistency", ConstantNumber.checkConsistency());
        assertTrue("MatrixNumber consistency", MatrixNumber.checkConsistency());
    }

}

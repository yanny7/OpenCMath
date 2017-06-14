package com.opencmath;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static org.junit.Assert.assertTrue;

public class TestPoolWatcher extends TestWatcher {

    private int initialComplexPoolSize;
    private int initialMatrixPoolSize;

    protected void starting(Description description) {
        initialComplexPoolSize = CNumber.poolSize();
        initialMatrixPoolSize = CMatrix.poolSize();

        System.out.printf("Test '%s'\n", description.getDisplayName());
    }

    protected void succeeded(Description description) {
        assertTrue("ComplexNumber", initialComplexPoolSize <= CNumber.poolSize());
        assertTrue("MatrixNumber", initialMatrixPoolSize <= CMatrix.poolSize());

        assertTrue("ComplexNumber consistency", CNumber.checkConsistency());
        assertTrue("MatrixNumber consistency", CMatrix.checkConsistency());
    }

}

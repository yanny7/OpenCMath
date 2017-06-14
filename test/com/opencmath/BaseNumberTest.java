package com.opencmath;

import org.junit.Test;

import java.util.ArrayList;

import static com.opencmath.BaseNumber.simplify;
import static com.opencmath.TestHelper.compareResultAndPut;
import static com.opencmath.TestHelper.parseNumber;
import static com.opencmath.TestHelper.parseValues;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BaseNumberTest {

    @Test
    public void simplifyTest() throws Exception {
        compareResultAndPut(simplify(parseNumber("NaN")), parseNumber("NaN"));
        compareResultAndPut(simplify(parseNumber("5")), parseNumber("5"));
        compareResultAndPut(simplify(parseNumber("PI")), parseNumber("PI"));
        compareResultAndPut(simplify(parseNumber("2.5")), parseNumber("2.5"));
        compareResultAndPut(simplify(parseNumber("2.0")), parseNumber("2"));
        compareResultAndPut(simplify(RealNumber.get(Double.NaN)), parseNumber("NaN"));
        compareResultAndPut(simplify(parseNumber("2+3i")), parseNumber("2+3i"));
        compareResultAndPut(simplify(parseNumber("2+0i")), parseNumber("2"));
        compareResultAndPut(simplify(parseNumber("2.5+0i")), parseNumber("2.5"));
        compareResultAndPut(simplify(ComplexNumber.get(2, Double.NaN)), parseNumber("NaN"));
        compareResultAndPut(simplify(ComplexNumber.get(Double.NaN, 2)), parseNumber("NaN"));
        compareResultAndPut(simplify(ComplexNumber.get(Double.NaN, Double.NaN)), parseNumber("NaN"));
        compareResultAndPut(simplify(ComplexNumber.get(2, Double.POSITIVE_INFINITY)), parseNumber("NaN"));
        compareResultAndPut(simplify(ComplexNumber.get(Double.POSITIVE_INFINITY, 2)), parseNumber("NaN"));
        compareResultAndPut(simplify(ComplexNumber.get(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)), parseNumber("NaN"));
        compareResultAndPut(simplify(parseNumber("M1x1[5]")), parseNumber("5"));
    }

    @Test
    public void fromToTest() throws Exception {
        ArrayList<BaseNumber> input = parseValues("{NaN, 2, 2.5, 2+3i, PI, M2x2[0;1;2;3]}");

        for (BaseNumber number : input) {
            byte[] data = BaseNumber.toData(number);
            assertNotNull(data);
            BaseNumber result = BaseNumber.fromData(data);
            assertNotNull(result);
            compareResultAndPut(number, result);
        }
    }

    @Test
    public void typeTest() throws Exception {
        assertEquals(parseNumber("NaN").getType(), NumberType.INVALID);
        assertEquals(parseNumber("5").getType(), NumberType.INTEGER);
        assertEquals(parseNumber("2.5").getType(), NumberType.REAL);
        assertEquals(parseNumber("PI").getType(), NumberType.CONSTANT);
        assertEquals(parseNumber("2+3i").getType(), NumberType.COMPLEX);
        assertEquals(parseNumber("M0x0[]").getType(), NumberType.MATRIX);
    }
}

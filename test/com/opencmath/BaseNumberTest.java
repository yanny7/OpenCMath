package com.opencmath;

import org.junit.Test;

import java.util.ArrayList;

import static com.opencmath.BaseNumber.put;
import static com.opencmath.BaseNumber.simplify;
import static com.opencmath.TestHelper.compareResultAndPut;
import static com.opencmath.TestHelper.parseNumber;
import static com.opencmath.TestHelper.parseValues;
import static org.junit.Assert.*;

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

    @Test
    public void getTest() throws Exception {
        BaseNumber a = BaseNumber.getNaN();
        BaseNumber b = BaseNumber.getInteger(10);
        BaseNumber c = BaseNumber.getReal(5.5);
        BaseNumber d = BaseNumber.getComplex(2, 3);
        BaseNumber e = BaseNumber.getConstant(ConstantType.PI);
        BaseNumber f = BaseNumber.getMatrix((byte) 2, (byte) 3);
        BaseNumber g = BaseNumber.getMatrix((byte) 2, (byte) 3, new BaseNumber[6]);

        assertEquals(a.getInteger(), Long.MIN_VALUE);
        assertEquals(b.getInteger(), 10);
        assertEquals(c.getInteger(), Long.MIN_VALUE);
        assertEquals(d.getInteger(), Long.MIN_VALUE);
        assertEquals(e.getInteger(), Long.MIN_VALUE);
        assertEquals(f.getInteger(), Long.MIN_VALUE);
        assertEquals(g.getInteger(), Long.MIN_VALUE);

        assertEquals(a.getReal(), Double.NaN, 0);
        assertEquals(b.getReal(), 10, 0);
        assertEquals(c.getReal(), 5.5, 0);
        assertEquals(d.getReal(), 2, 0);
        assertEquals(e.getReal(), ConstantType.PI.value, 0);
        assertEquals(f.getReal(), Double.NaN, 0);
        assertEquals(g.getReal(), Double.NaN, 0);

        assertEquals(a.getConstant(), null);
        assertEquals(b.getConstant(), null);
        assertEquals(c.getConstant(), null);
        assertEquals(d.getConstant(), null);
        assertEquals(e.getConstant(), ConstantType.PI);
        assertEquals(f.getConstant(), null);
        assertEquals(g.getConstant(), null);

        assertEquals(a.getImag(), Double.NaN, 0);
        assertEquals(b.getImag(), 0, 0);
        assertEquals(c.getImag(), 0, 0);
        assertEquals(d.getImag(), 3, 0);
        assertEquals(e.getImag(), 0, 0);
        assertEquals(f.getImag(), Double.NaN, 0);
        assertEquals(g.getImag(), Double.NaN, 0);

        assertNull(a.getMatrixItems());
        assertNull(b.getMatrixItems());
        assertNull(c.getMatrixItems());
        assertNull(d.getMatrixItems());
        assertNull(e.getMatrixItems());
        assertNotNull(f.getMatrixItems());
        assertNotNull(g.getMatrixItems());

        assertEquals(a.getCols(), -1);
        assertEquals(b.getCols(), -1);
        assertEquals(c.getCols(), -1);
        assertEquals(d.getCols(), -1);
        assertEquals(e.getCols(), -1);
        assertEquals(f.getCols(), 2);
        assertEquals(g.getCols(), 2);

        assertEquals(a.getRows(), -1);
        assertEquals(b.getRows(), -1);
        assertEquals(c.getRows(), -1);
        assertEquals(d.getRows(), -1);
        assertEquals(e.getRows(), -1);
        assertEquals(f.getRows(), 3);
        assertEquals(g.getRows(), 3);

        put(a);
        put(b);
        put(c);
        put(d);
        put(e);
        put(f);
        put(g);
    }
}

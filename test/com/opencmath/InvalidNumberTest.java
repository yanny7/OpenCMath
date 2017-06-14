package com.opencmath;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.ArrayList;

import static com.opencmath.BaseNumber.put;
import static com.opencmath.TestHelper.compareResultAndPut;
import static com.opencmath.TestHelper.parseValues;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvalidNumberTest {

    @Rule
    public TestRule watcher = new TestPoolWatcher();

    @Test
    public void equals() throws Exception {
        BaseNumber a = InvalidNumber.get();
        BaseNumber b = InvalidNumber.get();
        BaseNumber c = IntegerNumber.get(0);

        assertTrue(a.equals(b));
        //noinspection EqualsWithItself
        assertTrue(a.equals(a));
        //noinspection ObjectEqualsNull
        assertFalse(a.equals(null));
        assertFalse(a.equals(c));

        put(a);
        put(b);
        put(c);
    }

    @Test
    public void toStringTest() throws Exception {
        BaseNumber a = InvalidNumber.get();
        assertTrue(a.toString() != null);
        put(a);
    }

    @Test
    public void add() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,  10, 0.5, 2+3i,  PI, M1x1[2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).add(paramValues.get(i)));
        }
    }

    @Test
    public void sub() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,  10, 0.5, 2+3i,  PI, M1x1[2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sub(paramValues.get(i)));
        }
    }

    @Test
    public void mul() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,  10, 0.5, 2+3i,  PI, M1x1[2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).mul(paramValues.get(i)));
        }
    }

    @Test
    public void div() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,  10, 0.5, 2+3i,  PI, M1x1[2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).div(paramValues.get(i)));
        }
    }

    @Test
    public void arg() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).arg());
    }

    @Test
    public void abs() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).abs());
    }

    @Test
    public void ln() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).ln());
    }

    @Test
    public void log() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,  10, 0.5, 2+3i,  PI, M1x1[2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).log(paramValues.get(i)));
        }
    }

    @Test
    public void exp() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).exp());
    }

    @Test
    public void pow() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,  10, 0.5, 2+3i,  PI, M1x1[2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).pow(paramValues.get(i)));
        }
    }

    @Test
    public void sqrt() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).sqrt());
    }

    @Test
    public void root() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,  10, 0.5, 2+3i,  PI, M1x1[2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).root(paramValues.get(i)));
        }
    }

    @Test
    public void factorial() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).factorial());
    }

    @Test
    public void sin() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).sin());
    }

    @Test
    public void cos() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).cos());
    }

    @Test
    public void tan() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).tan());
    }

    @Test
    public void cot() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).cot());
    }

    @Test
    public void sec() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).sec());
    }

    @Test
    public void csc() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).csc());
    }

    @Test
    public void asin() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).asin());
    }

    @Test
    public void acos() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).acos());
    }

    @Test
    public void atan() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).atan());
    }

    @Test
    public void acot() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).acot());
    }

    @Test
    public void asec() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).asec());
    }

    @Test
    public void acsc() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).acsc());
    }

    @Test
    public void sinh() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).sinh());
    }

    @Test
    public void cosh() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).cosh());
    }

    @Test
    public void tanh() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).tanh());
    }

    @Test
    public void coth() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).coth());
    }

    @Test
    public void sech() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).sech());
    }

    @Test
    public void csch() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).csch());
    }

    @Test
    public void asinh() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).asinh());
    }

    @Test
    public void acosh() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).acosh());
    }

    @Test
    public void atanh() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).atanh());
    }

    @Test
    public void acoth() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).acoth());
    }

    @Test
    public void asech() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).asech());
    }

    @Test
    public void acsch() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).acsch());
    }

    @Test
    public void and() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,  10, 0.5, 2+3i,  PI, M1x1[2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).and(paramValues.get(i)));
        }
    }

    @Test
    public void or() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,  10, 0.5, 2+3i,  PI, M1x1[2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).or(paramValues.get(i)));
        }
    }

    @Test
    public void xor() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,  10, 0.5, 2+3i,  PI, M1x1[2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).xor(paramValues.get(i)));
        }
    }

    @Test
    public void not() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).not());
        }
    }

    @Test
    public void shl() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,  10, 0.5, 2+3i,  PI, M1x1[2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).shl(paramValues.get(i)));
        }
    }

    @Test
    public void shr() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,  10, 0.5, 2+3i,  PI, M1x1[2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN,  NaN, NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).shr(paramValues.get(i)));
        }
    }

    @Test
    public void det() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).det());
        }
    }

    @Test
    public void transpose() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).transpose());
        }
    }

    @Test
    public void tr() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).trace());
        }
    }

    @Test
    public void adj() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).adjugate());
        }
    }

    @Test
    public void rank() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).rank());
        }
    }

    @Test
    public void inv() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INVALID);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).inv());
        }
    }

    @Test
    public void toRadians() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).toRadians(AngleType.RAD));
    }

    @Test
    public void fromRadians() throws Exception {
        ArrayList<BaseNumber> inputValues =    parseValues("{ NaN }");
        ArrayList<BaseNumber> expectedValues = parseValues("{ NaN }");
        assertTrue(expectedValues.size() == inputValues.size());
        compareResultAndPut(expectedValues.get(0), inputValues.get(0).fromRadians(AngleType.RAD));
    }

}
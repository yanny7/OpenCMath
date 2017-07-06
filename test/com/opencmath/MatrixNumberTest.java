package com.opencmath;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.ArrayList;

import static com.opencmath.BaseNumber.put;
import static com.opencmath.TestHelper.compareResultAndPut;
import static com.opencmath.TestHelper.parseNumber;
import static com.opencmath.TestHelper.parseValues;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MatrixNumberTest {

    @Rule
    public TestRule watcher = new TestPoolWatcher();

    @Test
    public void equals() throws Exception {
        BaseNumber a = parseNumber("M2x2[0;1;2;3]");
        BaseNumber b = parseNumber("M2x2[0;1;2;3]");
        BaseNumber c = parseNumber("M2x1[0;1]");
        BaseNumber d = parseNumber("M2x2[0;1;2;4]");
        BaseNumber e = InvalidNumber.get();

        assertTrue(a.equals(b));
        //noinspection EqualsWithItself
        assertTrue(a.equals(a));
        //noinspection ObjectEqualsNull
        assertFalse(a.equals(null));
        assertFalse(a.equals(c));
        assertFalse(a.equals(d));
        assertFalse(a.equals(e));

        put(a);
        put(b);
        put(c);
        put(d);
        put(e);
    }

    @Test
    public void toStringTest() throws Exception {
        BaseNumber a = parseNumber("M2x2[0;1;2;3]");
        assertTrue(a.toString() != null);
        put(a);
    }

    @Test
    public void add() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x2[0;1;2;3], M2x2[0;1;2;3],         M2x2[0;1;2;3],           M2x2[0;1;2;3],     M2x2[0;0;0;0], M2x2[0;1;2;3], M2x2[0;1;2;3] }");
        ArrayList<BaseNumber> paramValues =     parseValues("{           NaN,             2,                   2.5,                      1i,                PI, M2x2[0;1;2;3],     M2x1[0;1] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{           NaN, M2x2[2;3;4;5], M2x2[2.5;3.5;4.5;5.5], M2x2[1i;1+1i;2+1i;3+1i], M2x2[PI;PI;PI;PI], M2x2[0;2;4;6],           NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).add(paramValues.get(i)));
        }
    }

    @Test
    public void sub() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x2[0;1;2;3], M2x2[2;3;4;5],         M2x2[3;4;5;6],            M2x2[0;1;2;3], M2x2[PI;PI;PI;PI], M2x2[0;1;2;3], M2x2[0;1;2;3] }");
        ArrayList<BaseNumber> paramValues =     parseValues("{           NaN,             2,                   2.5,                       1i,                PI, M2x2[0;1;2;3],     M2x1[0;1] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{           NaN, M2x2[0;1;2;3], M2x2[0.5;1.5;2.5;3.5], M2x2[-1i;1-1i;2-1i;3-1i],     M2x2[0;0;0;0], M2x2[0;0;0;0],           NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sub(paramValues.get(i)));
        }
    }

    @Test
    public void mul() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x2[0;1;2;3],  M2x2[2;3;4;5],     M2x2[3;4;5;6],    M2x2[0;1;2;3], M2x2[0;0;0;0],  M2x2[0;1;2;3], M1x2[0;1],     M2x1[0;1], M2x1[0;1] }");
        ArrayList<BaseNumber> paramValues =     parseValues("{           NaN,              2,               0.5,               1i,            PI,  M2x2[0;1;2;3], M2x1[0;1],     M1x2[0;1], M2x1[0;1] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{           NaN, M2x2[4;6;8;10], M2x2[1.5;2;2.5;3], M2x2[0;1i;2i;3i], M2x2[0;0;0;0], M2x2[2;3;6;11],         1, M2x2[0;0;0;1],       NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).mul(paramValues.get(i)));
        }
    }

    @Test
    public void div() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x2[0;1;2;3],     M2x2[2;3;4;5],   M2x2[3;4;5;6],       M2x2[0;1;2;3], M2x2[0;0;0;0],           M2x2[0;1;2;3], M2x1[0;1] }");
        ArrayList<BaseNumber> paramValues =     parseValues("{           NaN,                 2,             0.5,                  1i,            PI,           M2x2[3;2;1;0], M2x1[0;1] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{           NaN, M2x2[1;1.5;2;2.5], M2x2[6;8;10;12], M2x2[0;-1i;-2i;-3i], M2x2[0;0;0;0], M2x2[0.5;-1.5;1.5;-2.5],       NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).div(paramValues.get(i)));
        }
    }

    @Test
    public void arg() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[-1;1] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[PI;0] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).arg());
        }
    }

    @Test
    public void abs() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[-1;1] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  M2x1[1;1] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).abs());
        }
    }

    @Test
    public void ln() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[E;E] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[1;1] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).ln());
        }
    }

    @Test
    public void log() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x2[0;1;2;3], M2x2[2;2;4;4],     M2x2[2;4;2;4], M2x2[1;1;1;1i], M2x2[1;1;1;1], M2x2[0;1;2;3] }");
        ArrayList<BaseNumber> paramValues =     parseValues("{           NaN,             2,               0.5,             1i,            PI, M2x2[3;2;1;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{           NaN, M2x2[1;1;2;2], M2x2[-1;-2;-1;-2],  M2x2[0;0;0;1], M2x2[0;0;0;0],           NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).log(paramValues.get(i)));
        }
    }

    @Test
    public void exp() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[1;1] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).exp());
        }
    }

    @Test
    public void pow() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x2[0;1;2;3],  M2x2[1;2;3;4], M2x2[-4;4;-4;4], M2x2[1;1;1;1], M2x2[1;1;1;1], M2x2[0;1;2;3],      M2x2[0;1;2;3] }");
        ArrayList<BaseNumber> paramValues =     parseValues("{           NaN,              2,             0.5,            1i,            PI, M2x2[3;2;1;0],                 -1 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{           NaN, M2x2[1;4;9;16], M2x2[2i;2;2i;2], M2x2[1;1;1;1], M2x2[1;1;1;1],           NaN, M2x2[-1.5;0.5;1;0] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).pow(paramValues.get(i)));
        }
    }

    @Test
    public void sqrt() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[4;16] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  M2x1[2;4] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sqrt());
        }
    }

    @Test
    public void root() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x2[0;1;2;3], M2x2[4;4;16;16], M2x2[0;1;2;3], M2x2[1;1;1;1], M2x2[1;1;1;1], M2x2[0;1;2;3] }");
        ArrayList<BaseNumber> paramValues =     parseValues("{           NaN,               2,           0.5,            1i,            PI, M2x2[3;2;1;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{           NaN,   M2x2[2;2;4;4], M2x2[0;1;4;9], M2x2[1;1;1;1], M2x2[1;1;1;1],           NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).root(paramValues.get(i)));
        }
    }

    @Test
    public void factorial() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{        M2x1[5;10] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[120;3628800] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).factorial());
        }
    }

    @Test
    public void sin() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[0;PI] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  M2x1[0;0] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sin());
        }
    }

    @Test
    public void cos() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[0;PI] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[1;-1] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).cos());
        }
    }

    @Test
    public void tan() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[0;PI] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  M2x1[0;0] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).tan());
        }
    }

    @Test
    public void cot() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{    M2x1[0;PI] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[NaN;NaN] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).cot());
        }
    }

    @Test
    public void sec() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[0;PI] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[1;-1] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sec());
        }
    }

    @Test
    public void csc() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{    M2x1[0;PI] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[NaN;NaN] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).csc());
        }
    }

    @Test
    public void asin() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[0;0] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asin());
        }
    }

    @Test
    public void acos() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[1;1] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[0;0] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acos());
        }
    }

    @Test
    public void atan() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[0;0] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).atan());
        }
    }

    @Test
    public void acot() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                   M2x1[PI;PI] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[0.30816907111598494;0.30816907111598494] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acot());
        }
    }

    @Test
    public void asec() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{     M2x1[0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[NaN;NaN] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asec());
        }
    }

    @Test
    public void acsc() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{     M2x1[0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[NaN;NaN] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acsc());
        }
    }

    @Test
    public void sinh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[0;0] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sinh());
        }
    }

    @Test
    public void cosh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[1;1] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).cosh());
        }
    }

    @Test
    public void tanh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[0;0] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).tanh());
        }
    }

    @Test
    public void coth() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{     M2x1[0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[NaN;NaN] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).coth());
        }
    }

    @Test
    public void sech() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[1;1] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sech());
        }
    }

    @Test
    public void csch() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{     M2x1[0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[NaN;NaN] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).csch());
        }
    }

    @Test
    public void asinh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[0;0] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asinh());
        }
    }

    @Test
    public void acosh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[1;1] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[0;0] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acosh());
        }
    }

    @Test
    public void atanh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[0;0] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).atanh());
        }
    }

    @Test
    public void acoth() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                     M2x1[0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[1.5707963267948966i;1.5707963267948966i] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acoth());
        }
    }

    @Test
    public void asech() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[1;1] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[0;0] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asech());
        }
    }

    @Test
    public void acsch() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{     M2x1[0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[NaN;NaN] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acsch());
        }
    }

    @Test
    public void and() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M1x1[1], M1x1[1], M1x1[1], M1x1[1], M1x1[1], M1x1[1] }");
        ArrayList<BaseNumber> paramValues =     parseValues("{     NaN,       2,     2.5,      PI,    2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{     NaN,     NaN,     NaN,     NaN,     NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).and(paramValues.get(i)));
        }
    }

    @Test
    public void or() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M1x1[1], M1x1[1], M1x1[1], M1x1[1], M1x1[1], M1x1[1] }");
        ArrayList<BaseNumber> paramValues =     parseValues("{     NaN,       2,     2.5,      PI,    2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{     NaN,     NaN,     NaN,     NaN,     NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).or(paramValues.get(i)));
        }
    }

    @Test
    public void xor() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M1x1[1], M1x1[1], M1x1[1], M1x1[1], M1x1[1], M1x1[1] }");
        ArrayList<BaseNumber> paramValues =     parseValues("{     NaN,       2,     2.5,      PI,    2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{     NaN,     NaN,     NaN,     NaN,     NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).xor(paramValues.get(i)));
        }
    }

    @Test
    public void not() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M1x1[1] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).not());
        }
    }

    @Test
    public void shl() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M1x1[1], M1x1[1], M1x1[1], M1x1[1], M1x1[1], M1x1[1] }");
        ArrayList<BaseNumber> paramValues =     parseValues("{     NaN,       2,     2.5,      PI,    2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{     NaN,     NaN,     NaN,     NaN,     NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).shl(paramValues.get(i)));
        }
    }

    @Test
    public void shr() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M1x1[1], M1x1[1], M1x1[1], M1x1[1], M1x1[1], M1x1[1] }");
        ArrayList<BaseNumber> paramValues =     parseValues("{     NaN,       2,     2.5,      PI,    2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{     NaN,     NaN,     NaN,     NaN,     NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).shr(paramValues.get(i)));
        }
    }

    @Test
    public void det() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M1x1[1], M2x2[1;2;3;4], M3x3[0;1;2;3;4;5;6;7;9], M4x4[1;5;2;3;5;5;6;9;9;9;10;15;13;15;14;19], M1x2[1;2], M2x1[1;2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{       1,            -2,                      -3,                                         -32,       NaN,       NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).det());
        }
    }

    @Test
    public void transpose() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M1x1[1], M2x2[1;2;3;4], M3x3[1;2;3;4;5;6;7;8;9], M4x4[1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16], M1x2[1;2], M2x1[1;2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M1x1[1], M2x2[1;3;2;4], M3x3[1;4;7;2;5;8;3;6;9], M4x4[1;5;9;13;2;6;10;14;3;7;11;15;4;8;12;16], M2x1[1;2], M1x2[1;2] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).transpose());
        }
    }

    @Test
    public void tr() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M1x1[1], M2x2[1;2;3;4], M3x3[1;2;3;4;5;6;7;8;9], M4x4[1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16], M1x2[1;2], M2x1[1;2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{       1,             5,                      15,                                           34,       NaN,       NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).trace());
        }
    }

    @Test
    public void adj() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M1x1[1],   M2x2[1;2;3;4],       M3x3[1;2;3;4;5;6;7;8;9],              M4x4[1;5;2;3;5;5;6;9;9;9;10;15;13;15;14;19], M1x2[1;2], M2x1[1;2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{       1, M2x2[4;-2;-3;1], M3x3[-3;6;-3;6;-12;6;-3;6;-3], M4x4[8;64;-40;0;-8;16;-8;0;12;-120;100;-24;-8;32;-40;16],       NaN,       NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).adjugate());
        }
    }

    @Test
    public void rank() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M1x1[1], M2x2[1;2;3;4], M3x3[1;2;3;4;5;6;7;8;9], M4x4[1;5;2;3;5;5;6;9;9;9;10;15;13;15;14;19], M1x2[1;2], M2x1[1;2], M1x1[0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{       1,             2,                       2,                                           4,         1,         1,       0 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).rank());
        }
    }

    @Test
    public void inv() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M1x1[1],       M2x2[1;2;3;4],                                        M3x3[0;1;2;3;4;5;6;7;9],                                      M4x4[1;5;2;3;5;5;6;9;9;9;10;15;13;15;14;19], M1x2[1;2], M2x1[1;2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M1x1[1], M2x2[-2;1;1.5;-0.5], M3x3[-0.3333333333333333;-1.6666666666666667;1;-1;4;-2;1;-2;1], M4x4[-0.25;-2;1.25;0;0.25;-0.5;0.25;0;-0.375;3.75;-3.125;0.75;0.25;-1;1.25;-0.5],       NaN,       NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).inv());
        }
    }

    @Test
    public void gauss() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M1x1[1], M2x2[1;2;3;4], M3x3[0;1;2;3;4;5;6;7;9], M4x4[1;5;2;3;5;5;6;9;9;9;10;15;13;15;14;19], M1x2[1;2], M2x1[1;2], M3x4[1;3;1;9;1;1;-1;1;3;11;5;35], M1x1[0], M2x1[M1x1[0];1] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M1x1[1], M2x2[1;0;0;1], M3x3[1;0;0;0;1;0;0;0;1],       M4x4[1;0;0;0;0;1;0;0;0;0;1;0;0;0;0;1], M1x2[1;2], M2x1[1;0],  M3x4[1;0;-2;-3;0;1;1;4;0;0;0;0], M1x1[0], M1x1[NaN] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).gauss());
        }
    }

    @Test
    public void toRadians() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[1.5;1.5],                                   M2x1[1.5;1.5],                                 M2x1[1.5;1.5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[1.5;1.5], M2x1[0.026179938779914945;0.026179938779914945], M2x1[0.02356194490192345;0.02356194490192345] }");
        AngleType[] angles =         { AngleType.RAD, AngleType.DEG, AngleType.GRAD };
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).toRadians(angles[i]));
        }
    }

    @Test
    public void fromRadians() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ M2x1[1.5;1.5], M2x1[3.1415926535897932;3.1415926535897932], M2x1[3.1415926535897932;3.1415926535897932] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ M2x1[1.5;1.5],                               M2x1[180;180],                               M2x1[200;200] }");
        AngleType[] angles =         { AngleType.RAD, AngleType.DEG, AngleType.GRAD };
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.MATRIX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).fromRadians(angles[i]));
        }
    }
}

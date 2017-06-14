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

public class IntegerNumberTest {

    @Rule
    public TestRule watcher = new TestPoolWatcher();

    @Test
    public void equals() throws Exception {
        BaseNumber a = IntegerNumber.get(0);
        BaseNumber b = IntegerNumber.get(0);
        BaseNumber c = InvalidNumber.get();

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
        BaseNumber a = IntegerNumber.get(0);
        assertTrue(a.toString() != null);
        put(a);
    }

    @Test
    public void add() {
        ArrayList<BaseNumber> inputValues =     parseValues("{   2,   2,   2,    2,                  2,  0,   9223372036854775800,             2 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2, 2.5, 2+3i,                 PI, PI,                    10, M2x2[0;1;2;3] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN,   4, 4.5, 4+3i, 5.1415926535897932, PI, 9.2233720368547758E18, M2x2[2;3;4;5] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).add(paramValues.get(i)));
        }
    }

    @Test
    public void sub() {
        ArrayList<BaseNumber> inputValues =     parseValues("{   2,   2,    2,    2,                   2,                   0,   -9223372036854775800,              2 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2,  2.5, 2+3i,                  PI,                  PI,                     10,  M2x2[0;1;2;3] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN,   0, -0.5,  -3i, -1.1415926535897932, -3.1415926535897932, -9.2233720368547758E18, M2x2[2;1;0;-1] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sub(paramValues.get(i)));
        }
    }

    @Test
    public void mul() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{   2,   2,    2,    2,                  2, 10000000000000,             2 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2,  2.5, 2+3i,                 PI,    10000000000, M2x2[0;1;2;3] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN,   4,    5, 4+6i, 6.2831853071795865,          10E22, M2x2[0;2;4;6] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).mul(paramValues.get(i)));
        }
    }

    @Test
    public void div() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{   2,   2,    2,                                        2,                   2,   5,   1, 0,                 2 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2,  2.5,                                     2+3i,                  PI,   2,   0, 1,    M2x2[10;1;2;4] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN,   1,  0.8, 0.30769230769230769-0.46153846153846154i, 0.63661977236758134, 2.5, NaN, 0, M2x2[0.2;2;1;0.5] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).div(paramValues.get(i)));
        }
    }

    @Test
    public void arg() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ -2, 2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ PI, 0 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).arg());
        }
    }

    @Test
    public void abs() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ -2, 2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  2, 2 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).abs());
        }
    }

    @Test
    public void ln() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                      -2,                   2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.69314718055994531+3.1415926535897932i, 0.69314718055994531 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).ln());
        }
    }

    @Test
    public void log() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{   2, 2,                   2,                                        2,                   2, 2,                    -2,                                        2,             2 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN, 2,                 2.5,                                     2+3i,                  PI, 0,                     2,                                       -2, M2x2[2;2;2;2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 1, 0.75647079736603003, 0.34050994471749278-0.26094163254969080i, 0.60551156139828016, 0, 1+4.5323601418271938i, 0.04642032354540813-0.21039362420793021i, M2x2[1;1;1;1] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).log(paramValues.get(i)));
        }
    }

    @Test
    public void exp() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  -2,                  2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.13533528323661269, 7.3890560989306502 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).exp());
        }
    }

    @Test
    public void pow() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{   2, 2,                  2,                                       2,                  2,                                      -2, -2,                  -2,    10,             2 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN, 2,                2.5,                                    2+3i,                 PI,                                      PI,  2,                 2.5,   100, M2x2[2;2;2;2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 4, 5.6568542494923802, -1.9479776718631256+3.4936203270994856i, 8.8249778270762876, -7.9661783038856857-3.7973986989897564i,  4, 5.6568542494923802i, 1E100, M2x2[4;4;4;4] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).pow(paramValues.get(i)));
        }
    }

    @Test
    public void sqrt() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                   -2,                   2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.41421356237309505i, 1.41421356237309505 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sqrt());
        }
    }

    @Test
    public void root() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{   2,                   2,                   2,                                        2,                  2,   2,                   -2,                   2,             4 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,                   2,                 2.5,                                     2+3i,                 PI,   0,                    2,                  -2, M2x2[2;2;2;2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 1.41421356237309505, 1.31950791077289426, 1.09832904001806791-0.17719933735321196i, 1.2468689889006383, NaN, 1.41421356237309505i, 0.70710678118654752, M2x2[2;2;2;2] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).root(paramValues.get(i)));
        }
    }

    @Test
    public void factorial() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  -5,   5, 0,      10,                  20,                    100 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 120, 1, 3628800, 2432902008176640000, 9.3326215443944153E157 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).factorial());
        }
    }

    @Test
    public void sin() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                   -2,                   2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.90929742682568170, 0.90929742682568170 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sin());
        }
    }

    @Test
    public void cos() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                   -2,                    2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.41614683654714239, -0.41614683654714239 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).cos());
        }
    }

    @Test
    public void tan() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 -2,                   2, 0 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2.1850398632615190, -2.1850398632615190, 0 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).tan());
        }
    }

    @Test
    public void cot() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  -2,                    2,   0 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.45765755436028576, -0.45765755436028576, NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).cot());
        }
    }

    @Test
    public void sec() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  -2,                   2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -2.4029979617223810, -2.4029979617223810 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sec());
        }
    }

    @Test
    public void csc() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  -2,                  2,   0 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -1.0997501702946156, 1.0997501702946156, NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).csc());
        }
    }

    @Test
    public void asin() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                      -2,                                      2, 0,                  -1,                  1 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -1.5707963267948966+1.3169578969248167i, 1.5707963267948966-1.3169578969248167i, 0, -1.5707963267948966, 1.5707963267948966 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asin());
        }
    }

    @Test
    public void acos() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                     -2,                   2,                  0, -1, 1 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 3.1415926535897932-1.3169578969248167i, 1.3169578969248167i, 1.5707963267948966, PI, 0 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acos());
        }
    }

    @Test
    public void atan() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  -2,                  2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -1.1071487177940905, 1.1071487177940905 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).atan());
        }
    }

    @Test
    public void acot() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                   -2,                   2,                  0 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.46364760900080612, 0.46364760900080612, 1.5707963267948966 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acot());
        }
    }

    @Test
    public void asec() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 -2,                  2,   0, -1, 1 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2.0943951023931955, 1.0471975511965977, NaN, PI, 0 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asec());
        }
    }

    @Test
    public void acsc() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                   -2,                   2,   0,                  -1,                  1 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.52359877559829887, 0.52359877559829887, NaN, -1.5707963267948966, 1.5707963267948966 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acsc());
        }
    }

    @Test
    public void sinh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  -2,                  2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -3.6268604078470188, 3.6268604078470188 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sinh());
        }
    }

    @Test
    public void cosh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 -2,                  2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 3.7621956910836315, 3.7621956910836315 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).cosh());
        }
    }

    @Test
    public void tanh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                   -2,                   2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.96402758007581688, 0.96402758007581688 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).tanh());
        }
    }

    @Test
    public void coth() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  -2,                  2,   0 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -1.0373147207275481, 1.0373147207275481, NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).coth());
        }
    }

    @Test
    public void sech() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  -2,                   2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.26580222883407969, 0.26580222883407969 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sech());
        }
    }

    @Test
    public void csch() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                   -2,                   2,   0 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.27572056477178321, 0.27572056477178321, NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).csch());
        }
    }

    @Test
    public void asinh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  -2,                  2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -1.4436354751788103, 1.4436354751788103 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asinh());
        }
    }

    @Test
    public void acosh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                     -2,                  2,                   0,                  -1, 1 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.3169578969248167+3.1415926535897932i, 1.3169578969248167, 1.5707963267948966i, 3.1415926535897932i, 0 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acosh());
        }
    }

    @Test
    public void atanh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                      -2,                                      2, 0,        -1,        1 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.5493061443340548+1.5707963267948966i, 0.5493061443340548-1.5707963267948966i, 0, -Infinity, Infinity }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).atanh());
        }
    }

    @Test
    public void acoth() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                   -2,                   2,                   0,        -1,        1 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.54930614433405485, 0.54930614433405485, 1.5707963267948966i, -Infinity, Infinity }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acoth());
        }
    }

    @Test
    public void asech() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  -2,                   2,        0,                  -1, 1 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2.0943951023931955i, 1.0471975511965977i, Infinity, 3.1415926535897932i, 0 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asech());
        }
    }

    @Test
    public void acsch() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                   -2,                   2,   0 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.48121182505960345, 0.48121182505960345, NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acsch());
        }
    }

    @Test
    public void and() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{   2, 2,   2,   2,    2,       2 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN, 5, 2.5,  PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 0, NaN, NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).and(paramValues.get(i)));
        }
    }

    @Test
    public void or() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{   2, 2,   2,   2,    2,       2 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN, 5, 2.5,  PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 7, NaN, NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).or(paramValues.get(i)));
        }
    }

    @Test
    public void xor() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{   2, 2,   2,   2,    2,       2 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN, 5, 2.5,  PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 7, NaN, NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).xor(paramValues.get(i)));
        }
    }

    @Test
    public void not() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -3 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).not());
        }
    }

    @Test
    public void shl() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{   2,   8,   2,   2,    2,       2 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   4, 2.5,  PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 128, NaN, NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).shl(paramValues.get(i)));
        }
    }

    @Test
    public void shr() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{   2, 8,   2,   2,    2,       2 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN, 4, 2.5,  PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 0, NaN, NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).shr(paramValues.get(i)));
        }
    }

    @Test
    public void det() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2, -2, 0 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2, -2, 0 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).det());
        }
    }

    @Test
    public void transpose() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2, -2, 0 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2, -2, 0 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).transpose());
        }
    }

    @Test
    public void tr() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2, -2, 0 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2, -2, 0 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).trace());
        }
    }

    @Test
    public void adj() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2, -2, 0 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1,  1, 1 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).adjugate());
        }
    }

    @Test
    public void rank() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2, -2, 0 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1,  1, 0 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).rank());
        }
    }

    @Test
    public void inv() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{   2,   -2,   0 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.5, -0.5, NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).inv());
        }
    }

    @Test
    public void toRadians() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 100,                100,                100 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 100, 1.7453292519943296, 1.5707963267948966 }");
        AngleType[] angles =                    { AngleType.RAD, AngleType.DEG, AngleType.GRAD };
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).toRadians(angles[i]));
        }
    }

    @Test
    public void fromRadians() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2,                  2,                  2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2, 114.59155902616464, 127.32395447351627 }");
        AngleType[] angles =                    { AngleType.RAD, AngleType.DEG, AngleType.GRAD };
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.INTEGER);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).fromRadians(angles[i]));
        }
    }

}
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

public class RealNumberTest {

    @Rule
    public TestRule watcher = new TestPoolWatcher();

    @Test
    public void equals() throws Exception {
        BaseNumber a = RealNumber.get(0);
        BaseNumber b = RealNumber.get(0);
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
        BaseNumber a = RealNumber.get(0);
        assertTrue(a.toString() != null);
        put(a);
    }

    @Test
    public void add() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5, 2.5, 2.5,    2.5,                2.5,                   2.5 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2, 2.5,   2+3i,                 PI,         M2x2[0;1;2;3] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 4.5,   5, 4.5+3i, 5.6415926535897932, M2x2[2.5;3.5;4.5;5.5] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).add(paramValues.get(i)));
        }
    }

    @Test
    public void sub() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5, 2.5, 2.5,    2.5,                 2.5,                    2.5 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2, 2.5,   2+3i,                  PI,          M2x2[0;1;2;3] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 0.5,   0, 0.5-3i, -0.6415926535897932, M2x2[2.5;1.5;0.5;-0.5] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sub(paramValues.get(i)));
        }
    }

    @Test
    public void mul() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5, 2.5,  2.5,    2.5,                2.5,               2.5 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2,  2.5,   2+3i,                 PI,     M2x2[0;1;2;3] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN,   5, 6.25, 5+7.5i, 7.8539816339744831, M2x2[0;2.5;5;7.5] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).mul(paramValues.get(i)));
        }
    }

    @Test
    public void div() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5,  2.5,  2.5,                                      2.5,                 2.5,                       2.5 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,    2,  2.5,                                     2+3i,                  PI,            M2x2[-1;1;2;4] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 1.25,    1, 0.38461538461538462-0.57692307692307692i, 0.79577471545947668, M2x2[-2.5;2.5;1.25;0.625] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).div(paramValues.get(i)));
        }
    }

    @Test
    public void arg() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ -0.5, 0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{   PI,   0 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).arg());
        }
    }

    @Test
    public void abs() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ -0.5, 0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  0.5, 0.5 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).abs());
        }
    }

    @Test
    public void ln() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                     -0.5,                  0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.69314718055994531+3.1415926535897932i, -0.69314718055994531 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).ln());
        }
    }

    @Test
    public void log() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5,                2.5,  2.5,                                      2.5,                 2.5, 2.5,                                   -2.5,                                      2.5,                   2.5 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,                  2,  2.5,                                     2+3i,                  PI,   0,                                      2,                                       -2, M2x2[2.5;2.5;2.5;2.5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 1.3219280948873623,    1, 0.45012966251059631-0.34494607519321090i, 0.80044274479150062,   0, 1.3219280948873623+4.5323601418271938i, 0.06136432986843634-0.27812524282563682i,         M2x2[1;1;1;1] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).log(paramValues.get(i)));
        }
    }

    @Test
    public void exp() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                -0.5,                0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.60653065971263342, 1.6487212707001281 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).exp());
        }
    }

    @Test
    public void pow() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5,  2.5,                2.5,                                     2.5,                2.5,                 -2.5,                -2.5,                 -0.5,                                    -0.1,                     2.5 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,    2,                2.5,                                    2+3i,                 PI,                   PI,                 2.5,                 -0.5,                                    -0.1,           M2x2[0;1;2;3] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 6.25, 9.8821176880261854, -5.7741959497272868+2.3918948835918766i, 17.789568244261242, -7.6548728654118072i, 9.8821176880261854i, -1.4142135623730950i, 1.1973092164164023-0.38902934689487654i, M2x2[1;2.5;6.25;15.625] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).pow(paramValues.get(i)));
        }
    }

    @Test
    public void sqrt() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 -0.5,                 0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.70710678118654752i, 0.70710678118654752 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sqrt());
        }
    }

    @Test
    public void root() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5,                2.5,                2.5,                                    2.5,                 2.5,                                    -2.5, 2.5,                -2.5,                                   -2.5,                     2.5 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,                  2,                2.5,                                   2+3i,                  PI,                                      PI,   1,                   2,                                    2.5,     M2x2[1;-1;-0.5;0.5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 1.5811388300841897, 1.4426999059072136, 1.125743023889638-0.24165264275788171i, 1.33865368815920424, 0.72327767447130769+1.1264382372920484i, 2.5, 1.5811388300841897i, 0.4458187887084666+1.3720891465714604i, M2x2[2.5;0.4;0.16;6.25] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).root(paramValues.get(i)));
        }
    }

    @Test
    public void factorial() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{               -0.5,                 0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.7724538509055160, 0.88622692545275801 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).factorial());
        }
    }

    @Test
    public void sin() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 -0.5,                 0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.47942553860420300, 0.47942553860420300 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sin());
        }
    }

    @Test
    public void cos() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                -0.5,                 0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.87758256189037272, 0.87758256189037272 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).cos());
        }
    }

    @Test
    public void tan() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 -0.5,                 0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.54630248984379051, 0.54630248984379051 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).tan());
        }
    }

    @Test
    public void cot() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                -0.5,                0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -1.8304877217124519, 1.8304877217124519 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).cot());
        }
    }

    @Test
    public void sec() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{               -0.5,                0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.1394939273245491, 1.1394939273245491 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sec());
        }
    }

    @Test
    public void csc() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                -0.5,                0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -2.0858296429334882, 2.0858296429334882 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).csc());
        }
    }

    @Test
    public void asin() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 -0.5,                 0.5,                                    -2.5,                                    2.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.52359877559829887, 0.52359877559829887, -1.5707963267948966+1.5667992369724111i, 1.5707963267948966-1.5667992369724111i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asin());
        }
    }

    @Test
    public void acos() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{               -0.5,                0.5,                                   -2.5,                 2.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2.0943951023931955, 1.0471975511965977, 3.1415926535897932-1.5667992369724111i, 1.5667992369724111i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acos());
        }
    }

    @Test
    public void atan() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 -0.5,                 0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.46364760900080612, 0.46364760900080612 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).atan());
        }
    }

    @Test
    public void acot() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                -0.5,                0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -1.1071487177940905, 1.1071487177940905 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acot());
        }
    }

    @Test
    public void asec() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                   -0.5,                 0.5,               -2.5,                2.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 3.1415926535897932-1.3169578969248167i, 1.3169578969248167i, 1.9823131728623846, 1.1592794807274086 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asec());
        }
    }

    @Test
    public void acsc() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                    -0.5,                                    0.5,                 -2.5,                 2.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -1.5707963267948966+1.3169578969248167i, 1.5707963267948966-1.3169578969248167i, -0.41151684606748802, 0.41151684606748802 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acsc());
        }
    }

    @Test
    public void sinh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 -0.5,                 0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.52109530549374736, 0.52109530549374736 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sinh());
        }
    }

    @Test
    public void cosh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{               -0.5,                0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.1276259652063808, 1.1276259652063808 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).cosh());
        }
    }

    @Test
    public void tanh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 -0.5,                 0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.46211715726000976, 0.46211715726000976 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).tanh());
        }
    }

    @Test
    public void coth() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                -0.5,                0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -2.1639534137386528, 2.1639534137386528 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).coth());
        }
    }

    @Test
    public void sech() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                -0.5,                 0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.88681888397007391, 0.88681888397007391 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sech());
        }
    }

    @Test
    public void csch() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                -0.5,                0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -1.9190347513349437, 1.9190347513349437 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).csch());
        }
    }

    @Test
    public void asinh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 -0.5,                 0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.48121182505960345, 0.48121182505960345 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asinh());
        }
    }

    @Test
    public void acosh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                -0.5,                 0.5,                                   -2.5,                2.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2.0943951023931955i, 1.0471975511965977i, 1.5667992369724111+3.1415926535897932i, 1.5667992369724111 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acosh());
        }
    }

    @Test
    public void atanh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 -0.5,                 0.5,                                    -2.5,                                    2.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.54930614433405485, 0.54930614433405485, -0.4236489301936018+1.5707963267948966i, 0.4236489301936018-1.5707963267948966i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).atanh());
        }
    }

    @Test
    public void acoth() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                    -0.5,                                    0.5,                 -2.5,                 2.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.5493061443340548+1.5707963267948966i, 0.5493061443340548-1.5707963267948966i, -0.42364893019360181, 0.42364893019360181 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acoth());
        }
    }

    @Test
    public void asech() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                   -0.5,                0.5,                -2.5,                  2.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.3169578969248167+3.1415926535897932i, 1.3169578969248167, 1.9823131728623846i, 1.15927948072740860i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asech());
        }
    }

    @Test
    public void acsch() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                -0.5,                0.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -1.4436354751788103, 1.4436354751788103 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acsch());
        }
    }

    @Test
    public void and() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5, 2.5, 2.5, 2.5,  2.5,     2.5 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2, 2.5,  PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN, NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).and(paramValues.get(i)));
        }
    }

    @Test
    public void or() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5, 2.5, 2.5, 2.5,  2.5,     2.5 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2, 2.5,  PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN, NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).or(paramValues.get(i)));
        }
    }

    @Test
    public void xor() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5, 2.5, 2.5, 2.5,  2.5,     2.5 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2, 2.5,  PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN, NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).xor(paramValues.get(i)));
        }
    }

    @Test
    public void not() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).not());
        }
    }

    @Test
    public void shl() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5, 2.5, 2.5, 2.5,  2.5,     2.5 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2, 2.5,  PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN, NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).shl(paramValues.get(i)));
        }
    }

    @Test
    public void shr() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5, 2.5, 2.5, 2.5,  2.5,     2.5 }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2, 2.5,  PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN, NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).shr(paramValues.get(i)));
        }
    }

    @Test
    public void det() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5, -2.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2.5, -2.5 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).det());
        }
    }

    @Test
    public void transpose() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5, -2.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2.5, -2.5 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).transpose());
        }
    }

    @Test
    public void tr() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5, -2.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2.5, -2.5 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).trace());
        }
    }

    @Test
    public void adj() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5, -2.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{   1,    1 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).adjugate());
        }
    }

    @Test
    public void rank() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5, -2.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{   1,    1 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).rank());
        }
    }

    @Test
    public void inv() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2.5, -2.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.4, -0.4 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).inv());
        }
    }

    @Test
    public void toRadians() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 1.5,                  1.5,                 1.5 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.5, 0.026179938779914945, 0.02356194490192345 }");
        AngleType[] angles =         { AngleType.RAD, AngleType.DEG, AngleType.GRAD };
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).toRadians(angles[i]));
        }
    }

    @Test
    public void fromRadians() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 1.5, 3.1415926535897932, 3.1415926535897932 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.5,                180,                200 }");
        AngleType[] angles =         { AngleType.RAD, AngleType.DEG, AngleType.GRAD };
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.REAL);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).fromRadians(angles[i]));
        }
    }

}
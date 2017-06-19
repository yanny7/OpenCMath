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

public class ConstantNumberTest {

    @Rule
    public TestRule watcher = new TestPoolWatcher();

    @Test
    public void equals() throws Exception {
        BaseNumber a = ConstantNumber.get(ConstantType.PI);
        BaseNumber b = ConstantNumber.get(ConstantType.PI);
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
        BaseNumber a = ConstantNumber.get(ConstantType.PI);
        assertTrue(a.toString() != null);
        put(a);
    }

    @Test
    public void add() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  PI, PI,                 PI,                 PI,                    PI,                 PI,                PI }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,  0,                  2,                2.5,                  2+3i,                 PI,     M2x2[0;0;0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, PI, 5.1415926535897932, 5.6415926535897932, 5.1415926535897932+3i, 6.2831853071795865, M2x2[PI;PI;PI;PI] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).add(paramValues.get(i)));
        }
    }

    @Test
    public void sub() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  PI,                 PI,                 PI,                    PI, PI,                PI }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,                  2,                2.5,                  2+3i, PI,     M2x2[0;0;0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 1.1415926535897932, 0.6415926535897932, 1.1415926535897932+3i,  0, M2x2[PI;PI;PI;PI] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sub(paramValues.get(i)));
        }
    }

    @Test
    public void mul() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  PI,                 PI,                 PI,                                     PI,                 PI,            PI }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,                  2,                2.5,                                   2+3i,                 PI, M2x2[0;0;0;0] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 6.2831853071795865, 7.8539816339744831, 6.2831853071795865+9.4247779607693797i, 9.8696044010893586, M2x2[0;0;0;0] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).mul(paramValues.get(i)));
        }
    }

    @Test
    public void div() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  PI,                 PI,                 PI,                                       PI, PI,                PI }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,                  2,                2.5,                                     2+3i, PI, M2x2[PI;PI;PI;PI] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 1.5707963267948966, 1.2566370614359173, 0.48332194670612204-0.72498292005918306i,  1,     M2x2[1;1;1;1] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).div(paramValues.get(i)));
        }
    }

    @Test
    public void arg() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).arg());
        }
    }

    @Test
    public void abs() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ PI }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).abs());
        }
    }

    @Test
    public void ln() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.1447298858494002 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).ln());
        }
    }

    @Test
    public void log() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  PI,                 PI,                 PI,                                       PI, PI,                PI }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,                  2,                2.5,                                     2+3i, PI, M2x2[PI;PI;PI;PI] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 1.6514961294723188, 1.2493085939088374, 0.56235085574777258-0.43094409617400240i,  1,     M2x2[1;1;1;1] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).log(paramValues.get(i)));
        }
    }

    @Test
    public void exp() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 23.140692632779269 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).exp());
        }
    }

    @Test
    public void pow() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  PI,                 PI,                 PI,                                      PI,                 PI,                                                                                PI }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,                  2,                2.5,                                    2+3i,                 PI,                                                                     M2x2[1;1;1;1] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 9.8696044010893586, 17.493418327624863, -9.4501267303049968-2.8467869283066176i, 36.462159607207912, M2x2[3.1415926535897932;3.1415926535897932;3.1415926535897932;3.1415926535897932] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).pow(paramValues.get(i)));
        }
    }

    @Test
    public void sqrt() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.7724538509055160 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sqrt());
        }
    }

    @Test
    public void root() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  PI,                 PI,                 PI,                                      PI,                 PI,                                                                                PI }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,                  2,                2.5,                                    2+3i,                 PI,                                                                     M2x2[1;1;1;1] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 1.7724538509055160, 1.5807382019317313, 1.1512015980517069-0.31138845966129549i, 1.4396194958475907, M2x2[3.1415926535897932;3.1415926535897932;3.1415926535897932;3.1415926535897932] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).root(paramValues.get(i)));
        }
    }

    @Test
    public void factorial() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 7.1880827289760327 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).factorial());
        }
    }

    @Test
    public void sin() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ PI,                   E }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  0, 0.41078129050290870 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sin());
        }
    }

    @Test
    public void cos() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ PI,                    E }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -1, -0.91173391478696510 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).cos());
        }
    }

    @Test
    public void tan() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ PI,                    E }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  0, -0.45054953406980750 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).tan());
        }
    }

    @Test
    public void cot() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  PI,                   E }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, -2.2195117836811733 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).cot());
        }
    }

    @Test
    public void sec() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ PI,                   E }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -1, -1.0968112338276448 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sec());
        }
    }

    @Test
    public void csc() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  PI,                  E }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, 2.4343854579543445 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).csc());
        }
    }

    @Test
    public void asin() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                     PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.5707963267948966-1.8115262724608531i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asin());
        }
    }

    @Test
    public void acos() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.8115262724608531i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acos());
        }
    }

    @Test
    public void atan() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.2626272556789117 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).atan());
        }
    }

    @Test
    public void acot() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.30816907111598494 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acot());
        }
    }

    @Test
    public void asec() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.2468502198629159 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asec());
        }
    }

    @Test
    public void acsc() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.32394610693198072 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acsc());
        }
    }

    @Test
    public void sinh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 11.548739357257748 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sinh());
        }
    }

    @Test
    public void cosh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 11.591953275521521 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).cosh());
        }
    }

    @Test
    public void tanh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.99627207622074994 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).tanh());
        }
    }

    @Test
    public void coth() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.0037418731973213 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).coth());
        }
    }

    @Test
    public void sech() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                   PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.086266738334054415 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sech());
        }
    }

    @Test
    public void csch() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                   PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.086589537530046942 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).csch());
        }
    }

    @Test
    public void asinh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.8622957433108482 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asinh());
        }
    }

    @Test
    public void acosh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.8115262724608531 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acosh());
        }
    }

    @Test
    public void atanh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                     PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.3297653149566991-1.5707963267948966i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).atanh());
        }
    }

    @Test
    public void acoth() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.32976531495669911 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acoth());
        }
    }

    @Test
    public void asech() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                   PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.24685021986291590i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asech());
        }
    }

    @Test
    public void acsch() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                  PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.31316588045086838 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acsch());
        }
    }

    @Test
    public void and() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  PI,  PI,  PI,  PI,   PI,      PI }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2, 2.5,  PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN, NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).and(paramValues.get(i)));
        }
    }

    @Test
    public void or() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  PI,  PI,  PI,  PI,   PI,      PI }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2, 2.5,  PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN, NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).or(paramValues.get(i)));
        }
    }

    @Test
    public void xor() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  PI,  PI,  PI,  PI,   PI,      PI }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2, 2.5,  PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN, NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).xor(paramValues.get(i)));
        }
    }

    @Test
    public void not() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).not());
        }
    }

    @Test
    public void shl() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  PI,  PI,  PI,  PI,   PI,      PI }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2, 2.5,  PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN, NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).shl(paramValues.get(i)));
        }
    }

    @Test
    public void shr() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{  PI,  PI,  PI,  PI,   PI,      PI }");
        ArrayList<BaseNumber> paramValues =     parseValues("{ NaN,   2, 2.5,  PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ NaN, NaN, NaN, NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).shr(paramValues.get(i)));
        }
    }

    @Test
    public void det() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ PI }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).det());
        }
    }

    @Test
    public void transpose() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ PI }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).transpose());
        }
    }

    @Test
    public void tr() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ PI }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).trace());
        }
    }

    @Test
    public void adj() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  1 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).adjugate());
        }
    }

    @Test
    public void rank() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  1 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).rank());
        }
    }

    @Test
    public void inv() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                 PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.3183098861837907 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).inv());
        }
    }

    @Test
    public void gauss() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  0 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).gauss());
        }
    }

    @Test
    public void toRadians() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ PI,                   PI,                   PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ PI, 0.054831135561607548, 0.049348022005446793 }");
        AngleType[] angles =                    { AngleType.RAD, AngleType.DEG, AngleType.GRAD };
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).toRadians(angles[i]));
        }
    }

    @Test
    public void fromRadians() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ PI,  PI,  PI }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ PI, 180, 200 }");
        AngleType[] angles =                    { AngleType.RAD, AngleType.DEG, AngleType.GRAD };
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.CONSTANT);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).fromRadians(angles[i]));
        }
    }

}
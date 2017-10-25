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

public class ComplexNumberTest {

    @Rule
    public TestRule watcher = new TestPoolWatcher();

    @Test
    public void equals() throws Exception {
        BaseNumber a = ComplexNumber.get(0, 0);
        BaseNumber b = ComplexNumber.get(0, 0);
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
        BaseNumber a = ComplexNumber.get(0, 0);
        assertTrue(a.toString() != null);
        put(a);
    }

    @Test
    public void add() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i, 2+3i,   2+3i, 2+3i,                  2+3i,                      2+3i }");
        ArrayList<BaseNumber> paramValues =     parseValues("{  NaN,    1,    2.5, 2+3i,                    PI,             M2x2[0;1;2;3] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  NaN, 3+3i, 4.5+3i, 4+6i, 5.1415926535897932+3i, M2x2[2+3i;3+3i;4+3i;5+3i] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).add(paramValues.get(i)));
        }
    }

    @Test
    public void sub() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i, 2+3i,    2+3i, 2+3i,                   2+3i,                       2+3i }");
        ArrayList<BaseNumber> paramValues =     parseValues("{  NaN,    1,     2.5, 2+3i,                     PI,              M2x2[0;1;2;3] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  NaN, 1+3i, -0.5+3i,    0, -1.1415926535897932+3i, M2x2[2+3i;1+3i;0+3i;-1+3i] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sub(paramValues.get(i)));
        }
    }

    @Test
    public void mul() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i, 2+3i,   2+3i,   2+3i,                                   2+3i,                   2+3i }");
        ArrayList<BaseNumber> paramValues =     parseValues("{  NaN,    2,    2.5,   2+3i,                                     PI,          M2x2[0;1;2;3] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  NaN, 4+6i, 5+7.5i, -5+12i, 6.2831853071795865+9.4247779607693797i, M2x2[0;2+3i;4+6i;6+9i] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).mul(paramValues.get(i)));
        }
    }

    @Test
    public void div() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i,   2+3i,     2+3i, 2+3i,                                     2+3i, 2+3i,                              2+3i }");
        ArrayList<BaseNumber> paramValues =     parseValues("{  NaN,      2,      2.5, 2+3i,                                       PI,    0,                    M2x2[-1;1;2;4] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  NaN, 1+1.5i, 0.8+1.2i,    1, 0.63661977236758134+0.95492965855137201i,  NaN, M2x2[-2-3i;2+3i;1+1.5i;0.5+0.75i] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).div(paramValues.get(i)));
        }
    }

    @Test
    public void arg() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{               -2-3i,              -2+3i,                 2-3i,                2+3i,                 -3i,                 3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -2.1587989303424642, 2.1587989303424642, -0.98279372324732907, 0.98279372324732907, -1.5707963267948966, 1.5707963267948966 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).arg());
        }
    }

    @Test
    public void abs() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{              -2-3i,              -2+3i,               2-3i,               2+3i, -3i, 3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 3.6055512754639893, 3.6055512754639893, 3.6055512754639893, 3.6055512754639893,   3,  3 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).abs());
        }
    }

    @Test
    public void ln() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                  -2-3i,                                  -2+3i,                                   2-3i,                                   2+3i,                                    -3i,                                     3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.2824746787307684-2.1587989303424642i, 1.2824746787307684+2.1587989303424642i, 1.2824746787307684-0.9827937232473291i, 1.2824746787307684+0.9827937232473291i, 1.0986122886681097-1.5707963267948966i, 1.0986122886681097+1.5707963267948966i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).ln());
        }
    }

    @Test
    public void log() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i,                                   2+3i,                                   2+3i, 2+3i,                                     2+3i, 2+3i, 2+3i,                 1i }");
        ArrayList<BaseNumber> paramValues =     parseValues("{  NaN,                                      2,                                    2.5, 2+3i,                                       PI,    0,    1, M2x2[-1;-1;1i;-1i] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  NaN, 1.8502198590705461+1.4178716307457220i, 1.3996372920935597+1.0725784830728896i,    1, 1.12032951579591222+0.85853766499516786i,    0,  NaN, M2x2[0.5;0.5;1;-1] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).log(paramValues.get(i)));
        }
    }

    @Test
    public void exp() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                       -2-3i,                                       -2+3i,                                    2-3i,                                    2+3i,                                  -3i,                                   3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.133980914929542613-0.019098516261135196i, -0.133980914929542613+0.019098516261135196i, -7.3151100949011025-1.0427436562359044i, -7.3151100949011025+1.0427436562359044i, -0.989992496600445-0.14112000805987i, -0.989992496600445+0.14112000805987i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).exp());
        }
    }

    @Test
    public void pow() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i,   2+3i,                                    2+3i,                                     2+3i, 2+3i,                                   2+3i, 2+3i,                          2+3i, -3i, -3i,                    3i,                  3i }");
        ArrayList<BaseNumber> paramValues =     parseValues("{  NaN,      2,                                     2.5,                                     2+3i,    0,                                     PI,    1,                 M2x2[0;1;2;4],   3,   2,                    -3,                  -2 }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  NaN, -5+12i, -19.122475853735758+15.609903355777290i, 0.60756666473147822-0.30875601809790225i,    1, -56.123315905497452+3.036710450050800i, 2+3i, M2x2[1;2+3i;-5+12i;-119-120i], 27i,  -9, 0.037037037037037037i, 0.11111111111111111 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).pow(paramValues.get(i)));
        }
    }

    @Test
    public void sqrt() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                  -2-3i,                                  -2+3i,                                   2-3i,                                   2+3i,                                    -3i,                                     3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.8959774761298381-1.6741492280355400i, 0.8959774761298381+1.6741492280355400i, 1.6741492280355400-0.8959774761298381i, 1.6741492280355400+0.8959774761298381i, 1.2247448713915890-1.2247448713915890i, 1.2247448713915890+1.2247448713915890i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sqrt());
        }
    }

    @Test
    public void root() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i,                                   2+3i,                                   2+3i,                                    2+3i,                                       2+3i, 2+3i, 2+3i,                                 2+3i }");
        ArrayList<BaseNumber> paramValues =     parseValues("{  NaN,                                      2,                                    2.5,                                    2+3i,                                         PI,    0,    1,                 M2x2[0.5;1;0.25;0.2] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  NaN, 1.6741492280355400+0.8959774761298381i, 1.5428677605929858+0.6398331885826732i, 1.5122344211248442-0.22044795193453988i, 1.431141747176076598+0.462908519632583600i,  NaN, 2+3i, M2x2[-5+12i;2+3i;-119-120i;122-597i] }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).root(paramValues.get(i)));
        }
    }

    @Test
    public void factorial() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                         -2-3i,                                         -2+3i,                                      2-3i,                                      2+3i,                                        -3i,                                         3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.0030591429145086438+0.0027465090888537295i, -0.0030591429145086438-0.0027465090888537295i, -0.44011340763700171+0.06363724312631702i, -0.44011340763700171-0.06363724312631702i, 0.019292758964016606-0.033896010543209497i, 0.019292758964016606+0.033896010543209497i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).factorial());
        }
    }

    @Test
    public void sin() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                   -2-3i,                                   -2+3i,                                   2-3i,                                   2+3i,                  -3i,                  3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -9.1544991469114296+4.1689069599665644i, -9.1544991469114296-4.1689069599665644i, 9.1544991469114296+4.1689069599665644i, 9.1544991469114296-4.1689069599665644i, -10.017874927409902i, 10.017874927409902i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sin());
        }
    }

    @Test
    public void cos() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                   -2-3i,                                   -2+3i,                                    2-3i,                                    2+3i,                -3i,                 3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -4.1896256909688072-9.1092278937553366i, -4.1896256909688072+9.1092278937553366i, -4.1896256909688072+9.1092278937553366i, -4.1896256909688072-9.1092278937553366i, 10.067661995777766, 10.067661995777766 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).cos());
        }
    }

    @Test
    public void tan() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                     -2-3i,                                     -2+3i,                                       2-3i,                                       2+3i,                   -3i,                   3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.0037640256415042483-1.0032386273536098i, 0.0037640256415042483+1.0032386273536098i, -0.0037640256415042483-1.0032386273536098i, -0.0037640256415042483+1.0032386273536098i, -0.99505475368673045i, 0.99505475368673045i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).tan());
        }
    }

    @Test
    public void cot() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                    -2-3i,                                    -2+3i,                                      2-3i,                                      2+3i,                 -3i,                   3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.00373971037633696+0.99675779656935831i, 0.00373971037633696-0.99675779656935831i, -0.00373971037633696+0.99675779656935831i, -0.00373971037633696-0.99675779656935831i, 1.0049698233136892i, -1.0049698233136892i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).cot());
        }
    }

    @Test
    public void sec() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                      -2-3i,                                      -2+3i,                                       2-3i,                                       2+3i,                  -3i,                   3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.04167496441114427+0.090611137196237597i, -0.04167496441114427-0.090611137196237597i, -0.04167496441114427-0.090611137196237597i, -0.04167496441114427+0.090611137196237597i, 0.099327927419433208, 0.099327927419433208 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sec());
        }
    }

    @Test
    public void csc() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                      -2-3i,                                      -2+3i,                                      2-3i,                                      2+3i,                   -3i,                     3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.09047320975320744-0.041200986288574126i, -0.09047320975320744+0.041200986288574126i, 0.09047320975320744-0.041200986288574126i, 0.09047320975320744+0.041200986288574126i, 0.099821569668822733i, -0.099821569668822733i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).csc());
        }
    }

    @Test
    public void asin() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                    -2-3i,                                    -2+3i,                                    2-3i,                                    2+3i,                  -3i,                  3i,                               2-0.5i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.57065278432110994-1.9833870299165354i, -0.57065278432110994+1.9833870299165354i, 0.57065278432110994-1.9833870299165354i, 0.57065278432110944+1.9833870299165354i, -1.8184464592320668i, 1.8184464592320668i, 1.293042070237183-1.361800900857846i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asin());
        }
    }

    @Test
    public void acos() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                -2-3i,                                 -2+3i,                                   2-3i,                                   2+3i,                                    -3i,                                     3i,                                 2-0.5i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{2.141449111115996+1.9833870299165354i, 2.141449111115996-1.9833870299165354i, 1.0001435424737972+1.9833870299165354i, 1.0001435424737972-1.9833870299165354i, 1.5707963267948966+1.8184464592320668i, 1.5707963267948966-1.8184464592320668i, 0.2777542565577140+1.3618009008578458i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acos());
        }
    }

    @Test
    public void atan() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                  -2-3i,                                   -2+3i,                                   2-3i,                                   2+3i,                                     -3i,                                     3i, -1i,  1i,                0.5i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{-1.4099210495965755-0.2290726829685388i, -1.4099210495965755+0.2290726829685388i, 1.4099210495965755-0.2290726829685388i, 1.4099210495965755+0.2290726829685388i, -1.5707963267948966-0.3465735902799727i, 1.5707963267948966+0.3465735902799727i, NaN, NaN, 0.5493061443340548i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).atan());
        }
    }

    @Test
    public void acot() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                    -2-3i,                                    -2+3i,                                    2-3i,                                    2+3i,                  -3i,                    3i, -1i,  1i,                                   0.5i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.1608752771983211+0.22907268296853877i, -0.1608752771983211-0.22907268296853877i, 0.1608752771983211+0.22907268296853877i, 0.1608752771983211-0.22907268296853877i, 0.34657359027997265i, -0.34657359027997265i, NaN, NaN, -1.570796326794897-0.5493061443340548i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acot());
        }
    }

    @Test
    public void asec() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                  -2-3i,                                  -2+3i,                                   2-3i,                                   2+3i,                                    -3i,                                     3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.7211819311227586-0.2313346985739733i, 1.7211819311227586+0.2313346985739733i, 1.4204107224670347-0.2313346985739733i, 1.4204107224670347+0.2313346985739733i, 1.5707963267948966-0.3274501502372584i, 1.5707963267948966+0.3274501502372584i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asec());
        }
    }

    @Test
    public void acsc() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                     -2-3i,                                     -2+3i,                                     2-3i,                                     2+3i,                  -3i,                    3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.15038560432786196+0.23133469857397331i, -0.15038560432786196-0.23133469857397331i, 0.15038560432786196+0.23133469857397331i, 0.15038560432786196-0.23133469857397331i, 0.32745015023725844i, -0.32745015023725844i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acsc());
        }
    }

    @Test
    public void sinh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                -2-3i,                                -2+3i,                                  2-3i,                                  2+3i,                   -3i,                   3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 3.59056458998578-0.5309210862485198i, 3.59056458998578+0.5309210862485198i, -3.59056458998578-0.5309210862485198i, -3.59056458998578+0.5309210862485198i, -0.14112000805986722i, 0.14112000805986722i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sinh());
        }
    }

    @Test
    public void cosh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                   -2-3i,                                   -2+3i,                                    2-3i,                                    2+3i,                  -3i,                   3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -3.7245455049153226+0.5118225699873846i, -3.7245455049153226-0.5118225699873846i, -3.7245455049153226-0.5118225699873846i, -3.7245455049153226+0.5118225699873846i, -0.98999249660044546, -0.98999249660044546 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).cosh());
        }
    }

    @Test
    public void tanh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                     -2-3i,                                     -2+3i,                                     2-3i,                                     2+3i,                  -3i,                    3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.96538587902213312+0.00988437503832249i, -0.96538587902213312-0.00988437503832249i, 0.96538587902213312+0.00988437503832249i, 0.96538587902213312-0.00988437503832249i, 0.14254654307427781i, -0.14254654307427781i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).tanh());
        }
    }

    @Test
    public void coth() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                   -2-3i,                                   -2+3i,                                   2-3i,                                   2+3i,                  -3i,                  3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -1.0357466377649954-0.0106047834703371i, -1.0357466377649954+0.0106047834703371i, 1.0357466377649954-0.0106047834703371i, 1.0357466377649954+0.0106047834703371i, -7.0152525514345335i, 7.0152525514345335i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).coth());
        }
    }

    @Test
    public void sech() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                     -2-3i,                                     -2+3i,                                      2-3i,                                      2+3i,                 -3i,                  3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.26351297515838931-0.03621163655876852i, -0.26351297515838931+0.03621163655876852i, -0.26351297515838931+0.03621163655876852i, -0.26351297515838931-0.03621163655876852i, -1.0101086659079938, -1.0101086659079938 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).sech());
        }
    }

    @Test
    public void csch() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                   -2-3i,                                   -2+3i,                                     2-3i,                                     2+3i,                 -3i,                   3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.2725486614629402+0.04030057885689152i, 0.2725486614629402-0.04030057885689152i, -0.2725486614629402+0.04030057885689152i, -0.2725486614629402-0.04030057885689152i, 7.0861673957371859i, -7.0861673957371859i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).csch());
        }
    }

    @Test
    public void asinh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                   -2-3i,                                   -2+3i,                                   2-3i,                                   2+3i,                                     -3i,                                     3i,                                2-0.5i,                0.5i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -1.9686379257930963-0.9646585044076028i, -1.9686379257930963+0.9646585044076028i, 1.9686379257930963-0.9646585044076028i, 1.9686379257930963+0.9646585044076028i, -1.7627471740390861-1.5707963267948966i, 1.7627471740390861+1.5707963267948966i, 1.465715351947291-0.2210186356228839i, 0.5235987755982989i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asinh());
        }
    }

    @Test
    public void acosh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                 -2-3i,                                 -2+3i,                                   2-3i,                                   2+3i,                                    -3i,                                     3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 1.9833870299165354-2.141449111115996i, 1.9833870299165354+2.141449111115996i, 1.9833870299165354-1.0001435424737972i, 1.9833870299165354+1.0001435424737972i, 1.8184464592320668-1.5707963267948966i, 1.8184464592320668+1.5707963267948966i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acosh());
        }
    }

    @Test
    public void atanh() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                     -2-3i,                                     -2+3i,                                     2-3i,                                     2+3i,                  -3i,                  3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.14694666622552975-1.33897252229449356i, -0.14694666622552975+1.33897252229449356i, 0.14694666622552975-1.33897252229449356i, 0.14694666622552975+1.33897252229449356i, -1.2490457723982544i, 1.2490457723982544i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).atanh());
        }
    }

    @Test
    public void acoth() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                     -2-3i,                                     -2+3i,                                     2-3i,                                     2+3i,                  -3i,                    3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.14694666622552975+0.23182380450040306i, -0.14694666622552975-0.23182380450040306i, 0.14694666622552975+0.23182380450040306i, 0.14694666622552975-0.23182380450040306i, 0.32175055439664219i, -0.32175055439664219i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acoth());
        }
    }

    @Test
    public void asech() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                  -2-3i,                                  -2+3i,                                   2-3i,                                   2+3i,                                    -3i,                                     3i,                              0.5+0.5i,                            -0.5+0.1i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.2313346985739733+1.7211819311227586i, 0.2313346985739733-1.7211819311227586i, 0.2313346985739733+1.4204107224670347i, 0.2313346985739733-1.4204107224670347i, 0.3274501502372584+1.5707963267948966i, 0.3274501502372584-1.5707963267948966i, 1.061275061905036-0.9045568943023814i, 1.301894874440678-2.913661635478200i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).asech());
        }
    }

    @Test
    public void acsch() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                     -2-3i,                                     -2+3i,                                     2-3i,                                     2+3i,                  -3i,                    3i,                                -0.5i,                                  0.5i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ -0.15735549884498543+0.22996290237720785i, -0.15735549884498543-0.22996290237720785i, 0.15735549884498543+0.22996290237720785i, 0.15735549884498543-0.22996290237720785i, 0.33983690945412194i, -0.33983690945412194i, 1.316957896924817+1.570796326794897i, -1.316957896924817-1.570796326794897i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).acsch());
        }
    }

    @Test
    public void and() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i, 2+3i, 2+3i, 2+3i, 2+3i,    2+3i }");
        ArrayList<BaseNumber> paramValues =     parseValues("{  NaN,    2,  2.5,   PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  NaN,  NaN,  NaN,  NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).and(paramValues.get(i)));
        }
    }

    @Test
    public void or() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i, 2+3i, 2+3i, 2+3i, 2+3i,    2+3i }");
        ArrayList<BaseNumber> paramValues =     parseValues("{  NaN,    2,  2.5,   PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  NaN,  NaN,  NaN,  NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).or(paramValues.get(i)));
        }
    }

    @Test
    public void xor() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i, 2+3i, 2+3i, 2+3i, 2+3i,    2+3i }");
        ArrayList<BaseNumber> paramValues =     parseValues("{  NaN,    2,  2.5,   PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  NaN,  NaN,  NaN,  NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).xor(paramValues.get(i)));
        }
    }

    @Test
    public void not() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).not());
        }
    }

    @Test
    public void shl() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i, 2+3i, 2+3i, 2+3i, 2+3i,    2+3i }");
        ArrayList<BaseNumber> paramValues =     parseValues("{  NaN,    2,  2.5,   PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  NaN,  NaN,  NaN,  NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).shl(paramValues.get(i)));
        }
    }

    @Test
    public void shr() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i, 2+3i, 2+3i, 2+3i, 2+3i,    2+3i }");
        ArrayList<BaseNumber> paramValues =     parseValues("{  NaN,    2,  2.5,   PI, 2+3i, M1x1[5] }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{  NaN,  NaN,  NaN,  NaN,  NaN,     NaN }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).shr(paramValues.get(i)));
        }
    }

    @Test
    public void det() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i, 2-3i, -2+3i, -2-3i, -2i, 2i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2+3i, 2-3i, -2+3i, -2-3i, -2i, 2i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).det());
        }
    }

    @Test
    public void transpose() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i, 2-3i, -2+3i, -2-3i, -2i, 2i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2+3i, 2-3i, -2+3i, -2-3i, -2i, 2i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).transpose());
        }
    }

    @Test
    public void tr() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i, 2-3i, -2+3i, -2-3i, -2i, 2i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2+3i, 2-3i, -2+3i, -2-3i, -2i, 2i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).trace());
        }
    }

    @Test
    public void adj() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i, 2-3i, -2+3i, -2-3i, -2i, 2i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{    1,    1,     1,     1,   1,  1 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).adjugate());
        }
    }

    @Test
    public void rank() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i, 2-3i, -2+3i, -2-3i, -2i, 2i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{    1,    1,     1,     1,   1,  1 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).rank());
        }
    }

    @Test
    public void inv() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{                                  2+3i,                                  2-3i,                                  -2+3i,                                  -2-3i,  -2i,    2i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 0.153846153846154-0.2307692307692308i, 0.153846153846154+0.2307692307692308i, -0.153846153846154-0.2307692307692308i, -0.153846153846154+0.2307692307692308i, 0.5i, -0.5i }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).inv());
        }
    }

    @Test
    public void gauss() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i, 2-3i, -2+3i, -2-3i, -2i, 2i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{    0,    0,     0,     0,   0,  0 }");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).gauss());
        }
    }

    @Test
    public void toRadians() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i,                                       2+3i,                                       2+3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2+3i, 0.034906585039886592+0.052359877559829887i, 0.031415926535897932+0.047123889803846899i }");
        AngleType[] angles =                    { AngleType.RAD, AngleType.DEG, AngleType.GRAD };
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).toRadians(angles[i]));
        }
    }

    @Test
    public void fromRadians() throws Exception {
        ArrayList<BaseNumber> inputValues =     parseValues("{ 2+3i,                                   2+3i,                                  2+3i }");
        ArrayList<BaseNumber> expectedValues =  parseValues("{ 2+3i, 114.59155902616464+171.88733853924696i, 127.32395447351627+190.9859317102744i }");
        AngleType[] angles =                    { AngleType.RAD, AngleType.DEG, AngleType.GRAD };
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(inputValues.get(i).getType(), NumberType.COMPLEX);
            compareResultAndPut(expectedValues.get(i), inputValues.get(i).fromRadians(angles[i]));
        }
    }

}
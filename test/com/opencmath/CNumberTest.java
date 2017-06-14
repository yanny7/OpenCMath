package com.opencmath;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static com.opencmath.TestHelper.compareResultAndPut;
import static com.opencmath.TestHelper.parseValue;

public class CNumberTest {

    @Rule
    public TestRule watcher = new TestPoolWatcher();

    @Test
    public void add() throws Exception {
        String[][] strings = new String[][] {
                {                   "2",                "NaN",                                                  "NaN" },
                {                   "2",                  "0",                                                    "2" },
                {                   "2",                  "2",                                                    "4" },
                {                   "2",                "2.5",                                                  "4.5" },
                {                   "2",               "2+3i",                                                 "4+3i" },
                {                   "2",      "M2x2[0;1;2;3]",                                        "M2x2[2;3;4;5]" },
                {                 "2.5",                "NaN",                                                  "NaN" },
                {                 "2.5",                  "2",                                                  "4.5" },
                {                 "2.5",                "2.5",                                                    "5" },
                {                 "2.5",               "2+3i",                                               "4.5+3i" },
                {                 "2.5",      "M2x2[0;1;2;3]",                                "M2x2[2.5;3.5;4.5;5.5]" },
                {                "2+3i",                "NaN",                                                  "NaN" },
                {                "2+3i",                  "2",                                                 "4+3i" },
                {                "2+3i",                "2.5",                                               "4.5+3i" },
                {                "2+3i",               "2+3i",                                                 "4+6i" },
                {                "2+3i",      "M2x2[0;1;2;3]",                            "M2x2[2+3i;3+3i;4+3i;5+3i]" },
        };

        for (String[] string : strings) {
            CBase input = parseValue(string[0]);
            CBase param = parseValue(string[1]);
            CBase expected = parseValue(string[2]);
            compareResultAndPut(expected, input.add(param));
        }
    }

    @Test
    public void sub() throws Exception {
        String[][] strings = new String[][] {
                {                   "2",                "NaN",                                                  "NaN" },
                {                   "2",                  "0",                                                    "2" },
                {                   "2",                  "2",                                                    "0" },
                {                   "2",                "2.5",                                                 "-0.5" },
                {                   "2",               "2+3i",                                                  "-3i" },
                {                   "2",      "M2x2[0;1;2;3]",                                       "M2x2[2;1;0;-1]" },
                {                 "2.5",                "NaN",                                                  "NaN" },
                {                 "2.5",                  "2",                                                  "0.5" },
                {                 "2.5",                "2.5",                                                    "0" },
                {                 "2.5",               "2+3i",                                               "0.5-3i" },
                {                 "2.5",      "M2x2[0;1;2;3]",                               "M2x2[2.5;1.5;0.5;-0.5]" },
                {                "2+3i",                "NaN",                                                  "NaN" },
                {                "2+3i",                  "2",                                                   "3i" },
                {                "2+3i",                "2.5",                                              "-0.5+3i" },
                {                "2+3i",               "2+3i",                                                    "0" },
                {                "2+3i",      "M2x2[0;1;2;3]",                             "M2x2[2+3i;1+3i;3i;-1+3i]" },
        };

        for (String[] string : strings) {
            CBase input = parseValue(string[0]);
            CBase param = parseValue(string[1]);
            CBase expected = parseValue(string[2]);
            compareResultAndPut(expected, input.sub(param));
        }
    }

    @Test
    public void mul() throws Exception {
        String[][] strings = new String[][] {
                {                   "2",                "NaN",                                                  "NaN" },
                {                   "2",                  "0",                                                    "0" },
                {                   "2",                  "2",                                                    "4" },
                {                   "2",                "2.5",                                                    "5" },
                {                   "2",               "2+3i",                                                 "4+6i" },
                {                   "2",      "M2x2[0;1;2;3]",                                        "M2x2[0;2;4;6]" },
                {                 "2.5",                "NaN",                                                  "NaN" },
                {                 "2.5",                  "2",                                                    "5" },
                {                 "2.5",                "2.5",                                                 "6.25" },
                {                 "2.5",               "2+3i",                                               "5+7.5i" },
                {                 "2.5",      "M2x2[0;1;2;3]",                                    "M2x2[0;2.5;5;7.5]" },
                {                "2+3i",                "NaN",                                                  "NaN" },
                {                "2+3i",                  "2",                                                 "4+6i" },
                {                "2+3i",                "2.5",                                               "5+7.5i" },
                {                "2+3i",               "2+3i",                                               "-5+12i" },
                {                "2+3i",      "M2x2[0;1;2;3]",                               "M2x2[0;2+3i;4+6i;6+9i]" },
        };

        for (String[] string : strings) {
            CBase input = parseValue(string[0]);
            CBase param = parseValue(string[1]);
            CBase expected = parseValue(string[2]);
            compareResultAndPut(expected, input.mul(param));
        }
    }

    @Test
    public void div() throws Exception {
        String[][] strings = new String[][] {
                {                   "2",                "NaN",                                                  "NaN" },
                {                   "2",                  "0",                                                  "NaN" },
                {                   "2",                  "2",                                                    "1" },
                {                   "2",                "2.5",                                                  "0.8" },
                {                   "2",               "2+3i",              "0.3076923076923077-0.46153846153846154i" },
                {                   "2",      "M2x2[0;1;2;3]",                    "M2x2[NaN;2;1;0.66666666666666667]" },
                {                 "2.5",                "NaN",                                                  "NaN" },
                {                 "2.5",                  "2",                                                 "1.25" },
                {                 "2.5",                "2.5",                                                    "1" },
                {                 "2.5",               "2+3i",             "0.38461538461538462-0.57692307692307692i" },
                {                 "2.5",      "M2x2[0;1;2;3]",                 "M2x2[0;2.5;1.25;0.83333333333333333]" },
                {                "2+3i",                "NaN",                                                  "NaN" },
                {                "2+3i",                  "2",                                               "1+1.5i" },
                {                "2+3i",                "2.5",                                             "0.8+1.2i" },
                {                "2+3i",               "2+3i",                                                    "1" },
                {                "2+3i",      "M2x2[0;1;2;3]",           "M2x2[0;2+3i;1+1.5i;0.66666666666666667+1i]" },
        };

        for (String[] string : strings) {
            CBase input = parseValue(string[0]);
            CBase param = parseValue(string[1]);
            CBase expected = parseValue(string[2]);
            compareResultAndPut(expected, input.div(param));
        }
    }

    @Test
    public void arg() throws Exception {
        String[][] strings = new String[][] {
                {                 "NaN",                                                                        "NaN" },
                {                   "0",                                                                          "0" },
                {                  "-2",                                                                         "PI" },
                {                   "2",                                                                          "0" },
                {                 "2.5",                                                                          "0" },
                {               "-2-3i",                                                        "-2.1587989303424642" },
                {               "-2+3i",                                                         "2.1587989303424642" },
                {                "2-3i",                                                       "-0.98279372324732907" },
                {                "2+3i",                                                        "0.98279372324732907" },
                {                 "-3i",                                                        "-1.5707963267948966" },
                {                  "3i",                                                         "1.5707963267948966" },
        };

        for (String[] string : strings) {
            CBase input = parseValue(string[0]);
            CBase expected = parseValue(string[1]);
            compareResultAndPut(expected, input.arg());
        }
    }

    @Test
    public void ln() throws Exception {
        String[][] strings = new String[][] {
                {                 "NaN",                                                                        "NaN" },
                {                   "0",                                                                  "-Infinity" },
                {                  "-2",                                    "0.69314718055994531+3.1415926535897932i" },
                {                   "2",                                                        "0.69314718055994531" },
                {                 "2.5",                                                        "0.91629073187415507" },
                {               "-2-3i",                                     "1.2824746787307684-2.1587989303424642i" },
                {               "-2+3i",                                     "1.2824746787307684+2.1587989303424642i" },
                {                "2-3i",                                     "1.2824746787307684-0.9827937232473291i" },
                {                "2+3i",                                     "1.2824746787307684+0.9827937232473291i" },
                {                 "-3i",                                     "1.0986122886681097-1.5707963267948966i" },
                {                  "3i",                                     "1.0986122886681097+1.5707963267948966i" },
        };

        for (String[] string : strings) {
            CBase input = parseValue(string[0]);
            CBase expected = parseValue(string[1]);
            compareResultAndPut(expected, input.ln());
        }
    }

    @Test
    public void log() throws Exception {
        String[][] strings = new String[][] {
                {                   "2",                "NaN",                                                  "NaN" },
                {                   "2",                  "0",                                                    "0" },
                {                   "2",                  "2",                                                    "1" },
                {                   "2",                "2.5",                                  "0.75647079736603003" },
                {                   "2",               "2+3i",             "0.34050994471749278-0.26094163254969080i" },
                {                   "2",      "M2x2[0;1;2;3]",                    "M2x2[0;NaN;1;0.63092975357145744]" },
                {                 "2.5",                "NaN",                                                  "NaN" },
                {                 "2.5",                  "2",                                   "1.3219280948873623" },
                {                 "2.5",                "2.5",                                                    "1" },
                {                 "2.5",               "2+3i",             "0.45012966251059631-0.34494607519321090i" },
                {                 "2.5",      "M2x2[0;1;2;3]",   "M2x2[0;NaN;1.3219280948873623;0.83404376714646973]" },
                {                "2+3i",                "NaN",                                                  "NaN" },
                {                "2+3i",                  "2",               "1.8502198590705461+1.4178716307457220i" },
                {                "2+3i",                "2.5",               "1.3996372920935597+1.0725784830728896i" },
                {                "2+3i",               "2+3i",                                                    "1" },
                {                "2+3i",      "M2x2[0;1;0;1]",                                    "M2x2[NaN;0;NaN;0]" },
        };

        for (String[] string : strings) {
            CBase input = parseValue(string[0]);
            CBase param = parseValue(string[1]);
            CBase expected = parseValue(string[2]);
            compareResultAndPut(expected, input.log(param));
        }
    }

    @Test
    public void exp() throws Exception {
        String[][] strings = new String[][] {
                {                 "NaN",                                                                        "NaN" },
                {                   "0",                                                                  "-Infinity" },
                {                  "-2",                                                        "0.13533528323661269" },
                {                   "2",                                                         "7.3890560989306502" },
                {                 "2.5",                                                         "12.182493960703473" },
                {               "-2-3i",                                "-0.133980914929542613-0.019098516261135196i" },
                {               "-2+3i",                                "-0.133980914929542613+0.019098516261135196i" },
                {                "2-3i",                                    "-7.3151100949011025-1.0427436562359044i" },
                {                "2+3i",                                    "-7.3151100949011025+1.0427436562359044i" },
                {                 "-3i",                                       "-0.989992496600445-0.14112000805987i" },
                {                  "3i",                                       "-0.989992496600445+0.14112000805987i" },
        };

        for (String[] string : strings) {
            CBase input = parseValue(string[0]);
            CBase expected = parseValue(string[1]);
            compareResultAndPut(expected, input.exp());
        }
    }

    @Test
    public void pow() throws Exception {
        String[][] strings = new String[][] {
                {                   "2",                "NaN",                                                  "NaN" },
                {                   "2",                  "0",                                                    "1" },
                {                   "2",                  "2",                                                    "4" },
                {                   "2",                "2.5",                                   "5.6568542494923802" },
                {                   "2",               "2+3i",              "-1.9479776718631256+3.4936203270994856i" },
                {                   "2",      "M2x2[0;1;2;3]",                                        "M2x2[1;2;4;8]" },
                {                 "2.5",                "NaN",                                                  "NaN" },
                {                 "2.5",                  "2",                                                 "6.25" },
                {                 "2.5",                "2.5",                                   "9.8821176880261854" },
                {                 "2.5",               "2+3i",              "-5.7741959497272868+2.3918948835918766i" },
                {                 "2.5",      "M2x2[0;1;2;3]",                              "M2x2[1;2.5;6.25;15.625]" },
                {                "2+3i",                "NaN",                                                  "NaN" },
                {                "2+3i",                  "2",                                               "-5+12i" },
                {                "2+3i",                "2.5",              "-19.122475853735758+15.609903355777290i" },
                {                "2+3i",               "2+3i",             "0.60756666473147822-0.30875601809790225i" },
                {                "2+3i",      "M2x2[0;1;2;3]",                           "M2x2[0;2+3i;-5+12i;-46+9i]" },
        };

        for (String[] string : strings) {
            CBase input = parseValue(string[0]);
            CBase param = parseValue(string[1]);
            CBase expected = parseValue(string[2]);
            compareResultAndPut(expected, input.pow(param));
        }
    }

    @Test
    public void root() throws Exception {
        String[][] strings = new String[][] {
                {                   "2",                "NaN",                                                  "NaN" },
                {                   "2",                  "0",                                             "Infinity" },
                {                   "2",                  "2",                                  "1.41421356237309505" },
                {                   "2",                "2.5",                                  "1.31950791077289426" },
                {                   "2",               "2+3i",             "1.09832904001806791-0.17719933735321196i" },
                {                   "2",    "M2x2[0;1;2;0.5]",               "M2x2[Infinity;2;1.41421356237309505;4]" },
                {                 "2.5",                "NaN",                                                  "NaN" },
                {                 "2.5",                  "2",                                   "1.5811388300841897" },
                {                 "2.5",                "2.5",                                   "1.4426999059072136" },
                {                 "2.5",               "2+3i",              "1.1257430238896380-0.24165264275788171i" },
                {                 "2.5",    "M2x2[0;1;2;0.5]",           "M2x2[Infinity;2.5;1.5811388300841897;6.25]" },
                {                "2+3i",                "NaN",                                                  "NaN" },
                {                "2+3i",                  "2",              "1.6741492280355400+0.89597747612983812i" },
                {                "2+3i",                "2.5",              "1.5428677605929858+0.63983318858267321i" },
                {                "2+3i",               "2+3i",              "1.5122344211248442-0.22044795193453988i" },
                {                "2+3i",  "M2x2[0;1;0.5;0.2]",                       "M2x2[NaN;2+3i;-5+12i;122-597i]" },
        };

        for (String[] string : strings) {
            CBase input = parseValue(string[0]);
            CBase param = parseValue(string[1]);
            CBase expected = parseValue(string[2]);
            compareResultAndPut(expected, input.root(param));
        }
    }

    @Test
    public void factorial() throws Exception {
        String[][] strings = new String[][] {
                {                 "NaN",                                                                        "NaN" },
                {                   "0",                                                                          "1" },
                {                  "-2",                                                                        "NaN" },
                {                   "2",                                                                          "2" },
                {                  "10",                                                                    "3628800" },
                {                  "20",                                                        "2432902008176640000" },
                {                 "100",                                                     "9.3326215443944153E157" },
                {                 "2.5",                                                         "3.3233509704478426" },
                {               "-2-3i",                              "-0.0030591429145086438+0.0027465090888537295i" },
                {               "-2+3i",                              "-0.0030591429145086438-0.0027465090888537295i" },
                {                "2-3i",                                  "-0.44011340763700171+0.06363724312631702i" },
                {                "2+3i",                                  "-0.44011340763700171-0.06363724312631702i" },
                {                 "-3i",                                 "0.019292758964016606-0.033896010543209497i" },
                {                  "3i",                                 "0.019292758964016606+0.033896010543209497i" },
        };

        for (String[] string : strings) {
            CBase input = parseValue(string[0]);
            CBase expected = parseValue(string[1]);
            compareResultAndPut(expected, input.factorial());
        }
    }

    @Test
    public void sin() throws Exception {
        String[][] strings = new String[][] {
                {                 "NaN",                                                                        "NaN" },
                {                   "0",                                                                  "-Infinity" },
                {                  "-2",                                    "0.69314718055994531+3.1415926535897932i" },
                {                   "2",                                                        "0.69314718055994531" },
                {                 "2.5",                                                        "0.91629073187415507" },
                {               "-2-3i",                                     "1.2824746787307684-2.1587989303424642i" },
                {               "-2+3i",                                     "1.2824746787307684+2.1587989303424642i" },
                {                "2-3i",                                     "1.2824746787307684-0.9827937232473291i" },
                {                "2+3i",                                     "1.2824746787307684+0.9827937232473291i" },
                {                 "-3i",                                     "1.0986122886681097-1.5707963267948966i" },
                {                  "3i",                                     "1.0986122886681097+1.5707963267948966i" },
        };

        for (String[] string : strings) {
            CBase input = parseValue(string[0]);
            CBase expected = parseValue(string[1]);
            compareResultAndPut(expected, input.sin());
        }
    }

}